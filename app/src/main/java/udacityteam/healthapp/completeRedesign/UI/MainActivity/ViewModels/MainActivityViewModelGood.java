package udacityteam.healthapp.completeRedesign.UI.MainActivity.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.ApiResponse;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.Result;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.completeRedesign.Repository.MainRepository;

public class MainActivityViewModelGood extends ViewModel {

    private static final String TAG = "MainViewModel";

    public String foodselection;
    private MainRepository repository;
    private MediatorLiveData<ApiResponse<Result>> resultMediatorLiveData;
    private LiveData<ApiResponse<Result>> apiResponseLiveData;

    private MutableLiveData<Boolean> hasPosted;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    public MainActivityViewModelGood(MainRepository mainRepository) {
        this.repository = mainRepository;
        hasPosted = new MutableLiveData<>();
        IfHasPosted();

    }

    public void IfHasPosted() {
        Transformations.switchMap(hasPosted, result -> {
            resultMediatorLiveData = repository.getResultMediatorLiveData();
            return resultMediatorLiveData;

        });

    }

    public LiveData<String> signOut() {

        return repository.SignOut();
    }

    public MediatorLiveData<ApiResponse<Result>> getResultMediatorLiveData() {

        return repository.getResultMediatorLiveData();
    }

    public void AddFoodtoDatabase(String foodselection, String foodId, String foodName, List<Float> nutritiens) {
        repository.AddFoodtoDatabase(foodselection, foodId, foodName, nutritiens);

    }


}
