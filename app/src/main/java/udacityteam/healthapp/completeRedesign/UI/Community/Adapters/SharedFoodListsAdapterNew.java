package udacityteam.healthapp.completeRedesign.UI.Community.Adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.completeRedesign.UI.Community.ViewModels.SharedFoodListItemViewModel;
import udacityteam.healthapp.databinding.SharedFoodsListItemBinding;

public class SharedFoodListsAdapterNew extends RecyclerView.Adapter<SharedFoodListsAdapterNew.RepositoryViewHolder> {

    private List<OneSharedFoodProductsListRetrofit> oneSharedFoodProductsListRetrofits;
    private String foodselection;

    public SharedFoodListsAdapterNew(String foodselection) {
        this.oneSharedFoodProductsListRetrofits = new ArrayList<OneSharedFoodProductsListRetrofit>();
        this.foodselection = foodselection;
    }

    public void setSelectedFoods(List<OneSharedFoodProductsListRetrofit> repositories) {
        this.oneSharedFoodProductsListRetrofits = repositories;
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SharedFoodsListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.shared_foods_list_item,
                parent,
                false);
        return new RepositoryViewHolder(binding, foodselection);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder holder, int position) {
        holder.bindRepository(oneSharedFoodProductsListRetrofits.get(position));
    }

    @Override
    public int getItemCount() {
        if (oneSharedFoodProductsListRetrofits != null)
            return oneSharedFoodProductsListRetrofits.size();
        else return 0;
    }

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {
        final SharedFoodsListItemBinding binding;
        String foodselection;

        public RepositoryViewHolder(SharedFoodsListItemBinding binding, String foodselection) {
            super(binding.layout);
            this.binding = binding;
            this.foodselection = foodselection;
        }

        void bindRepository(OneSharedFoodProductsListRetrofit repository) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new SharedFoodListItemViewModel(itemView.getContext(), repository, foodselection));
            } else {
                binding.getViewModel().setSelectectedFoood(repository);
            }
        }
    }
}