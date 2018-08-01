package udacityteam.healthapp.activities;

import android.content.Context;
import android.databinding.ObservableField;


import rx.Subscription;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.completeRedesign.Utils.ViewModel;


/**
 * ViewModel for the RepositoryActivity
 */
public class FoodNutritiensDisplayViewModel implements ViewModel {

    private static final String TAG = "RepositoryViewModel";

    private SelectedFoodretrofit selectedFoodretrofit;
    private Context context;
    private Subscription subscription;

    public ObservableField<String> foodId;
    public ObservableField<String> nutritional;

    public FoodNutritiensDisplayViewModel(Context context, final SelectedFoodretrofit repository) {
        this.selectedFoodretrofit = repository;
        this.context = context;
        this.foodId = new ObservableField<>();
        this.nutritional = new ObservableField<>();
    }

    public String getFoodId() {
        return selectedFoodretrofit.getFoodid();
    }

    public float getNutritional() {
        return selectedFoodretrofit.getCalories();
    }



    @Override
    public void destroy() {
        this.context = null;
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }



}
