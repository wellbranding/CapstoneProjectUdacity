package udacityteam.healthapp.completeRedesign;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.Result;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofitarray;
import udacityteam.healthapp.Network.PHPService;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.RetrofitFactoryNew;
import udacityteam.healthapp.completeRedesign.Repository.RecipiesRepository;
import udacityteam.healthapp.completeRedesign.Repository.Resource;
import udacityteam.healthapp.completeRedesign.Repository.Status;
import udacityteam.healthapp.completeRedesign.Utils.SingleLiveEvent;

/**
 * View model for the MainActivity
 */
public class FoodListViewModelComplete extends ViewModel implements Observer<Resource<List<SelectedFoodretrofit>>> {

    private static final String TAG = "MainViewModel";

    public ObservableField<String> infoMessage;
    public ObservableField<String> canshare;
    public  ObservableField<Boolean> isshared;
    public  String caloriesCount;
    public  String proteinCount;
    public  String fatsCount;
    public  String carbosCount;



    private Subscription subscription;
    String foodselection;
    String sharedFoodListDatabase;;
    public  List<SelectedFoodretrofit> selectedFoodretrofits;
    public MutableLiveData<List<SelectedFoodretrofit>> mutableLiveData;
    //private List<Repository> repositories;
    private String editTextUsernameValue;
    public  Float verte;
    public RecipiesRepository repository;

    private MutableLiveData<Result> shareResult = new SingleLiveEvent<>();

    public LiveData<Result> getShareResult() {
        return shareResult;
    }


    private MutableLiveData<List<String>> nutritionDisplay;
    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    public FoodListViewModelComplete(RecipiesRepository recipiesRepository) {
        this.repository = recipiesRepository;
        nutritionDisplay = new MutableLiveData<>();

    }

    LiveData<Resource<List<SelectedFoodretrofit>>> selectedFoodsLists;

    MutableLiveData<DataForRequest> dataForRequestMutableLiveData = new MutableLiveData<>();

    @Override
    public void onChanged(@Nullable Resource<List<SelectedFoodretrofit>> listResource) {
        if(listResource!=null) {
            if (listResource.status == Status.SUCCESS) {

                if(listResource.data!=null)
                {
                    CalculateNutritionsDisplay(listResource.data);
                }
            }
        }
    }

    private class DataForRequest
    {
        private String year;
        private String month;
        private String day;
        private String whichTime;

        public DataForRequest(String year, String month, String day, String whichTime) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.whichTime = whichTime;
        }
        private boolean eQuals(String whichTime, String year, String month, String day)
        {
            if(this.year.equals(year) && this.day.equals(day) && this.month.equals(month)
                    && this.whichTime.equals(whichTime))
            {
                return true;
            }
            return false;
        }
    }
    public void setDataForRequestMutableLiveData(String whichTime, String year, String month,
                                                 String day)
    {
           if(dataForRequestMutableLiveData.getValue()==null)
           {
               dataForRequestMutableLiveData.setValue(new DataForRequest(year, month,
                       day, whichTime));
           }
           else
           {
               if(dataForRequestMutableLiveData.getValue().eQuals(whichTime, year, month, day))
               {
                   return;
               }
               else
               {
                   dataForRequestMutableLiveData.setValue(new DataForRequest(year, month,
                           day, whichTime));
               }
           }
    }



//    public FoodListViewModelComplete()
//    {
//
//    }

    public LiveData<Resource<List<SelectedFoodretrofit>>> getFoodLists() {
        if(selectedFoodsLists==null)
        {
            selectedFoodsLists = repository.getAddedFoods(dataForRequestMutableLiveData.getValue()
            .whichTime, dataForRequestMutableLiveData.getValue()
                    .year, dataForRequestMutableLiveData.getValue()
                    .month, dataForRequestMutableLiveData.getValue()
                    .day);
         //   if(selectedFoodsLists.getValue()!=null && selectedFoodsLists.getValue().data!=null)
        //    CalculateNutritionsDisplay(selectedFoodsLists.getValue().data);
           selectedFoodsLists.observeForever(this);
        }
      return selectedFoodsLists;
    }


    public void CalculateNutritionsDisplay(List<SelectedFoodretrofit> selectedFoodretrofits)
    {
        float calories = 0;
        float protein = 0;
        float carbos = 0;
        float fats = 0;
        for(int i = 0; i<selectedFoodretrofits.size(); i++)
        {
            calories+=selectedFoodretrofits.get(i).getCalories();
            protein+=selectedFoodretrofits.get(i).getProtein();
            carbos+=selectedFoodretrofits.get(i).getCarbohydrates();
            fats+=selectedFoodretrofits.get(i).getFat();

        }
        caloriesCount = (String.valueOf(Math.round(calories*100.0)/100.0));
        proteinCount = (String.valueOf(Math.round(protein*100.0)/100.0));
        carbosCount=(String.valueOf(Math.round(carbos*100.0)/100.0));
        fatsCount=(String.valueOf(Math.round(fats*100.0)/100.0));
        List<String> nutritiens = new ArrayList<>();
        nutritiens.add(caloriesCount);
        nutritiens.add(proteinCount);
        nutritiens.add(carbosCount);
        nutritiens.add(fatsCount);
        nutritionDisplay.setValue(nutritiens);

    }
    public LiveData<List<String>> getNutritionalValue()

    {

        return  nutritionDisplay;
    }
    private void handleResponse(SelectedFoodretrofitarray androidList) {
        Log.d("kietass", "jauu");

    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }


    public void IsShared(String foodselection)
    {
        Log.d("helaalo", "haa");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        udacityteam.healthapp.completeRedesign.Data.Networking.API.APIService service =
                RetrofitFactoryNew.create();

        Call<Result> call = service.getIsShared(
                (sharedPreferences.getInt("userId", -1)),
                timestamp, foodselection
        );
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, Response<Result> response) {
                //  if(response.body().getMessage().equals("notfound"));
                if(response.body().getMessage().equals("Some error occurred")) {
                    canshare.set("UPDATE YOUR DIET");
                    isshared.set(false);
                }
                else {
                    canshare.set("SHARE YOUR DIET");
                    isshared.set(true);
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
            }

        });
    }

    public void ShareFoodList(String foodselection, String sharedFoodListDatabase )
    {


            float protein = 0.0f, carbohydrates = 0.0f, fats = 0.0f, calories = 0.0f;
            for (SelectedFoodretrofit food : selectedFoodsLists.getValue().data
                    ) {
                protein += food.getProtein();
                carbohydrates += food.getCarbohydrates();
                fats += food.getFat();
                calories += food.getCalories();
            }
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
             udacityteam.healthapp.completeRedesign.Data.Networking.API.APIService service =
                RetrofitFactoryNew.create();
             Call<Result> call = service.addSharedList(sharedPreferences.getInt("userId", -1),
                    timestamp,
                    sharedFoodListDatabase, foodselection,
                    calories, protein, fats, carbohydrates
            );
             call.enqueue(new Callback<Result>() {
                 @Override
                 public void onResponse(Call<Result> call, Response<Result> response) {
                     if(response.isSuccessful())
                    shareResult.setValue(response.body());
                     else
                         shareResult.setValue(new Result(true, "Server issue", null));
                 }

                 @Override
                 public void onFailure(Call<Result> call, Throwable t) {
                     Log.d("pavyko", "nepavyko");
                 }
             });






    }




}
