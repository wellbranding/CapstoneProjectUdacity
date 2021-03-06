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
import udacityteam.healthapp.completeRedesign.Data.Networking.API.RetrofitFactoryNew;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.Result;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.UserRetrofitGood;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.Userretrofit;
import udacityteam.healthapp.completeRedesign.Repository.MainRepository;
import udacityteam.healthapp.completeRedesign.db.MainDao;
import udacityteam.healthapp.completeRedesign.db.MainDatabase;

public class LoginRegisterViewModel extends ViewModel {

    private static final String TAG ="sharedSend" ;
    MainRepository repository;
    private MainDao mainDao;
    private MainDatabase mainDatabase;

    public static final String SHARED_PREF_USER_ID = "userId";
    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    public LoginRegisterViewModel(MainRepository mainRepository) {
        this.repository = mainRepository;
        this.mainDao = mainRepository.getMainDao();
        this.mainDatabase = mainRepository.getMainDatabase();
    }

    MutableLiveData<Result> googleSigInRegister;

    public LiveData<Result> getRegisterWithGoogleSignInResponse(Userretrofit userretrofit) {
        if (userretrofit != null)
            registerWithGoogleSignIn(userretrofit);
        return googleSigInRegister;

    }

    public void registerWithGoogleSignIn(Userretrofit retrofituser) {
        googleSigInRegister = new MutableLiveData<>();

        udacityteam.healthapp.completeRedesign.Data.Networking.API.APIService apiService
                = RetrofitFactoryNew.create();
        Call<Result> call = apiService.createUser(
                retrofituser.getName(),
                retrofituser.getEmail(),
                retrofituser.getUid()

        );
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                Result result = response.body();
                if (result != null) {
                    if (!result.getError()) {
                        googleSigInRegister.setValue(result);
                        UserRetrofitGood userRetrofitGood = result.getUser();
                        sharedPreferences.edit().putInt(SHARED_PREF_USER_ID, userRetrofitGood.getId())
                                .commit();
                        Log.d(TAG, String.valueOf(sharedPreferences.getInt(SHARED_PREF_USER_ID, -1)));
                        Completable.fromAction(() ->
                                mainDao.insertCurrentUser(userRetrofitGood)).
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
                } else {
                    googleSigInRegister.setValue(new Result(false, "Server Issue"
                            , null));
                }

            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

            }
        });
    }

}
