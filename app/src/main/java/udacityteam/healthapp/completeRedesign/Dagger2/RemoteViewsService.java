package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.completeRedesign.Widget.AddedFoodsAppWidgetService;

@Module
public abstract class RemoteViewsService {
    @ContributesAndroidInjector()
    abstract AddedFoodsAppWidgetService contributeRemoteViewsService();
}
