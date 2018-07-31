package udacityteam.healthapp.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import udacityteam.healthapp.Model.Result;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofitarray;
import udacityteam.healthapp.Network.PHPService;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.R;
import udacityteam.healthapp.app.ApplicationController;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.ApiResponse;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.RetrofitFactoryNew;
import udacityteam.healthapp.completeRedesign.Repository.RecipiesRepository;
import udacityteam.healthapp.models.SelectedFood;
import udacityteam.healthapp.models.SelectedFoodmodel;

/**
 * View model for the MainActivity
 */
public class MainActivityViewModelGood extends ViewModel {

    private static final String TAG = "MainViewModel";

    public String sharedFoodListDatabase;
    public String foodselection;

    private Context context;
    private Subscription subscription;
    public  List<SelectedFoodretrofit> selectedFoodretrofits;

    SimpleDateFormat format;
    //private List<Repository> repositories;
    private String editTextUsernameValue;
    public  Float verte;
    private RecipiesRepository repository;
    private MediatorLiveData<ApiResponse<Result>> resultMediatorLiveData;
    private LiveData<ApiResponse<Result>> apiResponseLiveData;

    private MutableLiveData<Boolean> hasPosted;

    @Inject
    SharedPreferences sharedPreferences;



    @Inject
    public MainActivityViewModelGood(RecipiesRepository recipiesRepository) {
        this.repository = recipiesRepository;
        hasPosted = new MutableLiveData<>();
        IfHasPosted();

    }

    public void IfHasPosted()
    {
         Transformations.switchMap(hasPosted, result -> {

             resultMediatorLiveData = repository.getResultMediatorLiveData();
               return resultMediatorLiveData;

        });

    }



    public LiveData<String> signOut()
    {

       return repository.SignOut();
    }



    public void setHasPosted(MutableLiveData<Boolean> hasPosted) {
        this.hasPosted = hasPosted;
    }

    public MediatorLiveData<ApiResponse<Result>> getResultMediatorLiveData() {

        return repository.getResultMediatorLiveData();
    }

    //  CALL IN RETROFIT MIGHT BE WRAPPED WITH LIVEDATA ON RESPONSE ADD TO REPOSITORY AND OPEN ACTIVITY
    public void AddFoodtoDatabase(String foodselection, String foodId, String foodName, List<Float> nutritiens) {
        repository.AddFoodtoDatabase(foodselection, foodId, foodName, nutritiens);
//        Date date = new Date();
//        Date newDate = new Date(date.getTime());
//        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        Log.d("timestamp", timestamp.toString());
//
//        String stringdate = dt.format(newDate);
//
//
//        LiveData<List<SelectedFoodretrofit>> getAddedFoods = repository.
//                getAddedFoods(foodselection, String.valueOf(newDate.getYear()),
//                        String.valueOf(newDate.getMonth()),
//                        String.valueOf(newDate.getDay()));
//resultMediatorLiveData.addSource();
//      apiResponseLiveData = RetrofitFactoryNew.create().addSelectedFood(
//                foodId,foodName,
//                2, timestamp, nutritiens.get(0)
//                ,nutritiens.get(1),nutritiens.get(2),nutritiens.get(3),
//                foodselection,0
//
//        );
//        resultMediatorLiveData.addSource(apiResponseLiveData, result->
//        {
//            resultMediatorLiveData.removeSource(apiResponseLiveData);
//            if(result.isSuccessful()) {
//                resultMediatorLiveData.removeSource(apiResponseLiveData);
//
//                resultMediatorLiveData.postValue(result);
//            }
//        });

    }




}
