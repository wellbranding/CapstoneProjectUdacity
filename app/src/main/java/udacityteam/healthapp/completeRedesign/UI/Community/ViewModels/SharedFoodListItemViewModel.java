package udacityteam.healthapp.completeRedesign.UI.Community.ViewModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import udacityteam.healthapp.completeRedesign.Data.Networking.Models.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.completeRedesign.UI.Community.Views.FoodListPrieviewNew;
import udacityteam.healthapp.completeRedesign.Utils.ViewModel;

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
    }

}
