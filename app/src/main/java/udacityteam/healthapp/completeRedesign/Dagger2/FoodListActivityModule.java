package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.activities.BaseActivity;
import udacityteam.healthapp.completeRedesign.FoodListComplete;

@Module
public abstract class FoodListActivityModule {

    @ContributesAndroidInjector()
    abstract FoodListComplete contributeFoodListActivity();
}

