package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.Views.FoodNutritiensDisplayFragment;
import udacityteam.healthapp.completeRedesign.UI.MainActivity.Views.FoodSearchFragment;

@Module
public abstract class MainActivityFragmentsModule {

   @ContributesAndroidInjector()
   abstract FoodSearchFragment contributeFoodSearchFragment();

   @ContributesAndroidInjector()
   abstract FoodNutritiensDisplayFragment contributeFoodNutritiensDisplayFragment();


}
