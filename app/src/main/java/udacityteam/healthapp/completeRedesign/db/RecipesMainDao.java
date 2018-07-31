package udacityteam.healthapp.completeRedesign.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.SelectedFoodretrofit;
import udacityteam.healthapp.Model.UserRetrofitGood;
import udacityteam.healthapp.Model.Userretrofit;


@Dao
public interface RecipesMainDao {
    @Query("SELECT * FROM OneSharedFoodProductsListRetrofit WHERE " +
            "whichDatabase ==:whichDatabase ")
    public abstract LiveData<List<OneSharedFoodProductsListRetrofit>> getRecipes(String whichDatabase);
//
//    @Query("SELECT * FROM SelectedFoodretrofit WHERE " +
//            "whichTime ==:whichTime AND  strftime('%Y', SelectedFoodretrofit.SendDate) = :year  "
//            )
//    LiveData<List<SelectedFoodretrofit>> getAddedFoods(String whichTime,
//                                                                    String year, String month, String day
//    );
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

    @Delete
    public void deleteCurrentUser(UserRetrofitGood userRetrofitGood);
   @Insert
    public void insertCurrentUser(UserRetrofitGood userretrofit);

  //  @Query("SELECT * FROM ")
//
//    @Transaction
//    @Query("SELECT * FROM Recipe WHERE id = :id")
//    public abstract Recipe getRecipe(int id);
//
//    @Insert
//    public abstract void insertAllRecipes(List<Recipe> recipeList);
  @Insert
  public void insertAllAddedFoodLists(List<SelectedFoodretrofit> oneSelectedFoodRetrofit);

    @Insert
    public void insertOneAddedFood(SelectedFoodretrofit oneSelectedFoodRetrofit);
    @Insert
    public void insertAllOneSharedFoodProductsListRetrofit(List<OneSharedFoodProductsListRetrofit> oneSharedFoodProductsListRetrofits);
    @Insert
    public abstract void insertOneSharedFoodProductsListRetrofit(OneSharedFoodProductsListRetrofit oneSharedFoodProductsListRetrofit);
}



