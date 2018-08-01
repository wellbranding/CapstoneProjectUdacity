package udacityteam.healthapp.completeRedesign.Data.Networking.API;


import android.arch.lifecycle.LiveData;

import java.sql.Timestamp;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.Result;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofitarray;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SharedFoodProductsRetrofit;

public interface APIService {
    String BASE_URL = "http://app.wellbranding.com/";

    @FormUrlEncoded
    @POST("register")
   Call<Result> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("uid") String uid);

    @FormUrlEncoded
    @POST("addSelectedFood")
    LiveData<ApiResponse<Result>> addSelectedFood(
            @Field("foodId") String foodId,
            @Field("foodName") String foodName,
            @Field("UserId") Integer UserId,
            @Field("Date") Timestamp Date,
            @Field("Calories") Float Calories,
            @Field("Protein") Float Protein,
            @Field("Fat") Float Fat,
            @Field("Carbohydrates") Float Carbohyrates,
            @Field("whichtime") String whichtime,
            @Field("sharedfoodId") Integer sharedFoodId
    );
    @GET("getSelectedFoodsPrieview")
    Observable<SelectedFoodretrofitarray> getselectedfoodsPrieview(
            @Query("getParentSharedFoodsId") Integer ParentSharedKey,
            @Query("foodselection") String foodSelection
    );

    @FormUrlEncoded
    @POST("addSharedList")
    Call<Result> addSharedList(
            @Field("UserId") Integer UserId,
            @Field("Date") Timestamp Date,
            @Field("SharedFoodListDatabase") String SharedFoodListDatabase,
            @Field("whichtime") String whichtime,
            @Field("Calories") Float Calories,
            @Field("Protein") Float Protein,
            @Field("Fat") Float Fat,
            @Field("Carbohydrates") Float Carbohyrates
    );

    @GET("getSelectedFoods")
 LiveData<ApiResponse<SelectedFoodretrofitarray>> getSelectedFoods(
            @Query("UserId") Integer UserId,
            @Query("whichtime") String whichTime,
            @Query("year") String year,
            @Query("month") String month,
            @Query("day") String day
    );

    @GET("getAllSharedDiets")
    LiveData<ApiResponse<SharedFoodProductsRetrofit>> getAllSharedDiets(
            @Query("UserId") Integer UserId,
            @Query("SharedFoodListDatabase") String SharedFoodListDatabase
    );
    @GET("getAllFilteredSharedDiets")
   Call<SharedFoodProductsRetrofit> getAllFilteredSharedDiets(
            @Query("UserId") Integer UserId,
            @Query("SharedFoodListDatabase") String SharedFoodListDatabase,
            @Query("ProteinBegin") Integer proteinbegin,
            @Query("ProteinEnd") Integer proteinend,
            @Query("CaloriesBegin") Integer caloriesbegin,
            @Query("CaloriesEnd") Integer caloriesend,
            @Query("CarbohydratesBegin") Integer carbohydratesbegin,
            @Query("CarbohydratesEnd") Integer carbohydratesend,
            @Query("FatsBegin") Integer fatsbegin,
            @Query("FatsEnd") Integer fatssend

    );

    @GET("IsShared")
    Call<Result> getIsShared
            (
                    @Query("UserId") Integer UserId,
                    @Query("date") Timestamp Date,
                    @Query("whichtime") String whichtime
            );



}
