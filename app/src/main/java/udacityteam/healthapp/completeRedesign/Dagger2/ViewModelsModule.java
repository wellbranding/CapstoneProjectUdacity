package udacityteam.healthapp.completeRedesign.Dagger2;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import udacityteam.healthapp.completeRedesign.FoodListViewModelComplete;
import udacityteam.healthapp.completeRedesign.SharedFoodListsViewModelNew;
import udacityteam.healthapp.activities.LoginRegisterViewModel;

@Module
public abstract class ViewModelsModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelsFactory(ViewModelFactory viewModelFactory);


    @Binds
    @IntoMap
    @ViewModelsKey(SharedFoodListsViewModelNew.class)
    abstract ViewModel bindSharedFoodListsViewModelNew(SharedFoodListsViewModelNew mainViewModel);

    @Binds
    @IntoMap
    @ViewModelsKey(LoginRegisterViewModel.class)
    abstract ViewModel bindLoginRegisterViewModel(LoginRegisterViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelsKey(FoodListViewModelComplete.class)
    abstract ViewModel bindFoodListViewModelComplete(FoodListViewModelComplete foodListViewModelComplete);
}
