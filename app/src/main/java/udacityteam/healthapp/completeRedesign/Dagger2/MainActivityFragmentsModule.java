package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.activities.CommunityActivities.SharedFoodListFragmentNetwork;
import udacityteam.healthapp.activities.FoodNutritiensDisplayFragment;
import udacityteam.healthapp.activities.FoodSearchFragment;

@Module
public abstract class MainActivityFragmentsModule {

   @ContributesAndroidInjector()
   abstract FoodSearchFragment contributeFoodSearchFragment();

   @ContributesAndroidInjector()
   abstract FoodNutritiensDisplayFragment contributeFoodNutritiensDisplayFragment();


}
