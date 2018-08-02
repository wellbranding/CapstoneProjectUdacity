package udacityteam.healthapp.completeRedesign.UI.AddedFoods.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.RetrofitFactoryNew;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.Result;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.completeRedesign.Repository.MainRepository;
import udacityteam.healthapp.completeRedesign.Repository.Resource;
import udacityteam.healthapp.completeRedesign.Repository.Status;
import udacityteam.healthapp.completeRedesign.Utils.SingleLiveEvent;

public class FoodListViewModelComplete extends ViewModel implements Observer<Resource<List<SelectedFoodretrofit>>> {

    public String caloriesCount;
    public String proteinCount;
    public String fatsCount;
    public String carbosCount;
    public MainRepository repository;

    private MutableLiveData<Result> shareResult = new SingleLiveEvent<>();

    public LiveData<Result> getShareResult() {
        return shareResult;
    }


    private MutableLiveData<List<String>> nutritionDisplay;
    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    public FoodListViewModelComplete(MainRepository mainRepository) {
        this.repository = mainRepository;
        nutritionDisplay = new MutableLiveData<>();

    }

    LiveData<Resource<List<SelectedFoodretrofit>>> selectedFoodsLists;

    MutableLiveData<DataForRequest> dataForRequestMutableLiveData = new MutableLiveData<>();

    @Override
    public void onChanged(@Nullable Resource<List<SelectedFoodretrofit>> listResource) {
        if (listResource != null) {
            if (listResource.status == Status.SUCCESS) {
                if (listResource.data != null) {
                    CalculateNutritionsDisplay(listResource.data);
                }
            }
        }
    }

    private class DataForRequest {
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

        private boolean eQuals(String whichTime, String year, String month, String day) {
            if (this.year.equals(year) && this.day.equals(day) && this.month.equals(month)
                    && this.whichTime.equals(whichTime)) {
                return true;
            }
            return false;
        }
    }

    public void setDataForRequestMutableLiveData(String whichTime, String year, String month,
                                                 String day) {
        if (dataForRequestMutableLiveData.getValue() == null) {
            dataForRequestMutableLiveData.setValue(new DataForRequest(year, month,
                    day, whichTime));
        } else {
            if (dataForRequestMutableLiveData.getValue().eQuals(whichTime, year, month, day)) {
                return;
            } else {
                dataForRequestMutableLiveData.setValue(new DataForRequest(year, month,
                        day, whichTime));
            }
        }
    }


    public LiveData<Resource<List<SelectedFoodretrofit>>> getFoodLists() {
        if (selectedFoodsLists == null) {
            selectedFoodsLists = repository.getAddedFoods(dataForRequestMutableLiveData.getValue()
                    .whichTime, dataForRequestMutableLiveData.getValue()
                    .year, dataForRequestMutableLiveData.getValue()
                    .month, dataForRequestMutableLiveData.getValue()
                    .day);
            selectedFoodsLists.observeForever(this);
        }
        return selectedFoodsLists;
    }


    public void CalculateNutritionsDisplay(List<SelectedFoodretrofit> selectedFoodretrofits) {
        float calories = 0;
        float protein = 0;
        float carbos = 0;
        float fats = 0;
        for (int i = 0; i < selectedFoodretrofits.size(); i++) {
            calories += selectedFoodretrofits.get(i).getCalories();
            protein += selectedFoodretrofits.get(i).getProtein();
            carbos += selectedFoodretrofits.get(i).getCarbohydrates();
            fats += selectedFoodretrofits.get(i).getFat();

        }
        caloriesCount = (String.valueOf(Math.round(calories * 100.0) / 100.0));
        proteinCount = (String.valueOf(Math.round(protein * 100.0) / 100.0));
        carbosCount = (String.valueOf(Math.round(carbos * 100.0) / 100.0));
        fatsCount = (String.valueOf(Math.round(fats * 100.0) / 100.0));
        List<String> nutritiens = new ArrayList<>();
        nutritiens.add(caloriesCount);
        nutritiens.add(proteinCount);
        nutritiens.add(carbosCount);
        nutritiens.add(fatsCount);
        nutritionDisplay.setValue(nutritiens);

    }

    public LiveData<List<String>> getNutritionalValue()
    {
        return nutritionDisplay;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }


    public void ShareFoodList(String foodselection, String sharedFoodListDatabase) {


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
                if (response.isSuccessful())
                    shareResult.setValue(response.body());
                else
                    shareResult.setValue(new Result(true, "Server issue", null));
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });

    }


}
