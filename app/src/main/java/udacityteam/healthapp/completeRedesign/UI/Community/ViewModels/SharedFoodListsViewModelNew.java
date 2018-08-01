package udacityteam.healthapp.completeRedesign.UI.Community.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.apptik.widget.MultiSlider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SharedFoodProductsRetrofit;
import udacityteam.healthapp.completeRedesign.ApplicationController;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.RetrofitFactoryNew;
import udacityteam.healthapp.completeRedesign.Repository.RecipiesRepository;
import udacityteam.healthapp.completeRedesign.Repository.Resource;
import udacityteam.healthapp.completeRedesign.Repository.Status;
public class SharedFoodListsViewModelNew extends ViewModel {

    private SelectedFoodretrofit selectedFoodretrofit;
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
    }

    public LiveData<Resource<List<OneSharedFoodProductsListRetrofit>>> getRecipes() {
        if (recipes == null) {
            recipes = repository.loadSharedFoodLists(whichTime.getValue());
        }
        return recipes;
    }

    public String getName() {
        return selectedFoodretrofit.getFoodid();
    }


    public  void GetFilteredSharedDiets(MultiSlider protein, MultiSlider calories,
                                        MultiSlider carbohydrates, MultiSlider fats
    , String SharedFoodListDatabase) //only if today
    {
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


}
