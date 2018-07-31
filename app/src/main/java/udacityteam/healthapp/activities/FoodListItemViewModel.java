package udacityteam.healthapp.activities;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import udacityteam.healthapp.Model.SelectedFoodretrofit;
/**
 * View model for each item in the repositories RecyclerView
 */
public class FoodListItemViewModel extends BaseObservable implements ViewModel {

    private SelectedFoodretrofit selectedFoodretrofit;
    private Context context;
    private String foodId;
    private String foodName;
    private String UserId;
    private String SendDate;
    private float Calories;
    private float Protein;
    private float Fat;
    private float Carbohydrates;

    public FoodListItemViewModel(Context context, SelectedFoodretrofit repository) {
        this.selectedFoodretrofit = repository;
        this.context = context;
    }

    public String getName() {
        return selectedFoodretrofit.getFoodid();
    }
    public void setSelectectedFoood(SelectedFoodretrofit selectedFoodretrofit) {
        this.selectedFoodretrofit = selectedFoodretrofit;
    }
    public void onItemClick(View view) {
        context.startActivity(FoodNutritiensDisplayPrieview.newIntent(context, selectedFoodretrofit));
    }

    @Override
    public void destroy() {
    }

}
