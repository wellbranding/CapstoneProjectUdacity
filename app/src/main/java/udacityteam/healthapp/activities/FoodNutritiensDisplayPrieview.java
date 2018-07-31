package udacityteam.healthapp.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.databases.DatabaseHelper;
import udacityteam.healthapp.databinding.FoodActivityBinding;

/**
 * Created by vvost on 11/26/2017.
 */

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

