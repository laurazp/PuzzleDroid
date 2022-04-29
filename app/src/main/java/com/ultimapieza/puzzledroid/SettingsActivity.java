package com.ultimapieza.puzzledroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    Switch switch1, switch2;
    boolean stateSwitch1, stateSwitch2;
    MediaPlayer mediaPlayer;

    String filePath = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3";
    Intent serviceIntent;
    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button backButton = findViewById(R.id.backBtn);
        Button buttonSelect = findViewById(R.id.buttonSelect);

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
        // boton entra para seleccionar mp3
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                Intent intentMusic = new Intent(Intent.ACTION_GET_CONTENT);
                intentMusic.setType("audio/*");
                startActivityForResult(intentMusic, 1);

                MediaPlayer player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

        });
        MediaPlayer player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        File audio = null;
        try {
            player.setDataSource(new FileInputStream(new File(audio.getPath())).getFD());
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
            player.stop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri audio = data.getData(); //declared above Uri audio;
            Log.d("media", "onActivityResult: "+audio);

        }

        super.onActivityResult(requestCode, resultCode, data);


    }

}
