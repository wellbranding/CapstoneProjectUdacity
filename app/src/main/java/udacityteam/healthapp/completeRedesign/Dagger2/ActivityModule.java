package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.completeRedesign.UI.Community.Views.CommunityList;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = CommunityListFragmentsModule.class)
    abstract CommunityList contributeMainActivity();

//    @ContributesAndroidInjector()
//    abstract BaseActivity contributeBaseActivity();
}

