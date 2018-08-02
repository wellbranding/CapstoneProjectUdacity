package udacityteam.healthapp.completeRedesign.UI.Community.Views;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import java.util.List;

import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.Adapters.FoodListRetrofitAdapterNew;
import udacityteam.healthapp.completeRedesign.UI.Community.ViewModels.FoodListPrieviewNewViewModel;
import udacityteam.healthapp.databinding.ActivityFoodListPreviewBinding;

public class FoodListPrieviewNew extends AppCompatActivity implements FoodListPrieviewNewViewModel.DataListener {
    private static final String SELECTED_FOOD_PRIEVIEW = "EXTRA_REPOSITORY";
    private static final String FOOD_SELECTION = "FOOD_SELECTION";
    OneSharedFoodProductsListRetrofit oneSharedFoodProductsListRetrofit;
    String foodselection;
    TextView carboCount, fatCount, caloriesCount, proteinCount;
    private ActivityFoodListPreviewBinding binding;
    private FoodListPrieviewNewViewModel foodListPrieviewNewViewModel;

    public static Intent newIntent(Context context, OneSharedFoodProductsListRetrofit selectedFoodretrofit
            , String foodselection) {
        Intent intent = new Intent(context, FoodListPrieviewNew.class);
        intent.putExtra(SELECTED_FOOD_PRIEVIEW, selectedFoodretrofit);
        intent.putExtra(FOOD_SELECTION, foodselection);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_food_list_preview);
        foodListPrieviewNewViewModel = new FoodListPrieviewNewViewModel(this, this);
        binding.setViewModel(foodListPrieviewNewViewModel);
        proteinCount = findViewById(R.id.proteincount);
        caloriesCount = findViewById(R.id.caloriescount);
        fatCount = findViewById(R.id.fatcount);
        carboCount = findViewById(R.id.carbohncount);
        oneSharedFoodProductsListRetrofit = getIntent().getParcelableExtra(SELECTED_FOOD_PRIEVIEW);
        proteinCount.setText(String.format("%s%s", getString(R.string.protein_display_text), oneSharedFoodProductsListRetrofit.getProtein()));
        carboCount.setText(String.format("%s%s", getString(R.string.carbos_display_text), oneSharedFoodProductsListRetrofit.getCarbohydrates()));
        fatCount.setText(String.format("%s%s", getString(R.string.fats_display_text), oneSharedFoodProductsListRetrofit.getFat()));
        caloriesCount.setText(getString(R.string.calories_display_text) + oneSharedFoodProductsListRetrofit.getCalories());
        foodselection = getIntent().getExtras().getString(FOOD_SELECTION);
        foodListPrieviewNewViewModel.LoadFoodList(oneSharedFoodProductsListRetrofit.getParentSharedFoodsId(), foodselection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRepositoriesChanged(List<SelectedFoodretrofit> repositories) {
        FoodListRetrofitAdapterNew customAdapterFoodListPrievew = new FoodListRetrofitAdapterNew(repositories);
        customAdapterFoodListPrievew.setSelectedFoodretrofits(repositories);
        customAdapterFoodListPrievew.notifyDataSetChanged();
        binding.recyclerFood.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerFood.setHasFixedSize(true);
        binding.recyclerFood.setAdapter(customAdapterFoodListPrievew);
    }
}

