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
import com.ultimapieza.puzzledroid.entidades.Players;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    RecyclerView listPlayer;
    ArrayList<Players> listArrayPlayers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Button galleryButton = findViewById(R.id.galleryButton);
        Button exitButton = findViewById(R.id.exitButton);

        listPlayer= findViewById(R.id.listPlayer);
        listPlayer.setLayoutManager(new LinearLayoutManager(this));

        DbNewPlayer dbNewPlayer=new DbNewPlayer(ScoreActivity.this);

        //listArrayPlayers=new ArrayList<>();
        ListPlayersAdapter adapters=new ListPlayersAdapter(dbNewPlayer.mostrarPlayers());
        adapters.sortItems();
        listPlayer.setAdapter(adapters);

        galleryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), GalleryActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: COMPROBAR SI FUNCIONA BIEN!
                System.exit(0);
            }
        });

    }

}