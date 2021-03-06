package udacityteam.healthapp.completeRedesign.Dagger2;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.ViewModels.FoodListViewModelComplete;
import udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.ViewModels.LoginRegisterViewModel;
import udacityteam.healthapp.completeRedesign.UI.Community.ViewModels.SharedFoodListsViewModelNew;
import udacityteam.healthapp.completeRedesign.UI.MainActivity.ViewModels.MainActivityViewModelGood;

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

    @Binds
    @IntoMap
    @ViewModelsKey(MainActivityViewModelGood.class)
    abstract ViewModel bindMainViewModel(MainActivityViewModelGood mainActivityViewModelGood);
}
