package udacityteam.healthapp.completeRedesign.UI.Community.ViewModels;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import udacityteam.healthapp.completeRedesign.ApplicationController;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.APIService;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.RetrofitFactoryNew;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofitarray;
import udacityteam.healthapp.completeRedesign.Utils.ViewModel;


public class FoodListPrieviewNewViewModel implements ViewModel {


    public ObservableInt infoMessageVisibility;
    public ObservableInt recyclerViewVisibility;
    public ObservableInt searchButtonVisibility;
    public ObservableField<String> infoMessage;
    private DataListener dataListener;
    private Context context;
    private Subscription subscription;
    List<SelectedFoodretrofit> selectedFoodretrofits;

    public FoodListPrieviewNewViewModel(Context context, DataListener dataListener) {
        this.context = context;
        this.dataListener = dataListener;
        infoMessageVisibility = new ObservableInt(View.VISIBLE);
        recyclerViewVisibility = new ObservableInt(View.VISIBLE);
        searchButtonVisibility = new ObservableInt(View.GONE);
        infoMessage = new ObservableField<>("message");
    }

    public void LoadFoodList(Integer SharedListId, String foodselection) {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        APIService apiService = RetrofitFactoryNew.create();
        subscription = apiService.getselectedfoodsPrieview(SharedListId,
                foodselection)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError);
    }

    private void handleResponse(SelectedFoodretrofitarray androidList) {
        selectedFoodretrofits = new ArrayList<>(androidList.getUsers());
        dataListener.onRepositoriesChanged(selectedFoodretrofits);
    }

    private void handleError(Throwable error) {
    }


    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
        context = null;
    }


    public interface DataListener {
        void onRepositoriesChanged(List<SelectedFoodretrofit> repositories);
    }


}
