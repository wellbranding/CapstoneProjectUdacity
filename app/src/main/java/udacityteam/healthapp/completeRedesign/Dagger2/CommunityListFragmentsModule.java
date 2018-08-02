package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.completeRedesign.UI.Community.Views.SharedFoodListFragmentNetwork;

@Module
public abstract class CommunityListFragmentsModule {
    @ContributesAndroidInjector()
    abstract SharedFoodListFragmentNetwork contributeSharedFoodListFragmentNetwork();

}
