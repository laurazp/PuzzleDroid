package com.ultimapieza.puzzledroid;
import com.ultimapieza.puzzledroid.adaptadores.ListPlayersAdapter;
import com.ultimapieza.puzzledroid.db.DbHelperNewPlayer;
import com.ultimapieza.puzzledroid.entidades.Players;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ultimapieza.puzzledroid.db.DbNewPlayer;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    int score;
    int numOfPieces;
    DbHelperNewPlayer obj;
    RecyclerView listPlayer;
    ArrayList<Players> listArrayPlayers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);
        TextView name =findViewById(R.id.viewName);
        TextView score_p = findViewById(R.id.viewScore);

        Button scoretableBtn = findViewById(R.id.scoretableBtn);

        //Recibe el valor de score
        score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");
        SharedPreferences settings = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            highScoreLabel.setText("High Score: " + score);

            // Update high score
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();

        } else {
            highScoreLabel.setText("High Score: " + highScore);
        }


        //Recibe el valor de numOfPieces
        numOfPieces = getIntent().getIntExtra("NUMOFPIECES", 3);

        // Definimos el botón de "See ScoreTable" y le cambiamos el color
        scoretableBtn.setBackgroundColor(Color.parseColor("#16C282"));

        // Al hacer click en el botón scoretableBtn, nos lleva a ScoreActivity
        scoretableBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: TERMINAR JUEGO AL DARLE A VER PUNTUACIÓN ???

                Intent intent = new Intent (v.getContext(), ScoreActivity.class);
                startActivity(intent);


            }
        });

    }


    /*public void playAgain(View view) {
        Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
        // Pasamos la puntuación a la clase Play para que siga sumando puntos
        intent.putExtra("SCORE", score);
        intent.putExtra("NUMOFPIECES", numOfPieces);
        startActivity(intent);
    }*/

}