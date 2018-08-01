package udacityteam.healthapp.completeRedesign.Data.Networking.Models;

import java.util.List;

/**
 * Created by vvost on 12/29/2017.
 */

    public class SharedFoodProductsRetrofit  {
   private List<OneSharedFoodProductsListRetrofit>  selectedFoodretrofits;
    public SharedFoodProductsRetrofit()
    {

    }

    public SharedFoodProductsRetrofit(List<OneSharedFoodProductsListRetrofit> selectedFoodretrofits) {
        this.selectedFoodretrofits = selectedFoodretrofits;
    }

    public List<OneSharedFoodProductsListRetrofit> getSelectedFoodretrofits() {
        return selectedFoodretrofits;
    }

    public void setSelectedFoodretrofits(List<OneSharedFoodProductsListRetrofit> selectedFoodretrofits) {
        this.selectedFoodretrofits = selectedFoodretrofits;
    }
}

