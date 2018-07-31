package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.activities.FoodNutritiensDisplayFragment;
import udacityteam.healthapp.activities.FoodSearchFragment;

@Module
public abstract class MainActivityFragmentsModule2 {

   @ContributesAndroidInjector()
   abstract FoodNutritiensDisplayFragment contributeFoodNutritiensDisplayFragment();


}
