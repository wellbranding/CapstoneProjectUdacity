package udacityteam.healthapp.completeRedesign.UI.MainActivity.Views;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.UserRetrofitGood;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.Views.BaseActivity;
import udacityteam.healthapp.completeRedesign.UI.Community.Views.CommunityList;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.Views.FoodListComplete;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.Views.FoodNutritiensDisplayFragment;
import udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.Views.RegisterWithMailFragment;
import udacityteam.healthapp.completeRedesign.UI.MainActivity.ViewModels.MainActivityViewModelGood;
import udacityteam.healthapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FoodSearchFragment.FoodListRecyclerViewListener,
        HasSupportFragmentInjector

{

    public static final String ANONYMOUS = "anonymous";

    public static final int RC_SIGN_IN = 1;

    private String calendarCurrentTime;
    FloatingActionButton fabsettings;

    DrawerLayout mDrawer;


    private ChildEventListener mChildEventListener;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private boolean fabExpanded = false;
    private LinearLayout Snacks;
    private LinearLayout Drinks;
    private LinearLayout Breakfast;
    private LinearLayout Dinner;
    private LinearLayout Lunch;
    private Button dinnerbtn;
    private Button lunchbtn;
    private Button breakfastbtn;
    private Button snacksbtn;
    private Button dailybtn;
    private Button drinksbtn;
    Calendar today;
    public static UserRetrofitGood currentUser;
    private ActivityMainBinding binding;
    private MainActivityViewModelGood mainActivityViewModel;

    public static final String INTENT_WHICH_DATABASE = "SharedFoodListDatabase";
    public static final String INTENT_WHICH_TIME = "foodselection";


    private String mUsername;
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
        mainActivityViewModel =  ViewModelProviders.of(this, ViewModelFactory).
                get(MainActivityViewModelGood.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        fabsettings = findViewById(R.id.fabSetting);

        Breakfast = (LinearLayout) this.findViewById(R.id.Breakfast);
        Dinner = (LinearLayout) this.findViewById(R.id.Dinner);
        Lunch = (LinearLayout) this.findViewById(R.id.Lunch);
        Snacks = (LinearLayout) this.findViewById(R.id.Snacks);
        Drinks = (LinearLayout) this.findViewById(R.id.Drinks);
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
                   if(stringdate.equals(calendarCurrentTime))
                    openSubMenusFab();
                   else
                       Toast.makeText(MainActivity.this, "Can add food only to today!", Toast.LENGTH_SHORT).show();
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

        if(savedInstanceState != null){
            resolveUpButtonWithFragmentStack();
        }




    }
    private void resolveUpButtonWithFragmentStack() {
        showUpButton(getSupportFragmentManager().getBackStackEntryCount() > 0);
    }

    private void initButton()
    {
       dinnerbtn= this.findViewById(R.id.btndinner);

       lunchbtn = this.findViewById(R.id.btnlunch);
       breakfastbtn = this.findViewById(R.id.btnbreakfast);
       snacksbtn = this.findViewById(R.id.btnscancks);
       drinksbtn = this.findViewById(R.id.btndrinks);
       dailybtn = this.findViewById(R.id.btndaily);
        binding.appBarMain.contentgood.calendarView.calendarView.getSelectedDate();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime( binding.appBarMain.contentgood.calendarView.calendarView.getSelectedDate().getDate());
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println();
        Log.d("ajaaz", format.format(calendar.getTime()));
        lunchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodListComplete.class);
                intent.putExtra("foodselection", "Lunch");
                intent.putExtra("SharedFoodListDatabase", "SharedLunches");
                intent.putExtra("requestdate", calendarCurrentTime);
                startActivity(intent);
            }
        });
        breakfastbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodListComplete.class);
                intent.putExtra("foodselection", "Breakfast");
                intent.putExtra("SharedFoodListDatabase", "SharedBreakfasts");
                intent.putExtra("requestdate", calendarCurrentTime);
                startActivity(intent);
            }
        });
        snacksbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodListComplete.class);
                intent.putExtra("foodselection", "Snacks");
                intent.putExtra("SharedFoodListDatabase", "SharedSnacks");
                intent.putExtra("requestdate", calendarCurrentTime);
                startActivity(intent);
            }
        });
        drinksbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodListComplete.class);
                intent.putExtra("foodselection", "Drinks");
                intent.putExtra("SharedFoodListDatabase", "SharedDrinks");
                intent.putExtra("requestdate", calendarCurrentTime);
                startActivity(intent);
            }
        });
       dinnerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodListComplete.class);
                intent.putExtra("foodselection", "Dinner");
                intent.putExtra("SharedFoodListDatabase", "SharedDinners");
                intent.putExtra("requestdate", calendarCurrentTime);
                startActivity(intent);
            }
        });
        dailybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Currently not Available", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void calendarinit()
    {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        ButterKnife.bind(this);

        binding.appBarMain.contentgood.calendarView.calendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
        binding.appBarMain.contentgood.calendarView.calendarView.setShowOtherDates(MaterialCalendarView.SHOW_OTHER_MONTHS);


        Calendar beginning = Calendar.getInstance();
        beginning.set(beginning.get(Calendar.YEAR), beginning.get(Calendar.MONTH), beginning.get(Calendar.DAY_OF_MONTH)-10);
        Log.d("aaa", beginning.toString());
       today = Calendar.getInstance();
       today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        calendarCurrentTime = dt.format(Calendar.getInstance().getTime());
        Log.d("current123",calendarCurrentTime);
        binding.appBarMain.contentgood.calendarView.calendarView.setCurrentDate(today);
        Calendar end = Calendar.getInstance();
        end.set(end.get(Calendar.YEAR),  end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH)+10);
        binding.appBarMain.contentgood.calendarView.calendarView.setArrowColor(this.getResources().getColor(R.color.colorPrimary));
        binding.appBarMain.contentgood.calendarView.calendarView.setSelectionColor(this.getResources().getColor(R.color.colorPrimary));
        binding.appBarMain.contentgood.calendarView.calendarView.setSelectedDate(today);
        Calendar minimum = Calendar.getInstance();
        minimum.set(minimum.get(Calendar.YEAR),minimum.get(Calendar.MONTH), minimum.get(Calendar.DAY_OF_MONTH)-50);
        Calendar maximum = Calendar.getInstance();
        maximum.set(maximum.get(Calendar.YEAR),maximum.get(Calendar.MONTH), maximum.get(Calendar.DAY_OF_MONTH)+50);
        binding.appBarMain.contentgood.calendarView.calendarView.state().edit()
                .setMinimumDate(minimum.getTime())
                .setMaximumDate(maximum.getTime())
                .commit();
        //mainActivityViewModel.SetCalendar(binding.appBarMain.calendarView.calendarView);
        binding.appBarMain.contentgood.calendarView.calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                calendarCurrentTime = dt.format(date.getDate().getTime());
                Log.d("current123",calendarCurrentTime);
    }
});


        }

private void closeSubMenusFab(){
        //  layoutFabSave.setVisibility(View.INVISIBLE);
        Snacks.setVisibility(View.INVISIBLE);
        Drinks.setVisibility(View.INVISIBLE);
        Breakfast.setVisibility(View.INVISIBLE);
        Dinner.setVisibility(View.INVISIBLE);
        Lunch.setVisibility(View.INVISIBLE);
        fabsettings.setImageResource(R.drawable.ic_settings_black_24dp);
        fabExpanded = false;
    }


    //Opens FAB submenus
    private void openSubMenusFab(){
        // layoutFabSave.setVisibility(View.VISIBLE);
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
                Toast.makeText(MainActivity.this, "ahahaa", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, FoodSearchActivity.class);
//                intent.putExtra("SharedFoodListDatabase", "SharedDrinks");
//                intent.putExtra("foodselection", "Drinks");
//                startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_WHICH_DATABASE, "SharedDrinks");
                bundle.putString(INTENT_WHICH_TIME, "Drinks");
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        replace(R.id.fragmentContainer, fragment)
                      //  .addToBackStack(null)
                        .commit();

            }
        });

        Drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_WHICH_DATABASE, "SharedDrinks");
                bundle.putString(INTENT_WHICH_TIME, "Drinks");
                FoodSearchFragment fragment = new FoodSearchFragment();
                android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                fragmentManager.executePendingTransactions();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        add(R.id.app_bar_main, fragment, "YOUR_TARGET_FRAGMENT_TAG")
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
                bundle.putString(INTENT_WHICH_DATABASE, "SharedSnacks");
                bundle.putString(INTENT_WHICH_TIME, "Snacks");
                FoodSearchFragment fragment = new FoodSearchFragment();
                android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                fragmentManager.executePendingTransactions();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        add(R.id.app_bar_main, fragment, "YOUR_TARGET_FRAGMENT_TAG")
                        .addToBackStack(null)
                        .commit();
                showUpButton(true);
                isOpenedFragment = true;
            }
        });
        Breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "tttttt", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_WHICH_DATABASE, "SharedBreakfasts");
                bundle.putString(INTENT_WHICH_TIME, "Breakfast");
                FoodSearchFragment fragment = new FoodSearchFragment();
                android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                fragmentManager.executePendingTransactions();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        add(R.id.app_bar_main, fragment, "YOUR_TARGET_FRAGMENT_TAG")
                        .addToBackStack(null)
                        .commit();
                showUpButton(true);
                isOpenedFragment = true;
            }
        });
        Dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "tttttt", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_WHICH_DATABASE, "SharedDinners");
                bundle.putString(INTENT_WHICH_TIME, "Dinner");
                FoodSearchFragment fragment = new FoodSearchFragment();
                android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                fragmentManager.executePendingTransactions();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        add(R.id.app_bar_main, fragment, "YOUR_TARGET_FRAGMENT_TAG")
                        .addToBackStack(null)
                        .commit();
                showUpButton(true);
                isOpenedFragment = true;
            }
        });
        Lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "tttttt", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_WHICH_DATABASE, "SharedLunches");
                bundle.putString(INTENT_WHICH_TIME, "Lunch");
                FoodSearchFragment fragment = new FoodSearchFragment();
                android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                fragmentManager.executePendingTransactions();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        add(R.id.app_bar_main, fragment, "YOUR_TARGET_FRAGMENT_TAG")
                        .addToBackStack(null)
                        .commit();
                showUpButton(true);
                isOpenedFragment = true;
            }
        });
        //Change settings icon to 'X' icon
        fabsettings.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }





    private void showUpButton(boolean show) {
        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if (show) {
            // Remove hamburger
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            // Show back button
            mActionBar.setDisplayHomeAsUpEnabled(true);
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            // Remove back button
            mActionBar.setDisplayHomeAsUpEnabled(false);


            // Show hamburger
            mDrawerToggle.setDrawerIndicatorEnabled(true);

//            navigationView.setNavigationItemSelectedListener(this);
//            // Remove the/any drawer toggle listener
//          //  mDrawerToggle.setToolbarNavigationClickListener(null);
//            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    @Override
    public void onBackPressed() {

  //  mToolbar.getMenu().clear();
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);

        } else {
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackCount >= 1) {
                getSupportFragmentManager().popBackStack();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("YOUR_TARGET_FRAGMENT_TAG");
                if (fragment instanceof FoodNutritiensDisplayFragment) {
                    mToolbar.setTitle(mCurrentTitle);
                }
                if(fragment instanceof FoodSearchFragment)
                {
                    mToolbar.setTitle(getResources().getString(R.string.app_name));
                }
                if(backStackCount == 1){
                    if (fragment instanceof FoodNutritiensDisplayFragment) {
                        mToolbar.setTitle(mCurrentTitle);
                    }
                    if(fragment instanceof FoodSearchFragment)
                    {
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

        int id = item.getItemId();

        switch(item.getItemId()) {
            case R.id.action_logout:
                ReturntoRegister();
                return  true
                        ;
               case android.R.id.home:

                   int backStackCount = getSupportFragmentManager().getBackStackEntryCount();

                   if (backStackCount >= 1) {
                       mDrawerToggle.setDrawerIndicatorEnabled(false);
                       mActionBar.setDisplayHomeAsUpEnabled(true);
                       onBackPressed();
                       return true;
                   }
                   else
                   {
                       mDrawerToggle.setDrawerIndicatorEnabled(true);
                       mActionBar.setDisplayHomeAsUpEnabled(false);
                       mDrawer.openDrawer(GravityCompat.START);
                     return true;
                   }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void ReturntoRegister()
    {

        AuthUI.getInstance().signOut(this)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                       mainActivityViewModel.signOut().observe(MainActivity.this, response->
                       {
                           if(response!=null)
                           {
                               if(response.equals("Success"))
                               {
                                   Toast.makeText(MainActivity.this, "succesfully Sign out", Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(MainActivity.this, BaseActivity.class);
                                   intent.putExtra("offline", true);
                                   startActivity(intent);
                                   finish();
                               }
                               else
                                   Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                           }
                       });
                    }
                    else
                        Toast.makeText(MainActivity.this, "Could not to logout, try again later!", Toast.LENGTH_SHORT).show();
                }
            });
    }

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
            setCurrentTitle(getResources().getString(R.string.Drinks));
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


    private void setCurrentTitle(String title)

    {
        mCurrentTitle = title;
    }
    private String determineWhichType(String whichTime)
    {
        if(whichTime.equals(getResources().getString(R.string.Drinks)))
        {

            return getResources().getString(R.string.SharedDrinks) ;
        }
      else if(whichTime.equals(getResources().getString(R.string.Snacks)))
        {
            return getResources().getString(R.string.SharedSnacks) ;
        }
        else if(whichTime.equals(getResources().getString(R.string.Lunch)))
        {
            return getResources().getString(R.string.SharedLunches) ;
        }
        else if(whichTime.equals(getResources().getString(R.string.Breakfast)))
        {
            return getResources().getString(R.string.SharedBreakfasts) ;
        }
        else return getResources().getString(R.string.SharedDinners) ;

    }
    @Override
    public void onFoodListSelected(String id, String foodName, String whichTime) {
        Toast.makeText(this, "Opened requiredFragment", Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("foodname", foodName);
        bundle.putString("whichtime", whichTime);
        bundle.putString(INTENT_WHICH_DATABASE, determineWhichType(whichTime));
        FoodNutritiensDisplayFragment fragment = new FoodNutritiensDisplayFragment();
        android.support.v4.app.FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().
                add(R.id.app_bar_main, fragment, "YOUR_TARGET_FRAGMENT_TAG")
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
