package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.activities.BaseActivity;
import udacityteam.healthapp.activities.MainActivity;

@Module
public abstract class MainActivityModule {

    @ContributesAndroidInjector(modules =  MainActivityFragmentsModule.class)
    abstract MainActivity contributeMainActivityModule();
}

