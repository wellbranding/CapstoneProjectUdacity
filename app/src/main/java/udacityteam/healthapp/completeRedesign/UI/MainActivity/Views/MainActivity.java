package udacityteam.healthapp.completeRedesign.UI.MainActivity.Views;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.Views.FoodListComplete;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.Views.FoodNutritiensDisplayFragment;
import udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.Views.BaseActivity;
import udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.Views.RegisterWithMailFragment;
import udacityteam.healthapp.completeRedesign.UI.Community.Views.CommunityList;
import udacityteam.healthapp.completeRedesign.UI.MainActivity.ViewModels.MainActivityViewModelGood;
import udacityteam.healthapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FoodSearchFragment.FoodListRecyclerViewListener,
        HasSupportFragmentInjector
{


    private String calendarCurrentTime;
    FloatingActionButton fabsettings;

    DrawerLayout mDrawer;

    private boolean fabExpanded = false;
    private ConstraintLayout Snacks;
    private ConstraintLayout Drinks;
    private ConstraintLayout Breakfast;
    private ConstraintLayout Dinner;
    private ConstraintLayout Lunch;
    private Button dinnerbtn;
    private Button lunchbtn;
    private Button breakfastbtn;
    private Button snacksbtn;
    private Button dailybtn;
    private Button drinksbtn;
    Calendar today;
    private ActivityMainBinding binding;
    private MainActivityViewModelGood mainActivityViewModel;

    public static final String INTENT_WHICH_DATABASE = "SharedFoodListDatabase";
    public static final String INTENT_WHICH_TIME = "foodselection";
    public static final String INTENT_REQUEST_DATA = "requestdate";
    public static final String INTENT_TITLE_NAME = "titlename";

    private ActionBar mActionBar;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mToolBarNavigationListenerIsRegistered;
    private NavigationView navigationView;
    private boolean isOpenedFragment = false;
    private boolean isOpenedSecondFragmnet = false;
    Toolbar mToolbar;

    @Inject
    ViewModelProvider.Factory ViewModelFactory;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    private String mCurrentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mainActivityViewModel = ViewModelProviders.of(this, ViewModelFactory).
                get(MainActivityViewModelGood.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        fabsettings = findViewById(R.id.fabSetting);

        Breakfast = this.findViewById(R.id.Breakfast);
        Dinner = this.findViewById(R.id.Dinner);
        Lunch = this.findViewById(R.id.Lunch);
        Snacks = this.findViewById(R.id.Snacks);
        Drinks =  this.findViewById(R.id.Drinks);
        fabsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded) {
                    closeSubMenusFab();
                } else {
                    Date date = new Date();
                    Date newDate = new Date(date.getTime());
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                    String stringdate = dt.format(newDate);
                    if (stringdate.equals(calendarCurrentTime))
                        openSubMenusFab();
                    else
                        Toast.makeText(MainActivity.this, R.string.can_only_add_food_today_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
        closeSubMenusFab();
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        calendarinit();
        initButton();

        if (savedInstanceState != null) {
            resolveUpButtonWithFragmentStack();
        }


    }

    private void resolveUpButtonWithFragmentStack() {
        showUpButton(getSupportFragmentManager().getBackStackEntryCount() > 0);
    }

    private void initButton() {
        dinnerbtn = this.findViewById(R.id.btndinner);

        lunchbtn = this.findViewById(R.id.btnlunch);
        breakfastbtn = this.findViewById(R.id.btnbreakfast);
        snacksbtn = this.findViewById(R.id.btnscancks);
        drinksbtn = this.findViewById(R.id.btndrinks);
        dailybtn = this.findViewById(R.id.btndaily);
        binding.appBarMain.contentgood.calendarView.calendarView.getSelectedDate();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(binding.appBarMain.contentgood.calendarView.calendarView.getSelectedDate().getDate());
        lunchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodListComplete.class);
                intent.putExtra(INTENT_WHICH_TIME, getResources().getString(R.string.which_time_lunch));
                intent.putExtra(INTENT_WHICH_DATABASE, getResources().getString(R.string.shared_lunches));
                intent.putExtra(INTENT_REQUEST_DATA, calendarCurrentTime);
                startActivity(intent);
            }
        });
        breakfastbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodListComplete.class);
                intent.putExtra(INTENT_WHICH_TIME, getResources().getString(R.string.which_time_breakfast));
                intent.putExtra(INTENT_WHICH_DATABASE, getResources().getString(R.string.shared_breakfasts));
                intent.putExtra(INTENT_REQUEST_DATA, calendarCurrentTime);
                startActivity(intent);
            }
        });
        snacksbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodListComplete.class);
                intent.putExtra(INTENT_WHICH_TIME, getResources().getString(R.string.which_time_snacks));
                intent.putExtra(INTENT_WHICH_DATABASE, getResources().getString(R.string.shared_snacks));
                intent.putExtra(INTENT_REQUEST_DATA, calendarCurrentTime);
                startActivity(intent);
            }
        });
        drinksbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodListComplete.class);
                intent.putExtra(INTENT_WHICH_TIME, getResources().getString(R.string.which_time_drinks));
                intent.putExtra(INTENT_WHICH_DATABASE, "SharedDrinks");
                intent.putExtra(INTENT_REQUEST_DATA, calendarCurrentTime);
                startActivity(intent);
            }
        });
        dinnerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodListComplete.class);
                intent.putExtra(INTENT_WHICH_TIME, "Dinner");
                intent.putExtra(INTENT_WHICH_DATABASE, "SharedDinners");
                intent.putExtra(INTENT_REQUEST_DATA, calendarCurrentTime);
                startActivity(intent);
            }
        });
        dailybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, R.string.currently_not_available_toast, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void calendarinit() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

        binding.appBarMain.contentgood.calendarView.calendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
        binding.appBarMain.contentgood.calendarView.calendarView.setShowOtherDates(MaterialCalendarView.SHOW_OTHER_MONTHS);


        Calendar beginning = Calendar.getInstance();
        beginning.set(beginning.get(Calendar.YEAR), beginning.get(Calendar.MONTH), beginning.get(Calendar.DAY_OF_MONTH) - 10);
        today = Calendar.getInstance();
        today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        calendarCurrentTime = dt.format(Calendar.getInstance().getTime());
        binding.appBarMain.contentgood.calendarView.calendarView.setCurrentDate(today);
        Calendar end = Calendar.getInstance();
        end.set(end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH) + 10);
        binding.appBarMain.contentgood.calendarView.calendarView.setArrowColor(this.getResources().getColor(R.color.colorPrimary));
        binding.appBarMain.contentgood.calendarView.calendarView.setSelectionColor(this.getResources().getColor(R.color.colorPrimary));
        binding.appBarMain.contentgood.calendarView.calendarView.setSelectedDate(today);
        Calendar minimum = Calendar.getInstance();
        minimum.set(minimum.get(Calendar.YEAR), minimum.get(Calendar.MONTH), minimum.get(Calendar.DAY_OF_MONTH) - 50);
        Calendar maximum = Calendar.getInstance();
        maximum.set(maximum.get(Calendar.YEAR), maximum.get(Calendar.MONTH), maximum.get(Calendar.DAY_OF_MONTH) + 50);
        binding.appBarMain.contentgood.calendarView.calendarView.state().edit()
                .setMinimumDate(minimum.getTime())
                .setMaximumDate(maximum.getTime())
                .commit();
        binding.appBarMain.contentgood.calendarView.calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                calendarCurrentTime = dt.format(date.getDate().getTime());
            }
        });


    }

    private void closeSubMenusFab() {
        Snacks.setVisibility(View.INVISIBLE);
        Drinks.setVisibility(View.INVISIBLE);
        Breakfast.setVisibility(View.INVISIBLE);
        Dinner.setVisibility(View.INVISIBLE);
        Lunch.setVisibility(View.INVISIBLE);
        fabsettings.setImageResource(R.drawable.ic_add_circle_black_24dp);
        fabExpanded = false;
    }


    private void openSubMenusFab() {
        Snacks.setVisibility(View.VISIBLE);
        Drinks.setVisibility(View.VISIBLE);
        Breakfast.setVisibility(View.VISIBLE);
        Dinner.setVisibility(View.VISIBLE);
        Lunch.setVisibility(View.VISIBLE);
        RegisterWithMailFragment fragment = new RegisterWithMailFragment();
        android.support.v4.app.FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        Drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_WHICH_DATABASE, getString(R.string.shared_drinks));
                bundle.putString(INTENT_WHICH_TIME, getString(R.string.which_time_drinks));
                FoodSearchFragment fragment = new FoodSearchFragment();
                android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                fragmentManager.executePendingTransactions();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        add(R.id.app_bar_main, fragment, getString(R.string.fragment_tag_main_activity_search_display))
                        .addToBackStack(null)
                        .commit();
                showUpButton(true);
                isOpenedFragment = true;

            }
        });
        Snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_WHICH_DATABASE, getString(R.string.shared_snacks));
                bundle.putString(INTENT_WHICH_TIME, getString(R.string.which_time_snacks));
                FoodSearchFragment fragment = new FoodSearchFragment();
                android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                fragmentManager.executePendingTransactions();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        add(R.id.app_bar_main, fragment, getString(R.string.fragment_tag_main_activity_search_display))
                        .addToBackStack(null)
                        .commit();
                showUpButton(true);
                isOpenedFragment = true;
            }
        });
        Breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_WHICH_DATABASE, getString(R.string.shared_breakfasts));
                bundle.putString(INTENT_WHICH_TIME, getString(R.string.which_time_breakfast));
                FoodSearchFragment fragment = new FoodSearchFragment();
                android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                fragmentManager.executePendingTransactions();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        add(R.id.app_bar_main, fragment, getString(R.string.fragment_tag_main_activity_search_display))
                        .addToBackStack(null)
                        .commit();
                showUpButton(true);
                isOpenedFragment = true;
            }
        });
        Dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_WHICH_DATABASE, getString(R.string.shared_dinners));
                bundle.putString(INTENT_WHICH_TIME, getString(R.string.which_time_dinner));
                FoodSearchFragment fragment = new FoodSearchFragment();
                android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                fragmentManager.executePendingTransactions();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        add(R.id.app_bar_main, fragment, getString(R.string.fragment_tag_main_activity_search_display))
                        .addToBackStack(null)
                        .commit();
                showUpButton(true);
                isOpenedFragment = true;
            }
        });
        Lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_WHICH_DATABASE, getString(R.string.shared_lunches));
                bundle.putString(INTENT_WHICH_TIME, getString(R.string.which_time_lunch));
                FoodSearchFragment fragment = new FoodSearchFragment();
                android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                fragmentManager.executePendingTransactions();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        add(R.id.app_bar_main, fragment, getString(R.string.fragment_tag_main_activity_search_display))
                        .addToBackStack(null)
                        .commit();
                showUpButton(true);
                isOpenedFragment = true;
            }
        });
        fabsettings.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }


    private void showUpButton(boolean show) {
        if (show) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            if (!mToolBarNavigationListenerIsRegistered) {
                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {

            mActionBar.setDisplayHomeAsUpEnabled(false);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);

        } else {
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackCount >= 1) {
                getSupportFragmentManager().popBackStack();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_tag_main_activity_search_display));
                if (fragment instanceof FoodNutritiensDisplayFragment) {
                    mToolbar.setTitle(mCurrentTitle);
                }
                if (fragment instanceof FoodSearchFragment) {
                    mToolbar.setTitle(getResources().getString(R.string.app_name));
                }
                if (backStackCount == 1) {
                    if (fragment instanceof FoodNutritiensDisplayFragment) {
                        mToolbar.setTitle(mCurrentTitle);
                    }
                    if (fragment instanceof FoodSearchFragment) {
                        mToolbar.setTitle(getResources().getString(R.string.app_name));
                    }
                    isOpenedFragment = false;
                    showUpButton(false);
                }
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                ReturntoRegister();
                return true
                        ;
            case android.R.id.home:

                int backStackCount = getSupportFragmentManager().getBackStackEntryCount();

                if (backStackCount >= 1) {
                    mDrawerToggle.setDrawerIndicatorEnabled(false);
                    mActionBar.setDisplayHomeAsUpEnabled(true);
                    onBackPressed();
                    return true;
                } else {
                    mDrawerToggle.setDrawerIndicatorEnabled(true);
                    mActionBar.setDisplayHomeAsUpEnabled(false);
                    mDrawer.openDrawer(GravityCompat.START);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ReturntoRegister() {

        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mainActivityViewModel.signOut().observe(MainActivity.this, response ->
                            {
                                if (response != null) {
                                    if (response.equals("Success")) {
                                        Toast.makeText(MainActivity.this, R.string.successfully_sign_out, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, BaseActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else
                                        Toast.makeText(MainActivity.this, R.string.try_again_toast, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else
                            Toast.makeText(MainActivity.this, R.string.try_again_toast, Toast.LENGTH_SHORT).show();
                    }
                });
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


    private void setCurrentTitle(String title)

    {
        mCurrentTitle = title;
    }

    private String determineWhichType(String whichTime) {
        if (whichTime.equals(getResources().getString(R.string.which_time_drinks))) {

            return getResources().getString(R.string.shared_drinks);
        } else if (whichTime.equals(getResources().getString(R.string.which_time_snacks))) {
            return getResources().getString(R.string.shared_snacks);
        } else if (whichTime.equals(getResources().getString(R.string.which_time_lunch))) {
            return getResources().getString(R.string.shared_lunches);
        } else if (whichTime.equals(getResources().getString(R.string.which_time_breakfast))) {
            return getResources().getString(R.string.shared_breakfasts);
        } else return getResources().getString(R.string.shared_dinners);

    }

    @Override
    public void onFoodListSelected(String id, String foodName, String whichTime) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.food_id_bundle_key), id);
        bundle.putString(getString(R.string.food_name_bundle_key), foodName);
        bundle.putString(INTENT_WHICH_TIME, whichTime);
        bundle.putString(INTENT_WHICH_DATABASE, determineWhichType(whichTime));
        FoodNutritiensDisplayFragment fragment = new FoodNutritiensDisplayFragment();
        android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().
                add(R.id.app_bar_main, fragment, getString(R.string.fragment_tag_main_activity_search_display))
                .addToBackStack(null)
                .commit();
        showUpButton(true);
        isOpenedSecondFragmnet = true;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
