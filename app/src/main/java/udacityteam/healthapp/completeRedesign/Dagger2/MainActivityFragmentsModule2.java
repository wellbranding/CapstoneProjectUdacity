package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.Views.FoodNutritiensDisplayFragment;

@Module
public abstract class MainActivityFragmentsModule2 {

   @ContributesAndroidInjector()
   abstract FoodNutritiensDisplayFragment contributeFoodNutritiensDisplayFragment();


}
