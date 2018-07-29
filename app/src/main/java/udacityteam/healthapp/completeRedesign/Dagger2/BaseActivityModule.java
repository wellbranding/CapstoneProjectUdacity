package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.activities.BaseActivity;

@Module
public abstract class BaseActivityModule {

    @ContributesAndroidInjector(modules = BaseActivityFragmentsModule.class)
    abstract BaseActivity contributeBaseActivity();
}

