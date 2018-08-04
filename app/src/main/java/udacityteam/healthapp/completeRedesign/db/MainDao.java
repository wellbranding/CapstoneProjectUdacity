package udacityteam.healthapp.completeRedesign.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import udacityteam.healthapp.completeRedesign.Data.Networking.Models.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.UserRetrofitGood;


@Dao
public interface MainDao {
    @Query("SELECT * FROM OneSharedFoodProductsListRetrofit WHERE " +
            "whichDatabase ==:whichDatabase ")
    public abstract LiveData<List<OneSharedFoodProductsListRetrofit>> getRecipes(String whichDatabase);

    @Query("SELECT * FROM SelectedFoodretrofit WHERE " +
            "whichTime ==:whichTime AND  strftime('%Y', SelectedFoodretrofit.SendDate) = :year AND strftime('%M', SelectedFoodretrofit.SendDate) =:month AND strftime('%D', SelectedFoodretrofit.SendDate) =:day "
    )
    LiveData<List<SelectedFoodretrofit>> getAddedFoods(String whichTime,
                                                       String year, String month, String day
    );
    @Query("SELECT * FROM SelectedFoodretrofit WHERE " +
            "whichTime ==:whichTime AND SendDate LIKE :query  "
    )
    LiveData<List<SelectedFoodretrofit>> getAddedFoodsNew(String whichTime, String query
    );

    @Transaction
    @Query("SELECT * FROM SelectedFoodretrofit WHERE " +
            "whichTime ==:whichTime AND SendDate LIKE :query  "
    )
   List<SelectedFoodretrofit> getAddedFoodsWidget(String whichTime, String query
    );

    @Delete
    public void deleteCurrentUser(UserRetrofitGood userRetrofitGood);

    @Insert
    public void insertCurrentUser(UserRetrofitGood userretrofit);
    @Insert
    public void insertAllAddedFoodLists(List<SelectedFoodretrofit> oneSelectedFoodRetrofit);

    @Insert
    public void insertOneAddedFood(SelectedFoodretrofit oneSelectedFoodRetrofit);

    @Insert
    public void insertAllOneSharedFoodProductsListRetrofit(List<OneSharedFoodProductsListRetrofit> oneSharedFoodProductsListRetrofits);

    @Insert
    public abstract void insertOneSharedFoodProductsListRetrofit(OneSharedFoodProductsListRetrofit oneSharedFoodProductsListRetrofit);
}



