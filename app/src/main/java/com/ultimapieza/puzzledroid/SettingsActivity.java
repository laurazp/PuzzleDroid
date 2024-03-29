package com.ultimapieza.puzzledroid;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQ_CODE_PICK_SOUNDFILE = 0;
    Switch switch1, switch2;
    boolean stateSwitch1, stateSwitch2;
    MediaPlayer mediaPlayer;
    @BindView(R.id.backBtn) Button backButton;
    @BindView(R.id.buttonSelect) Button buttonSelect;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    String filePath = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3";
    Intent serviceIntent;
    SharedPreferences preferences;
    private File audio;
    MyService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Button backButton = findViewById(R.id.backBtn);
        //Button buttonSelect = findViewById(R.id.buttonSelect);
        ButterKnife.bind(this);

        // Cambiamos el color de los botones

        backButton.setBackgroundColor(Color.parseColor("#16C282"));

        // Solicita permisos para acceder a archivos del dispositivo
        verifyStoragePermissions(this);

        // Lanzamos el servicio para la música
        serviceIntent = new Intent(this, MyService.class);
        serviceIntent.putExtra("FilePath", filePath);

        preferences = getSharedPreferences("PREFS", 0);
        stateSwitch1 = preferences.getBoolean("switch1", false);
        stateSwitch2 = preferences.getBoolean("switch2", false);

        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);

        switch1.setChecked(stateSwitch1);
        switch2.setChecked(stateSwitch2);

        // Switch for turning the music on/off
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch1 = !stateSwitch1;
                switch1.setChecked(stateSwitch1);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch1", stateSwitch1);
                editor.apply();

                // TODO: PRUEBAS PARA APAGAR Y ENCENDER MÚSICA
                if (switch1.isChecked()) {
                    //encender musica
                    try {
                        startService(serviceIntent);
                    } catch (Exception e) {
                        Toast.makeText(SettingsActivity.this, "Error starting music.", Toast.LENGTH_SHORT).show();
                    }
                    //stateSwitch1 = false;
                    //SharedPreferences.Editor editor = preferences.edit();
                    //editor.putBoolean("switch1", false);
                    //editor.apply();
                } else {
                    //apagar musica
                    try {
                        stopService(serviceIntent);
                    } catch (Exception e) {
                        Toast.makeText(SettingsActivity.this, "Error stopping music.", Toast.LENGTH_SHORT).show();
                    }

                    //stateSwitch1 = true;
                    //SharedPreferences.Editor editor = preferences.edit();
                    //editor.putBoolean("switch1", true);
                    //editor.apply();
                }
            }
        });

        // TODO: Switch for turning sounds on/off
        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch2 = !stateSwitch2;
                switch2.setChecked(stateSwitch2);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch2", stateSwitch2);
                editor.apply();
            }
        });


        // When clicking on backBtn, go back to Menu
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        // Botón Select (para seleccionar el audio del móvil)
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMusic = new Intent(Intent.ACTION_GET_CONTENT);
                intentMusic.setType("audio/*");
                startActivityForResult(intentMusic, 1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri audio = data.getData(); //declared above Uri audio;
            Log.d("media", "onActivityResult: " + audio);

            // Prueba Uri to Path
            File file = new File(audio.getPath()); //create path from uri
            Log.d("media", "audio: " + audio);
            final String[] split = file.getPath().split(":"); //split the path.
            String pathToMusic = split[1]; //assign it to a string(your choice).
            //String pathToMusic = audio.getPath();
            //String pathToMusic = "Download/sedative-110241.mp3";
            Log.d("pathToMusic", pathToMusic);

            try {
                serviceIntent.putExtra("OwnFilePath", pathToMusic);
                serviceIntent.putExtra("ownAudio", true);
                startService(serviceIntent);
            }
            catch (Exception e) {
                e.getMessage();
            }
        }



        /*MediaPlayer player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            //player.setDataSource(new FileInputStream(new File(audio.getPath())).getFD());
            player.setDataSource(audio.getPath());
            Log.d("audioPath", audio.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                player.start();

            }
        });

        player.prepareAsync();
        if(player.isPlaying())
            player.stop();*/

    }

    // Verifica permisos para acceder a archivos del dispositivo (necesario para API 23+)
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}