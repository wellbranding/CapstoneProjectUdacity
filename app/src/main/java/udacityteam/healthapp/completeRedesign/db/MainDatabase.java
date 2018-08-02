package udacityteam.healthapp.completeRedesign.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import udacityteam.healthapp.completeRedesign.Data.Networking.Models.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.UserRetrofitGood;


@Database(entities = {OneSharedFoodProductsListRetrofit.class,
        UserRetrofitGood.class, SelectedFoodretrofit.class}, version = MainDatabase.VERSION)
public abstract class MainDatabase extends RoomDatabase {

    public abstract MainDao recipeDao();

    static final int VERSION = 6;

}
