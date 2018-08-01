package udacityteam.healthapp.activities;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import rx.android.schedulers.AndroidSchedulers;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofitarray;
import udacityteam.healthapp.Network.PHPService;
import udacityteam.healthapp.app.ApplicationController;
import udacityteam.healthapp.completeRedesign.Utils.ViewModel;

/**
 * View model for the MainActivity
 */
public class FoodListPrieviewNewViewModel implements ViewModel {

    private static final String TAG = "MainViewModel";

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

    public void LoadFoodList(Integer SharedListId,  String foodselection)
    {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        ApplicationController application = ApplicationController.get(context);
        PHPService phpService = application.getPHPService();
        subscription = phpService.getselectedfoodsPrieview(SharedListId,
                foodselection)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(this::handleResponse,this::handleError);
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
