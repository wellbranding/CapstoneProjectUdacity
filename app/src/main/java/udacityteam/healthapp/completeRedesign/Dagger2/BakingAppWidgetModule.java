package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.completeRedesign.Widget.AddedFoodsAppWidget;

@Module
public abstract class BakingAppWidgetModule {
    @ContributesAndroidInjector()
    abstract AddedFoodsAppWidget contributeBakingAppWidget();
}
