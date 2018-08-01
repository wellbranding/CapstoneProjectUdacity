package udacityteam.healthapp.completeRedesign.Widget;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.SelectedFoodretrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.db.MainDatabase;

public class AddedFoodsAppWidgetService extends RemoteViewsService {

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    MainDatabase mainDatabase;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingAppWidgetRemoteViewsFactory(mainDatabase, getApplicationContext(), sharedPreferences);
    }
    LiveData<List<SelectedFoodretrofit>> finalRecipeFromId;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    class BakingAppWidgetRemoteViewsFactory implements RemoteViewsFactory, LifecycleOwner, Observer<List<SelectedFoodretrofit>> {

        MainDatabase mainDatabase;
        Context mContext;
        SharedPreferences sharedPreferences;
        List<SelectedFoodretrofit> ingredientList;
        LiveData<List<SelectedFoodretrofit>> recipeFromId;


        public BakingAppWidgetRemoteViewsFactory(MainDatabase mainDatabase, Context context, SharedPreferences sharedPreferences) {
            mContext = context;
            this.mainDatabase = mainDatabase;
            this.sharedPreferences = sharedPreferences;
            finalRecipeFromId = new MutableLiveData<>();
            recipeFromId=new MutableLiveData<>();


        }


        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            String query = sharedPreferences.getString("query_widget", null);
            String whichTime = sharedPreferences.getString("whichtime_widget", null);

            if(whichTime!=null && query!=null) {
                recipeFromId = mainDatabase.recipeDao().getAddedFoodsNew(whichTime,
                        query);
            }
            recipeFromId.observeForever(this);


                }




        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredientList == null)
                return 0;
            return ingredientList.size();

        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position == AdapterView.INVALID_POSITION || ingredientList == null) {
                return null;
            }

            String formated = ingredientList.get(position).getFoodName();


            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.text_view_widget);
            remoteViews.setTextViewText(R.id.list_view_item_widget,
                    formated);
            Intent fillIntent = new Intent();
            remoteViews.setOnClickFillInIntent(R.id.list_view_item_widget, fillIntent);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return null;
        }


        @Override
        public void onChanged(@Nullable List<SelectedFoodretrofit> selectedFoodretrofits) {

                ingredientList = new ArrayList<>();
                ingredientList.addAll(selectedFoodretrofits);
                recipeFromId.removeObserver(this);


        }
    }
}
