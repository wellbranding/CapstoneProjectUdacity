package udacityteam.healthapp.activities;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;

/**
 * View model for each item in the repositories RecyclerView
 */
public class SharedFoodListItemViewModel extends BaseObservable implements ViewModel {

    private OneSharedFoodProductsListRetrofit selectedFoodretrofit;
    private Context context;
    private  String foodselection;
    private String foodId;
    private String foodName;
    private String SendDate;
    private float Calories;
    private float Protein;
    private float Fat;
    private float Carbohydrates;

    public SharedFoodListItemViewModel(Context context, OneSharedFoodProductsListRetrofit repository
    , String foodselection) {
        this.selectedFoodretrofit = repository;
        this.context = context;
        this.foodselection = foodselection;
    }

    public String getName() {
        return selectedFoodretrofit.getDisplayname();
    }
    public void setSelectectedFoood(OneSharedFoodProductsListRetrofit selectedFoodretrofit) {
        this.selectedFoodretrofit = selectedFoodretrofit;
        notifyChange();
    }
    public void onItemClick(View view) {
        context.startActivity(FoodListPrieviewNew.newIntent(context, selectedFoodretrofit, foodselection));
    }

    @Override
    public void destroy() {
        //In this case destroy doesn't need to do anything because there is not async calls
    }

}
