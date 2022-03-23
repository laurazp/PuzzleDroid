package com.ultimapieza.puzzledroid;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ultimapieza.puzzledroid.db.DbHelper;

public class MainActivity extends AppCompatActivity {

//Bundle pasa información desde una actividad a otra
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playButton = findViewById(R.id.playBtn);
        Button optionsButton = findViewById(R.id.optionsBtn);
        Button scoreButton = findViewById(R.id.scoreBtn);
        Button aboutButton = findViewById(R.id.aboutBtn);

        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Play button clicked! ", Toast.LENGTH_LONG).show(); //Aparece un mensaje en pantalla
                // TODO: Modificar acción al hacer click
                Intent intent = new Intent (v.getContext(), PlayActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO: Modificar acción al hacer click
            }
        });

        scoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO: Modificar acción al hacer click
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO: Modificar acción al hacer click
                DbHelper dbHelper = new DbHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
            }
        });
    }

}