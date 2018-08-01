package udacityteam.healthapp.completeRedesign.UI.AddedFoods.Adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;


import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.UI.AddedFoods.ViewModels.FoodListItemViewModel;
import udacityteam.healthapp.databinding.FoodListItemBinding;


public class FoodListRetrofitAdapterNew extends RecyclerView.Adapter<FoodListRetrofitAdapterNew.RepositoryViewHolder> {

    private List<SelectedFoodretrofit> selectedFoodretrofits;

    public void setSelectedFoodretrofits(List<SelectedFoodretrofit> selectedFoodretrofits) {
        this.selectedFoodretrofits = selectedFoodretrofits;
    }

    public FoodListRetrofitAdapterNew() {
        this.selectedFoodretrofits = Collections.emptyList();
    }

    public FoodListRetrofitAdapterNew(List<SelectedFoodretrofit> repositories) {
        this.selectedFoodretrofits = repositories;
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       FoodListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.food_list_item,
                parent,
                false);
        return new RepositoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder holder, int position) {
        holder.bindRepository(selectedFoodretrofits.get(position));
    }

    @Override
    public int getItemCount() {
        return selectedFoodretrofits.size();
    }

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {
        final FoodListItemBinding  binding;

        public RepositoryViewHolder(FoodListItemBinding binding) {
            super(binding.layout);
            this.binding = binding;
        }

        void bindRepository(SelectedFoodretrofit repository) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new FoodListItemViewModel(itemView.getContext(), repository));
            } else {
                binding.getViewModel().setSelectectedFoood(repository);
            }
        }
    }

}
