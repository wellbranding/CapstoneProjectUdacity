package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.completeRedesign.Widget.BakingAppWidget;

@Module
public abstract class BakingAppWidgetModule {
    @ContributesAndroidInjector()
    abstract BakingAppWidget contributeBakingAppWidget();
}
