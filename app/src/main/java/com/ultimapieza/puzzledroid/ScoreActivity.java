package com.ultimapieza.puzzledroid;

import android.os.Bundle;

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

        listPlayer= findViewById(R.id.listPlayer);
        listPlayer.setLayoutManager(new LinearLayoutManager(this));

        DbNewPlayer dbNewPlayer=new DbNewPlayer(ScoreActivity.this);

        //listArrayPlayers=new ArrayList<>();
        ListPlayersAdapter adapters=new ListPlayersAdapter(dbNewPlayer.mostrarPlayers());
        listPlayer.setAdapter(adapters);


    }

}