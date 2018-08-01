package udacityteam.healthapp.completeRedesign.UI.Community.Views;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.UI.Community.ViewModels.FoodNutritiensDisplayViewModel;
import udacityteam.healthapp.databinding.FoodActivityBinding;


public class FoodNutritiensDisplayPrieview extends AppCompatActivity {
    private static final String SELECTED_FOOD_PRIEVIEW = "EXTRA_REPOSITORY";
    String id = null;
    TextView productName, carbos, protein, fats, calories;
    private FoodActivityBinding binding;
    private FoodNutritiensDisplayViewModel foodNutritiensDisplayViewModel;
    public static Intent newIntent(Context context, SelectedFoodretrofit selectedFoodretrofit) {
        Intent intent = new Intent(context, FoodNutritiensDisplayPrieview.class);
        intent.putExtra(SELECTED_FOOD_PRIEVIEW, selectedFoodretrofit);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.food_activity);
        productName = findViewById(R.id.ProductName);
        carbos = findViewById(R.id.carbos2);
        protein = findViewById(R.id.protein);
        fats = findViewById(R.id.fats);
        calories = findViewById(R.id.calories);
        SelectedFoodretrofit repository = getIntent().getParcelableExtra(SELECTED_FOOD_PRIEVIEW);
        productName.setText(repository.getFoodName());
        calories.setText("Calories:" + repository.getCalories());
        fats.setText("Fats:" + repository.getFat());
        carbos.setText("Carbohydrates:" + repository.getCarbohydrates());
        protein.setText("Protein:" + repository.getProtein());
        foodNutritiensDisplayViewModel = new FoodNutritiensDisplayViewModel(this, repository);

        binding.setViewModel(foodNutritiensDisplayViewModel);

        }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        foodNutritiensDisplayViewModel.destroy();
    }

    }

