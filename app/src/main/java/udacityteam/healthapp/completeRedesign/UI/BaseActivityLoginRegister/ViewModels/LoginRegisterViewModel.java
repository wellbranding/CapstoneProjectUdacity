package udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.Result;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.UserRetrofitGood;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.Userretrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.RetrofitFactoryNew;
import udacityteam.healthapp.completeRedesign.Repository.RecipiesRepository;
import udacityteam.healthapp.completeRedesign.db.MainDatabase;
import udacityteam.healthapp.completeRedesign.db.RecipesMainDao;

public class LoginRegisterViewModel extends ViewModel {

    RecipiesRepository repository;
    private FirebaseAuth mAuth;
    private RecipesMainDao recipesMainDao;
    private MainDatabase mainDatabase;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    public LoginRegisterViewModel(RecipiesRepository recipiesRepository) {
        this.repository = recipiesRepository;
        this.recipesMainDao = recipiesRepository.getUserDao();
        this.mainDatabase = recipiesRepository.getMainDatabase();
    }
    MutableLiveData<Result> googleSigInRegister;
    public LiveData<Result> getRegisterWithGoogleSignInResponse(Userretrofit userretrofit)
    {
        if(userretrofit!=null)
            registerWithGoogleSignIn(userretrofit);
        return googleSigInRegister;

    }

    public void registerWithGoogleSignIn(Userretrofit retrofituser)
    {
        googleSigInRegister = new MutableLiveData<>();

        //Defiin retrofit api service
        udacityteam.healthapp.completeRedesign.Data.Networking.API.APIService apiService
                = RetrofitFactoryNew.create();

        //defining the call
        Call<Result> call = apiService.createUser(
                retrofituser.getName(),
                retrofituser.getEmail(),
                retrofituser.getUid()

        );
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                Result result = response.body();
                if(result!=null) {
                    if (!result.getError()) {
                        googleSigInRegister.setValue(result);
                        UserRetrofitGood userRetrofitGood= result.getUser();
                        sharedPreferences.edit().putInt("userId", userRetrofitGood.getId())
                                .commit();
                        Log.d("sharedSend", String.valueOf(sharedPreferences.getInt("userId", -1)));
                        Completable.fromAction(() ->
                                recipesMainDao.insertCurrentUser(userRetrofitGood)).
                                observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onComplete() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }
                        });

                    } else {
                        googleSigInRegister.setValue(result);
                    }
                }
                else
                {
                    googleSigInRegister.setValue(new Result(false, "Server Issue"
                    , null));
                }


            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {


            }
        });
    }

    //public void

}
