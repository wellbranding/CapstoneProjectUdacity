package udacityteam.healthapp.completeRedesign.UI.Community.Views;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import udacityteam.healthapp.R;

public class CommunityList extends AppCompatActivity implements HasSupportFragmentInjector {

    Resources res;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity_main);
        res = getResources();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String value;
        String foodselection = null;
        String SharedFoodListDatabase = null;
        if (b != null) {
            value = (String) b.get(getString(R.string.which_title_key));
            foodselection = (String) b.get(getString(R.string.which_time_key));
            SharedFoodListDatabase = (String) b.get(getString(R.string.shared_food_list_database_key));
            getSupportActionBar().setTitle(value);
        }
        Bundle tofragment = new Bundle();
        tofragment.putString("foodselection", foodselection);
        tofragment.putString("SharedFoodListDatabase", SharedFoodListDatabase);
        String[] tabTitles = new String[]{
                res.getString(R.string.tab1_title),
                res.getString(R.string.tab2_title)};
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabTitles, tofragment);

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_blank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private String[] tabTitles;
        private Bundle queryParam;

        public SectionsPagerAdapter(FragmentManager fm, String[] tabTitles, Bundle qureryparam) {
            super(fm);
            this.tabTitles = tabTitles;
            this.queryParam = qureryparam;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return SharedFoodListFragmentNetwork.newInstance(queryParam);
                case 1:
                    return new CommunityBlankFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
