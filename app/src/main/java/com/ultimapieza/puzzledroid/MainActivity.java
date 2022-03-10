package com.ultimapieza.puzzledroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playButton = findViewById(R.id.playBtn);
        playButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // TODO: Modificar acción al hacer click
                Toast.makeText(getApplicationContext(), "Play button clicked!", Toast.LENGTH_LONG).show();
            }

        });
    }
}