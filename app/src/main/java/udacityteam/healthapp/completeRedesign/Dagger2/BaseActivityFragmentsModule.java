package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.activities.LoginWithMailFragment;
import udacityteam.healthapp.activities.RegisterWithMailFragment;

@Module
public abstract class BaseActivityFragmentsModule {
    @ContributesAndroidInjector()
    abstract LoginWithMailFragment contributeLoginWithMailFragment();
    @ContributesAndroidInjector()
    abstract RegisterWithMailFragment contributeRegisterWithMailFragment();
//
//   @ContributesAndroidInjector()
//   abstract SharedFoodListFragmentNetwork contributeSharedFoodListFragmentNetwork();


}
