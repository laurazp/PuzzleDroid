package com.ultimapieza.puzzledroid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ultimapieza.puzzledroid.db.DbHelper;
import com.ultimapieza.puzzledroid.db.DbHelperNewPlayer;


public class MainActivity extends AppCompatActivity {

    String filePath = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3";
    public static boolean MediaPlayer = false;
    boolean musicPlaying = false;
    Intent serviceIntent;
    Button location;
    TextView tvUbicacion;

    // Bundle pasa información desde una actividad a otra
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Lanzamos el servicio para la música
        serviceIntent = new Intent(this, MyService.class);
        serviceIntent.putExtra("FilePath", filePath);


        // Asignamos botones buscando por su id
        Button playButton = findViewById(R.id.playBtn);
        Button optionsButton = findViewById(R.id.optionsBtn);
        Button scoreButton = findViewById(R.id.scoreBtn);


        // Cambiamos el color de los botones
        playButton.setBackgroundColor(Color.parseColor("#F7C52C"));
        optionsButton.setBackgroundColor(Color.parseColor("#16C282"));

        Button button_location=findViewById(R.id.location);
        TextView tvUbicacion=findViewById(R.id.tvUbicacion);
        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager=(LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener=new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull android.location.Location location) {
                        tvUbicacion.setText(""+location.getLatitude()+""+location.getLongitude());
                        Intent intentlocation = new Intent (view.getContext(), Location.class);
                        view.getContext().startActivity(intentlocation);
                    }
                    public void OnStatusChanged(String provide, int status, Bundle extras){}
                    public void OnProviderEnabled(String provide){}
                    public void OnProviderDisabled(String provide, int status, Bundle extras){}

                };
                int permissionCheck= ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER,0,0,locationListener);
            }
        });
        int permissionCheck= ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,android.Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }


        // Al hacer click en el botón Play, nos lleva a PlayActivity
        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), LoginActivity.class);
//                Intent intent = new Intent (v.getContext(), PlayActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        // Al hacer click en el botón Options, nos lleva a SettingsActivity
        optionsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent (v.getContext(), SettingsActivity.class);
                v.getContext().startActivity(intent2);
            }
        });

        // Al hacer click en el botón ScoreTable, nos lleva al ranking de puntuaciones
        scoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO: Modificar acción al hacer click para que cargue la pantalla de ScoreTable
                DbHelper dbHelper = new DbHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                DbHelperNewPlayer dbNhelper= new DbHelperNewPlayer(MainActivity.this);
                SQLiteDatabase dbNew = dbNhelper.getWritableDatabase();
                /*if (db != null || dbNew !=null) {
                    Toast.makeText(MainActivity.this, "DATABASE SUCCESFULLY CREATED", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "ERROR CREATING THE DATABASE", Toast.LENGTH_LONG).show();
                }*/
                Intent intent5 = new Intent (v.getContext(), ScoreActivity.class);
                v.getContext().startActivity(intent5);
            }
        });
    }

    // Create the Action Bar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    // Select options in the Action Bar Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_info:
                Toast.makeText(this, "Info Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_Login:
                Toast.makeText(this, "Authentication Selected", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(this, AuthActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_Logout:
                Toast.makeText(this, "Authentication Selected", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(this, HomeActivity.class);
                startActivity(intent4);
                break;
            case R.id.nav_help:
                Toast.makeText(this, "Help Selected", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, HelpActivity.class);
                startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("background", "método onResume llamado");
        if (!MediaPlayer) {

            serviceIntent = new Intent(this, MyService.class);
            serviceIntent.putExtra("FilePath", filePath);
            startService(serviceIntent);



          /*Intent i = new Intent(this, MyService.class);
            i.putExtra("action", startService(serviceIntent));
            startService(serviceInteIntentnt)(i);

             */
            MediaPlayer = true;
        }
        /*else {
            Log.d("background", "método onResume y else llamado");
            serviceIntent = new Intent(this, MyService.class);
            serviceIntent.putExtra("FilePath", filePath);
            startService(serviceIntent);


            MediaPlayer = true;
        }*/


    }

    /*@Override
    protected void onPause() {
        super.onPause();
        //La app esta en segundo plano (background).
        serviceIntent.putExtra("background", true);
        Log.d("background", "método onPause llamado");
        stopService(serviceIntent);
    }*/



    /*@Override
    public void onBackPressed() {
         if (webView.canGoBack()){
             webView.goBack();
         } else {
             super.onBackPressed();
         }
     }*/

}