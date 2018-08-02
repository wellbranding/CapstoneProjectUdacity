package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.Views.FoodListComplete;

@Module
public abstract class FoodListActivityModule {

    @ContributesAndroidInjector()
    abstract FoodListComplete contributeFoodListActivity();
}

