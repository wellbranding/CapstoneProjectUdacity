package udacityteam.healthapp.completeRedesign.UI.AddedFoods.ViewModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.completeRedesign.UI.Community.Views.FoodNutritiensDisplayPrieview;
import udacityteam.healthapp.completeRedesign.Utils.ViewModel;

public class FoodListItemViewModel extends BaseObservable implements ViewModel {

    private SelectedFoodretrofit selectedFoodretrofit;
    private Context context;

    public FoodListItemViewModel(Context context, SelectedFoodretrofit repository) {
        this.selectedFoodretrofit = repository;
        this.context = context;
    }
    public String getName() {
        return selectedFoodretrofit.getFoodName();
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
