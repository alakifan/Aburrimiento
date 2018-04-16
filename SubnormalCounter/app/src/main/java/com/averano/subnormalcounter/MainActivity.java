package com.averano.subnormalcounter;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import static com.averano.subnormalcounter.NewAppWidget.ADD_COUNTER;

public class MainActivity extends AppCompatActivity {

    EditText nombre;
    Button botonImagen, cancelar, guardar;
    ImageView imagen;
    private int PICK_IMAGE_REQUEST = 1;
    int nEstupideces;
    public static final String DATOS_MODIFICADOS = "datosModificados";
    int widgetId;
    String uriString;
    private static final String mSharedPrefFile = "com.averano.subnormalcounter";
    SharedPreferences sharedPref;

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("Nombre" + widgetId, nombre.getText().toString());
        prefEditor.putString("Imagen" + widgetId, uriString);
        prefEditor.putInt(ADD_COUNTER + widgetId, nEstupideces);
        prefEditor.apply();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref= this.getSharedPreferences(
                mSharedPrefFile, Context.MODE_PRIVATE);

        nombre = findViewById(R.id.nombre);
        botonImagen = findViewById(R.id.botonImagen);
        imagen = findViewById(R.id.imagen);
        cancelar = findViewById(R.id.cancelar);
        guardar = findViewById(R.id.guardar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            uriString = extras.getString("imagen");
            imagen.setImageURI(Uri.parse(uriString));

            nEstupideces = extras.getInt("estupideces", 0);

        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!nombre.getText().toString().isEmpty()){

                    Intent i = new Intent(MainActivity.this, NewAppWidget.class);
                    i.setAction(DATOS_MODIFICADOS);
                    i.putExtra("nombre", nombre.getText().toString());
                    i.putExtra("id", widgetId);
                    i.putExtra("imagen", uriString);
                    i.putExtra("estupideces", nEstupideces);
                    getApplicationContext().sendBroadcast(i);
                    finish();

                }else
                    Toast.makeText(MainActivity.this, "Introduce un nombre", Toast.LENGTH_SHORT).show();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        botonImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            uriString = uri.toString();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imagen.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}