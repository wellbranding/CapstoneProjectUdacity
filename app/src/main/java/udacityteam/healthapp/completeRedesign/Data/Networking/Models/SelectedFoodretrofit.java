package udacityteam.healthapp.completeRedesign.Data.Networking.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kunda on 10/4/2017.
 */
@Entity
public class SelectedFoodretrofit implements  Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int roomId;
    private String foodId;
    private String foodName;
    private String whichTime;
//    private String UserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String SendDate;
    private float Calories;
    private float Protein;
    private float Fat;
    private float Carbohydrates;
   //  private String mail;
   //  private String


    public String getFoodId() {
        return foodId;
    }

    public String getWhichTime() {
        return whichTime;
    }

    public void setWhichTime(String whichTime) {
        this.whichTime = whichTime;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getSendDate() {
        return SendDate;
    }

    public void setSendDate(String sendDate) {
        SendDate = sendDate;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public SelectedFoodretrofit()
    {

    }

    public SelectedFoodretrofit(String foodId, String foodName, String whichTime, String sendDate, float calories, float protein, float fat, float carbohydrates) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.whichTime = whichTime;
        SendDate = sendDate;
        Calories = calories;
        Protein = protein;
        Fat = fat;
        Carbohydrates = carbohydrates;
    }

    protected SelectedFoodretrofit(Parcel in) {
        foodId = in.readString();
        foodName = in.readString();
        SendDate = in.readString();
        Calories = in.readFloat();
        Protein = in.readFloat();
        Fat = in.readFloat();
        Carbohydrates = in.readFloat();
    }

    public static final Creator<SelectedFoodretrofit> CREATOR = new Creator<SelectedFoodretrofit>() {
        @Override
        public SelectedFoodretrofit createFromParcel(Parcel in) {
            return new SelectedFoodretrofit(in);
        }

        @Override
        public SelectedFoodretrofit[] newArray(int size) {
            return new SelectedFoodretrofit[size];
        }
    };

    public float getProtein() {
        return Protein;
    }

    public void setProtein(float protein) {
        Protein = protein;
    }

    public float getFat() {
        return Fat;
    }

    public void setFat(float fat) {
        Fat = fat;
    }

    public float getCarbohydrates() {
        return Carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        Carbohydrates = carbohydrates;
    }

    public float getCalories() {
        return Calories;
    }

    public void setCalories(float calories) {
        Calories = calories;
    }

    public String getDate() {
        return SendDate;
    }

    public void setDate(String date) {
        SendDate = date;
    }


    public String getFoodid() {
        return foodId;
    }

    public void setFoodid(String foodid) {
        this.foodId = foodid;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodId);
        dest.writeString(foodName);
       // dest.writeString(UserId);
        dest.writeString(SendDate);
        dest.writeFloat(Calories);
        dest.writeFloat(Protein);
        dest.writeFloat(Fat);
        dest.writeFloat(Carbohydrates);
    }
}
