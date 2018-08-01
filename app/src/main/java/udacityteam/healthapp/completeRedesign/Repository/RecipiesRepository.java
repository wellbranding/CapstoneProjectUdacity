package udacityteam.healthapp.completeRedesign.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;


import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.Result;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofitarray;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SharedFoodProductsRetrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.UserRetrofitGood;
import udacityteam.healthapp.completeRedesign.Dagger2.AppExecutors;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.APIService;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.ApiResponse;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.RetrofitFactoryNew;
import udacityteam.healthapp.completeRedesign.db.MainDatabase;
import udacityteam.healthapp.completeRedesign.db.RecipesMainDao;

public class RecipiesRepository {

    APIService webservice;
    RecipesMainDao userDao;
    APIService apiService;
    AppExecutors appExecutors;
    MainDatabase mainDatabase;

    @Inject
    SharedPreferences sharedPreferences;

    public MainDatabase getMainDatabase() {
        return mainDatabase;
    }

    private MutableLiveData<String> signOut;



    public RecipesMainDao getUserDao() {
        return userDao;
    }

    private MediatorLiveData<ApiResponse<Result>> resultMediatorLiveData = new MediatorLiveData<>();
    private LiveData<ApiResponse<Result>> apiResponseLiveData;

    public MediatorLiveData<ApiResponse<Result>> getResultMediatorLiveData() {
        return resultMediatorLiveData;
    }



    public LiveData<String> SignOut()
    {
        sharedPreferences.edit().putInt("userId", -1).commit();
        Completable.fromAction(() ->
           mainDatabase.clearAllTables()).
            observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onComplete() {
                    signOut.setValue("Success");
                }

                @Override
                public void onError(Throwable e) {
                    signOut.setValue("Error");
                }
            });
        return signOut;

    }
    public void AddFoodtoDatabase(String foodselection, String foodId, String foodName, List<Float> nutritiens) {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Log.d("timestamp", timestamp.toString());

        String stringdate = dt.format(newDate);
        SelectedFoodretrofit selectedFoodretrofit = new SelectedFoodretrofit(
                foodId, foodName, foodselection, stringdate, nutritiens.get(0)
                ,nutritiens.get(1),nutritiens.get(2),nutritiens.get(3)
        );
        apiResponseLiveData = RetrofitFactoryNew.create().addSelectedFood(
                foodId,foodName,
                sharedPreferences.getInt("userId",-1), timestamp, nutritiens.get(0)
                ,nutritiens.get(1),nutritiens.get(2),nutritiens.get(3),
                foodselection,0

        );

        resultMediatorLiveData.removeSource(apiResponseLiveData);
        resultMediatorLiveData.addSource(apiResponseLiveData, result->
        {
            resultMediatorLiveData.removeSource(apiResponseLiveData);
            if (result != null && result.isSuccessful()) {
                resultMediatorLiveData.removeSource(apiResponseLiveData);
                Completable.fromAction(() ->
                        userDao.insertOneAddedFood(selectedFoodretrofit)).
                        observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        resultMediatorLiveData.postValue(result);
                        //in order to not remember state
                        resultMediatorLiveData.setValue(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });

            }
        });

    }



    public MutableLiveData<UserRetrofitGood> curentUser = new MutableLiveData<>();

    public void getCurrentUser()
    {
//        if(curentUser.getValue() ==null)
//        {
//            Completable.fromAction(() ->
//                    userDao.getCurrentUser()).
//                    observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//                    });
//        }
    }




    @Inject
    RecipiesRepository(AppExecutors appExecutors, APIService apiService, MainDatabase mainDatabase) {
        userDao = mainDatabase.recipeDao();
        this.appExecutors = appExecutors;
        this.mainDatabase = mainDatabase;
        this.apiService = apiService;
         signOut= new MutableLiveData<>();

    }
    public LiveData<Resource<List<SelectedFoodretrofit>>> getAddedFoods(String whichTime, String year,
                                                                                      String month, String day) {
        return new NetworkBoundResource<List<SelectedFoodretrofit>,SelectedFoodretrofitarray>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull SelectedFoodretrofitarray item) {
                for(int i =0; i<item.getUsers().size(); i++)
                {
                    item.getUsers().get(i).setWhichTime(whichTime);
                }
                userDao.insertAllAddedFoodLists(item.getUsers());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SelectedFoodretrofit> data) {
                return data == null ||
                        data.size()==0;
            }

            @NonNull @Override
            protected LiveData<List<SelectedFoodretrofit>> loadFromDb() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(year+"-");
                stringBuilder.append(month+"-");
                stringBuilder.append(day+"%");
                String query = stringBuilder.toString();
                Log.d("sharedSend", query);
                return userDao.getAddedFoodsNew(whichTime, query);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<SelectedFoodretrofitarray>> createCall() {
                int userId = sharedPreferences.getInt("userId", -1);
                Log.d("sharedSend","sharedReceived" + userId);
                return RetrofitFactoryNew.create().getSelectedFoods(userId,whichTime,
                        year, month, day);
            }


        }.getAsLiveData();
    }



    public LiveData<Resource<List<OneSharedFoodProductsListRetrofit>>> loadSharedFoodLists(String whichDatabase) {
        return new  NetworkBoundResource<List<OneSharedFoodProductsListRetrofit>,SharedFoodProductsRetrofit >(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull SharedFoodProductsRetrofit item) {
                for(int i =0; i<item.getSelectedFoodretrofits().size(); i++)
                {
                    item.getSelectedFoodretrofits().get(i).setWhichDatabase(whichDatabase);
                }
                userDao.insertAllOneSharedFoodProductsListRetrofit(item.getSelectedFoodretrofits());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<OneSharedFoodProductsListRetrofit> data) {
                return data == null ||
                        data.size()==0;
            }

            @NonNull @Override
            protected LiveData<List<OneSharedFoodProductsListRetrofit>> loadFromDb() {
                return userDao.getRecipes(whichDatabase);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<SharedFoodProductsRetrofit>> createCall() {

                return RetrofitFactoryNew.create().getAllSharedDiets(sharedPreferences.getInt("userId", -1),whichDatabase );

            }


        }.getAsLiveData();


    }
}

