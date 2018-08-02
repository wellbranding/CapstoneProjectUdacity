package udacityteam.healthapp.completeRedesign.UI.AddedFoods.Views;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFood;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.completeRedesign.Repository.Status;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.Adapters.FoodListRetrofitAdapterNew;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.ViewModels.FoodListViewModelComplete;
import udacityteam.healthapp.completeRedesign.UI.Community.Views.CommunityList;
import udacityteam.healthapp.completeRedesign.Widget.AddedFoodsAppWidget;
import udacityteam.healthapp.databinding.ActivityFoodListBinding;

public class FoodListComplete extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String foodselection;
    String stringdate;
    TextView message;
    String requestedString;
    String SharedFoodListDatabase;
    Button share;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String newstring = null;
    TextView caloriescounter, proteincounter, fatcounter, carbohycounter;
    ArrayList<SelectedFood> selectedFoods;
    private FoodListViewModelComplete foodListViewModel;
    private ActivityFoodListBinding activityFoodListBinding;
    List<SelectedFoodretrofit> receivedSelectedFoods;
    FoodListRetrofitAdapterNew customAdapterFoodListPrievew;

    public static final String INTENT_WHICH_DATABASE = "SharedFoodListDatabase";
    public static final String INTENT_WHICH_TIME = "foodselection";
    public static final String INTENT_REQUEST_DATA = "requestdate";

    public static final String INTENT_TITLE_NAME = "titlename";


    @Inject
    ViewModelProvider.Factory ViewModelFactory;

    @Inject
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        foodListViewModel = ViewModelProviders.of(this, ViewModelFactory).
                get(FoodListViewModelComplete.class);
        receivedSelectedFoods = new ArrayList<>();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            foodselection = (String) b.get(getString(R.string.which_time_key));
            requestedString = (String) b.get(getString(R.string.request_date_key));
            SharedFoodListDatabase = (String) b.get(getString(R.string.shared_food_list_database_key));
        }
        activityFoodListBinding = DataBindingUtil.setContentView(this, R.layout.activity_food_list);
        caloriescounter = findViewById(R.id.caloriescount);
        proteincounter = findViewById(R.id.proteincount);
        carbohycounter = findViewById(R.id.carbohncount);
        fatcounter = findViewById(R.id.fatcount);

        message = findViewById(R.id.message);
        share = findViewById(R.id.share);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("MainActivity");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle(foodselection);
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        share.setVisibility(View.INVISIBLE);


        foodListViewModel.getShareResult().observe(this, response ->
        {
            if (response != null) {

                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String todays = dt.format(new Date(new Date().getTime()));
                if (todays.equals(stringdate) && receivedSelectedFoods.size() >= 1)
                    foodListViewModel.ShareFoodList(foodselection, SharedFoodListDatabase);
                else if (!todays.equals(stringdate))
                    Toast.makeText(FoodListComplete.this, R.string.cant_share_todays_diet_message, Toast.LENGTH_SHORT).show();
                else if (receivedSelectedFoods.size() >= 1) {
                    foodListViewModel.ShareFoodList(foodselection, SharedFoodListDatabase);
                } else if (receivedSelectedFoods.size() == 0) {
                    Toast.makeText(FoodListComplete.this, R.string.cant_share_empty_diet_message, Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (requestedString != null)
            stringdate = requestedString;
        else {
            Date date = new Date();
            Date newDate = new Date(date.getTime());
            stringdate = dt.format(newDate);
        }

        selectedFoods = new ArrayList<>();

        String year = requestedString.substring(0, 4);
        String month = requestedString.substring(5, 7);
        String day = requestedString.substring(8, 10);
        setupRecyclerView();
        foodListViewModel.setDataForRequestMutableLiveData(foodselection, year, month, day);
        foodListViewModel.getFoodLists().observe(this, response ->
        {
            if (response != null) {
                if (response.status == Status.SUCCESS) {

                    receivedSelectedFoods = response.data;
                    customAdapterFoodListPrievew.setSelectedFoodretrofits(response.data);
                    customAdapterFoodListPrievew.notifyDataSetChanged();
                    share.setEnabled(true);
                    share.setVisibility(View.VISIBLE);

                }
            } else {
                Toast.makeText(this, R.string.server_issue_error_toast, Toast.LENGTH_SHORT).show();
            }
        });
        foodListViewModel.getNutritionalValue().observe(this, response ->
        {
            if (response != null) {
                if (response.size() == 4) {
                    caloriescounter.setText(getString(R.string.calories_display_text) + response.get(0));
                    proteincounter.setText(getString(R.string.protein_display_text) + response.get(1));
                    carbohycounter.setText(getString(R.string.carbos_display_text) + response.get(2));
                    fatcounter.setText(getString(R.string.fats_display_text) + response.get(3));
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.to_widget) {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            String todays = dt.format(new Date(new Date().getTime()));
            if (foodselection != null)
                addToWidget(todays, foodselection);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void addToWidget(String query, String whichTime) {
        query = query + "%";
        sharedPreferences.edit().putString(getString(R.string.shared_prefs_query_string_key_widget), query).apply();
        sharedPreferences.edit().putString(getString(R.string.shared_prefs_which_time_key_widget), whichTime).apply();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(FoodListComplete.this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(FoodListComplete.this, AddedFoodsAppWidget.class));


        AddedFoodsAppWidget addedFoodsAppWidget = new AddedFoodsAppWidget();
        addedFoodsAppWidget.updateAppWidget(FoodListComplete.this, appWidgetManager, appWidgetIds);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_list_widget);
        Toast.makeText(FoodListComplete.this, R.string.track_in_your_widget_toast, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_breakfasts) {
            Intent intent = new Intent(this, CommunityList.class);
            intent.putExtra(INTENT_TITLE_NAME, getString(R.string.title_community_breakfasts));
            intent.putExtra(INTENT_WHICH_DATABASE, getString(R.string.shared_breakfasts));
            intent.putExtra(INTENT_WHICH_TIME, getString(R.string.which_time_breakfast));
            startActivity(intent);
        } else if (id == R.id.nav_dinners) {
            Intent intent = new Intent(this, CommunityList.class);
            intent.putExtra(INTENT_TITLE_NAME, getString(R.string.title_community_dinners));
            intent.putExtra(INTENT_WHICH_DATABASE, getString(R.string.shared_dinners));
            intent.putExtra(INTENT_WHICH_TIME, getString(R.string.which_time_dinner));
            startActivity(intent);


        } else if (id == R.id.nav_lunches) {
            Intent
                    intent = new Intent(this, CommunityList.class);
            intent.putExtra(INTENT_TITLE_NAME, getString(R.string.title_community_lunches));
            intent.putExtra(INTENT_WHICH_DATABASE, getString(R.string.shared_lunches));
            intent.putExtra(INTENT_WHICH_TIME, getString(R.string.shared_lunches));
            startActivity(intent);

        } else if (id == R.id.nav_community_daily_diets) {
            Intent intent = new Intent(this, CommunityList.class);
            intent.putExtra(INTENT_TITLE_NAME, "");
            intent.putExtra(INTENT_TITLE_NAME, "");
            Toast.makeText(this, R.string.currently_not_available_message, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_snacks) {
            Intent intent = new Intent(this, CommunityList.class);
            intent.putExtra(INTENT_TITLE_NAME, getString(R.string.title_community_snacks));
            intent.putExtra(INTENT_WHICH_DATABASE, getString(R.string.shared_snacks));
            intent.putExtra(INTENT_WHICH_TIME, getString(R.string.shared_snacks));
            startActivity(intent);

        } else if (id == R.id.nav_drinks_cocktails) {
            //  setCurrentTitle(getResources().getString(R.string.which_time_drinks));
            Intent intent = new Intent(this, CommunityList.class);
            intent.putExtra(INTENT_TITLE_NAME, getString(R.string.title_community_drinks));
            intent.putExtra(INTENT_WHICH_DATABASE, getString(R.string.shared_drinks));
            intent.putExtra(INTENT_WHICH_TIME, getString(R.string.which_time_drinks));
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setupRecyclerView() {

        customAdapterFoodListPrievew = new
                FoodListRetrofitAdapterNew();
        activityFoodListBinding.recyclerFood.setLayoutManager(new LinearLayoutManager(this));
        activityFoodListBinding.recyclerFood.setHasFixedSize(true);
        activityFoodListBinding.recyclerFood.setAdapter(customAdapterFoodListPrievew);
    }

}

