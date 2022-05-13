package com.ultimapieza.puzzledroid;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;



public class Location extends AppCompatActivity {

    Button location;
    TextView tvUbicacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        location=findViewById(R.id.location);
        tvUbicacion=findViewById(R.id.tvUbicacion);

    }
}