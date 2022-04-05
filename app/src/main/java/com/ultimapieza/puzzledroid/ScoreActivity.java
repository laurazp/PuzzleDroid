package com.ultimapieza.puzzledroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ultimapieza.puzzledroid.adaptadores.ListPlayersAdapter;
import com.ultimapieza.puzzledroid.db.DbNewPlayer;


public class ScoreActivity extends AppCompatActivity {
    RecyclerView listPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Definimos los botones
        Button galleryButton = findViewById(R.id.galleryButton);
        Button exitButton = findViewById(R.id.exitButton);

        // Definimos la ReciclerView
        listPlayer= findViewById(R.id.listPlayer);
        listPlayer.setLayoutManager(new LinearLayoutManager(this));

        // Llamamos al método mostrarPlayers para que se muestren en la ReciclerView
        DbNewPlayer dbNewPlayer=new DbNewPlayer(ScoreActivity.this);

        ListPlayersAdapter adapters=new ListPlayersAdapter(dbNewPlayer.mostrarPlayers());
        // Importante!! Notificamos que el orden de los datos en adapters ha cambiado
        adapters.notifyDataSetChanged();
        listPlayer.setAdapter(adapters);

        // Al hacer click en el botón "Play Again", vuelve a la activity de elegir imagen para el puzzle
        galleryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), GalleryActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        // Al hacer click en el botón "Exit", sale de la aplicación
        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (v.getContext(), InfoActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }
}