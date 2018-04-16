package com.averano.subnormalcounter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class NewAppWidget extends AppWidgetProvider {

    public static String ADD_COUNTER = "AddCounter";
    public static String RESET_COUNTER = "ResetCounter";
    public static final String DATOS_MODIFICADOS = "datosModificados";
    private static final String mSharedPrefFile = "com.averano.subnormalcounter";
    static int nEstupideces;
    static String nombre;
    static Uri uri = Uri.parse("android.resource://" + "com.averano.subnormalcounter" + "/" + R.drawable.icon);


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ADD_COUNTER))
            nEstupideces = nEstupideces + 1;
        else if (intent.getAction().equals(RESET_COUNTER))
            nEstupideces = 0;
        else if (intent.getAction().equals(DATOS_MODIFICADOS)){

            nombre = intent.getStringExtra("nombre");
            uri = Uri.parse(intent.getStringExtra("imagen"));
            nEstupideces = intent.getIntExtra("estupideces", 1);

        }
        updateAppWidget(context, AppWidgetManager.getInstance(context), intent.getIntExtra("id", 0));

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(
                mSharedPrefFile, Context.MODE_PRIVATE);

        //SharedPreferences.Editor prefEditor = prefs.edit();
        //prefEditor.putInt(ADD_COUNTER + appWidgetId, nEstupideces);
        //prefEditor.apply();

        nEstupideces = prefs.getInt(ADD_COUNTER + appWidgetId, 0);
        nombre = prefs.getString("Nombre" + appWidgetId, "");
        uri = Uri.parse(prefs.getString("Imagen" + appWidgetId, ""));

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
        i3.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        i3.putExtra("imagen", uri.toString());
        i3.putExtra("estupideces", nEstupideces);

        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, appWidgetId, i3, PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        views.setTextViewText(R.id.contador, contador);
        views.setTextViewText(R.id.nombre, nombre);
        views.setImageViewUri(R.id.boton, uri);

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

}

