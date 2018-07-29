package udacityteam.healthapp.completeRedesign.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;


import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofitarray;
import udacityteam.healthapp.Model.SharedFoodProductsRetrofit;
import udacityteam.healthapp.Model.UserRetrofitGood;
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



    public RecipesMainDao getUserDao() {
        return userDao;
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
        return new NetworkBoundResource<List<OneSharedFoodProductsListRetrofit>,SharedFoodProductsRetrofit >(appExecutors) {
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

