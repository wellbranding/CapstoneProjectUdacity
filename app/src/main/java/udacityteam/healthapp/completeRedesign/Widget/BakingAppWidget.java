package udacityteam.healthapp.completeRedesign.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import javax.inject.Inject;

import udacityteam.healthapp.R;
import udacityteam.healthapp.activities.MainActivity;

import static dagger.android.AndroidInjection.inject;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    @Inject
    SharedPreferences sharedPreferences;

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId[]) {
        inject(this, context);
        for (int singleId : appWidgetId) {
            Intent intent = new Intent(context, MainActivity.class);
            Intent serviceIntent = new Intent(context, BakingAppWidgetService.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            views.setRemoteAdapter(R.id.recipe_list_widget, serviceIntent);
            views.setPendingIntentTemplate(R.id.recipe_list_widget, pendingIntent);
            views.setOnClickPendingIntent(R.id.parent_relative_layout_widget, pendingIntent);

            String title = sharedPreferences.getString("whichtime_widget", "Error");

            views.setTextViewText(R.id.recipe_title_widget, title);
            appWidgetManager.updateAppWidget(singleId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        inject(this, context);
        // There may be multiple widgets active, so update all of them

        updateAppWidget(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

