package udacityteam.healthapp.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.adapters.FoodListRetrofitAdapterNew;
import udacityteam.healthapp.databases.DatabaseHelper;
import udacityteam.healthapp.databinding.ActivityFoodListPreviewBinding;
import udacityteam.healthapp.databinding.FoodActivityBinding;

/**
 * Created by vvost on 11/26/2017.
 */

public class FoodListPrieviewNew extends AppCompatActivity implements FoodListPrieviewNewViewModel.DataListener {
    private static final String SELECTED_FOOD_PRIEVIEW = "EXTRA_REPOSITORY";
    private static final String FOOD_SELECTION = "FOOD_SELECTION";
    String id = null;
    OneSharedFoodProductsListRetrofit oneSharedFoodProductsListRetrofit;
    String foodselection;
    TextView carboCount, fatCount, caloriesCount, proteinCount;

    private ActivityFoodListPreviewBinding binding;
    private FoodListPrieviewNewViewModel foodListPrieviewNewViewModel;
//    private FoodNutritiensDisplayViewModel foodNutritiensDisplayViewModel;
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
        proteinCount.setText("Protein " + oneSharedFoodProductsListRetrofit.getProtein());
        carboCount.setText("Carbos " + oneSharedFoodProductsListRetrofit.getCarbohydrates());
       fatCount.setText("Fats " + oneSharedFoodProductsListRetrofit.getFat());
        caloriesCount.setText("Calories " + oneSharedFoodProductsListRetrofit.getCalories());
      foodselection = getIntent().getExtras().getString(FOOD_SELECTION);
      foodListPrieviewNewViewModel.LoadFoodList(oneSharedFoodProductsListRetrofit.getParentSharedFoodsId(), foodselection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRepositoriesChanged(List<SelectedFoodretrofit> repositories) {
        FoodListRetrofitAdapterNew customAdapterFoodListPrievew= new FoodListRetrofitAdapterNew(repositories);
        customAdapterFoodListPrievew.setSelectedFoodretrofits(repositories);
        customAdapterFoodListPrievew.notifyDataSetChanged();
        binding.recyclerFood.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerFood.setHasFixedSize(true);
        binding.recyclerFood.setAdapter(customAdapterFoodListPrievew);
    }
}

