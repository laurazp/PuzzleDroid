/*package com.ultimapieza.puzzledroid.entidades;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ultimapieza.puzzledroid.MainActivity;
import com.ultimapieza.puzzledroid.R;

public class Sounds extends AppCompatActivity implements View.OnClickListener
{
    Button sonido1, sonido2, sonido3;

    private MediaPlayer
    sound1,
    sound2,
    sound3;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sonido1 = MediaPlayer.create(MainActivity.this, R.raw.sounduno);
        sonido2 = MediaPlayer.create(MainActivity.this, R.raw.sounddos);
        sonido3 = MediaPlayer.create(MainActivity.this, R.raw.soundtres);

        sonido1 = (Button) findViewById(R.id.playBtn);
        sonido1.setOnClickListener(this);
        sonido2 = (Button) findViewById(R.id.optionsBtn);
        sonido2.setOnClickListener(this);
        sonido3 = (Button) findViewById(R.id.scoreBtn);
        sonido3.setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.playBtn:
                sonido1.start();
                break;
        }
        switch (view.getId()){
            case R.id.optionsBtn:
                sonido2.start();
                break;
        }
        switch (view.getId()){
            case R.id.scoreBtn:
                sonido3.start();
                break;
        }
    }
}

 */
