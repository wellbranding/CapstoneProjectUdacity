package udacityteam.healthapp.completeRedesign;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.util.List;

import javax.inject.Inject;

import io.apptik.widget.MultiSlider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;
import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.SharedFoodProductsRetrofit;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.activities.FoodNutritiensDisplayPrieview;
import udacityteam.healthapp.app.ApplicationController;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.RetrofitFactoryNew;
import udacityteam.healthapp.completeRedesign.Repository.RecipiesRepository;
import udacityteam.healthapp.completeRedesign.Repository.Resource;
import udacityteam.healthapp.completeRedesign.Repository.Status;
public class SharedFoodListsViewModelNew extends ViewModel {

    private static final String TAG ="trxt" ;
    private SelectedFoodretrofit selectedFoodretrofit;
    private Context context;
    private String foodId;
    private String foodName;
    private String UserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String SendDate;
    private float Calories;
    private float Protein;
    private float Fat;
    private float Carbohydrates;
    private Subscription subscription;
    private  DataListener dataListener;
    private LiveData<Resource<List<OneSharedFoodProductsListRetrofit>>> recipes;

    List<OneSharedFoodProductsListRetrofit> selectedFoodretrofits;
    RecipiesRepository repository;

    MutableLiveData<String> whichTime = new MutableLiveData<>();
    public void setWhichTime(String value)
    {
        if(whichTime.getValue()!=null) {
            if (whichTime.getValue().equals(value)) {
                return;
            }
        }else {

                whichTime.setValue(value);
            }

    }




    public MutableLiveData<Resource<List<OneSharedFoodProductsListRetrofit>>> mutableLiveData = new MutableLiveData<>();
    ApplicationController application = null;

    public LiveData<Resource<List<OneSharedFoodProductsListRetrofit>>> getFilteredData()
    {
        return mutableLiveData;
    }

    @Inject
    public SharedFoodListsViewModelNew(RecipiesRepository recipiesRepository) {
        this.repository = recipiesRepository;
        //Transofrmations();
    }

    public LiveData<Resource<List<OneSharedFoodProductsListRetrofit>>> getRecipes() {
//    recipes = Transformations.switchMap(whichTime,
//              important -> repository.loadSharedFoodLists(important));
//      return recipes;

        if (recipes == null) {
            recipes = repository.loadSharedFoodLists(whichTime.getValue());
        }
        return recipes;
    }

    public String getName() {
        return selectedFoodretrofit.getFoodid();
    }

    public void setSelectectedFoood(SelectedFoodretrofit selectedFoodretrofit) {
        this.selectedFoodretrofit = selectedFoodretrofit;
      notify();
    }
    public void onItemClick(View view) {
        application.getApplicationContext().startActivity(FoodNutritiensDisplayPrieview.newIntent(context, selectedFoodretrofit));
    }

    public  void GetFilteredSharedDiets(MultiSlider protein, MultiSlider calories,
                                        MultiSlider carbohydrates, MultiSlider fats
    , String SharedFoodListDatabase) //only if today
    {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        udacityteam.healthapp.completeRedesign.Data.Networking.API.APIService service = RetrofitFactoryNew.create();

        Call<SharedFoodProductsRetrofit> call = service.getAllFilteredSharedDiets(2
                , SharedFoodListDatabase, protein.getThumb(0).getValue(), protein.getThumb(1).getValue(),
                calories.getThumb(0).getValue(), calories.getThumb(1).getValue(),
                carbohydrates.getThumb(0).getValue(), carbohydrates.getThumb(1).getValue(),
                fats.getThumb(0).getValue(), fats.getThumb(1).getValue()
        );
        call.enqueue(new Callback<SharedFoodProductsRetrofit>() {
            @Override
            public void onResponse(Call<SharedFoodProductsRetrofit> call, Response<SharedFoodProductsRetrofit> response) {
                List<OneSharedFoodProductsListRetrofit> selectedFoodretrofits = response.body().
                        getSelectedFoodretrofits();
                if(response.isSuccessful()){
                    mutableLiveData.setValue(new Resource(Status.SUCCESS, response.body().getSelectedFoodretrofits(), "SUCCESS"));
                }
             //   mutableLiveData.setValue(selectedFoodretrofits);

                //notify();

                if (selectedFoodretrofits.size() != 0) {
                //    Toast.makeText(application.getApplicationContext(), String.valueOf(selectedFoodretrofits.get(0).getCalories()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SharedFoodProductsRetrofit> call, Throwable t) {

            }

        });

    }

    public interface DataListener {
        void onRepositoriesChanged(List<OneSharedFoodProductsListRetrofit> repositories);
    }

}
