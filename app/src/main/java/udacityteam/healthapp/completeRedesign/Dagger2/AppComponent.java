package udacityteam.healthapp.completeRedesign.Dagger2;


import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import udacityteam.healthapp.completeRedesign.ApplicationController;
import udacityteam.healthapp.completeRedesign.Widget.AddedFoodsAppWidget;

@Singleton
@Component(modules = {ActivityModule.class,
        AndroidInjectionModule.class,
        // DetailsOfRecipeActivityModule.class,
        RemoteViewsService.class,
        BaseActivityModule.class,
        FoodListActivityModule.class,
        MainActivityModule.class,
        BakingAppWidgetModule.class,
        AppModule.class})


public interface AppComponent {
    void inject(ApplicationController app);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();

    }

    void inject(AddedFoodsAppWidget addedFoodsAppWidget);

}

