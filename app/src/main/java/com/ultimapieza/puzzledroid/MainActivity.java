package com.ultimapieza.puzzledroid;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        Button helpButton = findViewById(R.id.helpBtn);

        playButton.setBackgroundColor(Color.parseColor("#F7C52C"));
        optionsButton.setBackgroundColor(Color.parseColor("#16C282"));

        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), PlayActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent (v.getContext(), SettingsActivity.class);
                v.getContext().startActivity(intent2);
                // TODO: Modificar acción al hacer click
            }
        });

        scoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO: Modificar acción al hacer click para que cargue la pantalla de ScoreTable
                DbHelper dbHelper = new DbHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                if (db != null) {
                    Toast.makeText(MainActivity.this, "DATABASE SUCCESFULLY CREATED", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "ERROR CREATING THE DATABASE", Toast.LENGTH_LONG).show();

                }
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO: Modificar acción al hacer click
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), HelpActivity.class);
                v.getContext().startActivity(intent);
                // TODO: Modificar acción al hacer click
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_info:
                Toast.makeText(this, "Info Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_help:
                Toast.makeText(this, "Help Selected", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}