package com.ultimapieza.puzzledroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class Location extends AppCompatActivity {

    Button location;
    TextView tvUbicacion;
    String locationCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recibimos los datos de localización de MainActivity
        Intent intent = getIntent();
        locationCoords = intent.getStringExtra("LOCATION");

        // Asignamos el botón y textView
        setContentView(R.layout.activity_location);
        location=findViewById(R.id.location);
        tvUbicacion=findViewById(R.id.tvUbicacion);

        // Establecemos los datos de localización en el TextView
        tvUbicacion.setText(locationCoords);
    }
}