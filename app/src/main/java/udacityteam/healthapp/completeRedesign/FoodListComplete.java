package udacityteam.healthapp.completeRedesign;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.CommunityActivities.CommunityList;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.Adapters.FoodListRetrofitAdapterNew;
import udacityteam.healthapp.completeRedesign.Repository.Status;
import udacityteam.healthapp.completeRedesign.Widget.BakingAppWidget;
import udacityteam.healthapp.databinding.ActivityFoodListBinding;
import udacityteam.healthapp.models.SelectedFood;

public class FoodListComplete extends AppCompatActivity implements   NavigationView.OnNavigationItemSelectedListener  {

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
    CollectionReference userstorage;
   FirebaseFirestore storage;
    String newstring=null;
    TextView caloriescounter, proteincounter, fatcounter, carbohycounter;
    ArrayList<SelectedFood> selectedFoods;
    private FoodListViewModelComplete foodListViewModel;
    private ActivityFoodListBinding activityFoodListBinding;
    List<SelectedFoodretrofit> receivedSelectedFoods;
    FoodListRetrofitAdapterNew customAdapterFoodListPrievew;



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
        foodselection = (String) b.get("foodselection");
        requestedString = (String) b.get("requestdate");
        SharedFoodListDatabase = (String) b.get("SharedFoodListDatabase");
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
        getSupportActionBar().setTitle("MainActivity");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle(foodselection);
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        share.setVisibility(View.INVISIBLE);


        foodListViewModel.getShareResult().observe(this, response->
        {
           if(response!=null) {

               Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
               Log.d("response", response.getMessage());
           }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String todays = dt.format(new Date(new Date().getTime()));
                if(todays.equals(stringdate) && receivedSelectedFoods.size()>=1)
                foodListViewModel.ShareFoodList(foodselection, SharedFoodListDatabase);
                else if(!todays.equals(stringdate))
                    Toast.makeText(FoodListComplete.this, "Can only share Today's diet", Toast.LENGTH_SHORT).show();
                else if(receivedSelectedFoods.size()>=1)
                {
                    foodListViewModel.ShareFoodList(foodselection, SharedFoodListDatabase);
                }
                else if(receivedSelectedFoods.size()==0)
                {
                    Toast.makeText(FoodListComplete.this, "Can't share empty Diet", Toast.LENGTH_SHORT).show();
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
        Log.d("reqss", stringdate);

        selectedFoods = new ArrayList<>();

        String year = requestedString.substring(0, 4);
        String month = requestedString.substring(5, 7);
        String day = requestedString.substring(8, 10);
        setupRecyclerView();
        foodListViewModel.setDataForRequestMutableLiveData(foodselection, year, month, day);
        foodListViewModel.getFoodLists().observe(this, response->
        {
            if(response!=null) {
                if (response.status == Status.SUCCESS) {

                    receivedSelectedFoods = response.data;
                    customAdapterFoodListPrievew.setSelectedFoodretrofits(response.data);
                    customAdapterFoodListPrievew.notifyDataSetChanged();
                    share.setEnabled(true);
                    share.setVisibility(View.VISIBLE);

                }
            }
            else
            {
                Toast.makeText(this, "Server issue", Toast.LENGTH_SHORT).show();
            }
        });
        foodListViewModel.getNutritionalValue().observe(this, response->
        {
            if(response!=null)
            {
                if(response.size()==4)
                {
                    caloriescounter.setText("Calories: " + response.get(0));
                    proteincounter.setText("Protein: " + response.get(1));
                    carbohycounter.setText("Carbos:" + response.get(2));
                    fatcounter.setText("Fats:" + response.get(3));
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.to_widget) {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            String todays = dt.format(new Date(new Date().getTime()));
            if(foodselection!=null)
            addToWidget(todays, foodselection);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void addToWidget(String query, String whichTime )
    {
        query = query+"%";
        sharedPreferences.edit().putString("query_widget", query).apply();
        sharedPreferences.edit().putString("whichtime_widget", whichTime).apply();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(FoodListComplete.this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(FoodListComplete.this, BakingAppWidget.class));


        BakingAppWidget bakingAppWidget = new BakingAppWidget();
        bakingAppWidget.updateAppWidget(FoodListComplete.this, appWidgetManager, appWidgetIds);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_list_widget);
        Toast.makeText(FoodListComplete.this, "Track in your widget", Toast.LENGTH_SHORT).show();
    }






    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    ///
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_breakfasts) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Breakfasts");
            intent.putExtra("SharedFoodListDatabase", "SharedBreakfasts");
            intent.putExtra("foodselection", "Breakfast");
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_dinners) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Dinners");
            intent.putExtra("SharedFoodListDatabase", "SharedDinners");
            intent.putExtra("foodselection", "Dinner");
            startActivity(intent);


        } else if (id == R.id.nav_lunches) {
            Intent
                    intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Lunches");
            intent.putExtra("SharedFoodListDatabase", "SharedLunches");
            intent.putExtra("foodselection", "Lunch");
            startActivity(intent);

        } else if (id == R.id.nav_community_daily_diets) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Community Daily Diet Plan");
            intent.putExtra("SharedFoodListDatabase", "SharedDailyDiets");
            Toast.makeText(this, "Currently Not Available", Toast.LENGTH_SHORT).show();
            //startActivity(intent);

        } else if (id == R.id.nav_snacks) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Snacks");
            intent.putExtra("SharedFoodListDatabase", "SharedSnacks");
            intent.putExtra("foodselection", "Snacks");
            startActivity(intent);
            //test

        } else if (id == R.id.nav_drinks_cocktails) {
            Intent intent = new Intent(this, CommunityList.class);
            Bundle extras = intent.getExtras();
            intent.putExtra("titlename", "Drinks/Coctails");
            intent.putExtra("SharedFoodListDatabase", "SharedDrinks");
            intent.putExtra("foodselection", "Drinks");
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setupRecyclerView() {

      customAdapterFoodListPrievew= new
                FoodListRetrofitAdapterNew();
        activityFoodListBinding.recyclerFood.setLayoutManager(new LinearLayoutManager(this));
        activityFoodListBinding.recyclerFood.setHasFixedSize(true);
        activityFoodListBinding.recyclerFood.setAdapter(customAdapterFoodListPrievew);
      //  Log.d("tikrinu", String.valueOf(foodListViewModel.selectedFoodretrofits.size()));

    }

}

