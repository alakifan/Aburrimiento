package com.averano.subnormalcounter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    public static String ADD_COUNTER = "AddCounter";
    public static String RESET_COUNTER = "ResetCounter";
    public static String GO_TO_CONFIG = "GoToConfig";
    static int nEstupideces;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ADD_COUNTER))
            nEstupideces = nEstupideces + 1;
        else if (intent.getAction().equals(RESET_COUNTER))
            nEstupideces = 0;
        updateAppWidget(context, AppWidgetManager.getInstance(context), intent.getIntExtra("id", 0));

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String contador = String.valueOf(nEstupideces);

        Intent i = new Intent(context, NewAppWidget.class);
        i.setAction(ADD_COUNTER);
        i.putExtra("id", appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent i2 = new Intent(context, NewAppWidget.class);
        i2.setAction(RESET_COUNTER);
        i2.putExtra("id", appWidgetId);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, appWidgetId, i2, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent i3 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, i3, 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        views.setTextViewText(R.id.contador, contador);
        views.setTextViewText(R.id.nombre, "Javi");
        views.setImageViewResource(R.id.boton, R.drawable.icon);

        views.setOnClickPendingIntent(R.id.boton, pendingIntent);
        views.setOnClickPendingIntent(R.id.reset, pendingIntent2);
        views.setOnClickPendingIntent(R.id.config, pendingIntent3);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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

