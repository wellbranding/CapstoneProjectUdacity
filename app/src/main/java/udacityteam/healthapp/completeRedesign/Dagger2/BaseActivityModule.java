package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.Views.BaseActivity;

@Module
public abstract class BaseActivityModule {

    @ContributesAndroidInjector(modules = BaseActivityFragmentsModule.class)
    abstract BaseActivity contributeBaseActivity();
}

