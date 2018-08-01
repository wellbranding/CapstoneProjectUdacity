package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.completeRedesign.UI.MainActivity.Views.MainActivity;

@Module
public abstract class MainActivityModule {

    @ContributesAndroidInjector(modules =  MainActivityFragmentsModule.class)
    abstract MainActivity contributeMainActivityModule();
}

