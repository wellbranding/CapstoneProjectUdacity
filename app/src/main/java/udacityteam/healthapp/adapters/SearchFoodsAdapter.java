package udacityteam.healthapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.FoodNutritiensDisplay;
import udacityteam.healthapp.activities.FoodSearchActivity;
import udacityteam.healthapp.models.Model;


/**
 * Created by vvost on 11/16/2017.
 */

public class SearchFoodsAdapter extends RecyclerView.Adapter<SearchFoodsAdapter.ViewHolder>  {
    private static final String TAG = "CustomAdapter";
    private String whichTime;

    private List<Model> mDataSet = new ArrayList<>();
    public interface OnItemClickListener {
        void onItemClick(String id, String name, String whichTime);
    }

    private Context context;
    private final OnItemClickListener listener;
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(mDataSet.get(getAdapterPosition()).getId(),
                            mDataSet.get(getAdapterPosition()).getName(), whichTime );
                }
            });
                    textView = v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public SearchFoodsAdapter(List<Model> dataSet, String whichTime, OnItemClickListener listener) {
        mDataSet = dataSet;
        this.whichTime = whichTime;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.food_list_item, viewGroup, false);

        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(mDataSet.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}