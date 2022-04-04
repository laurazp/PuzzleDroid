package com.ultimapieza.puzzledroid;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ultimapieza.puzzledroid.db.DbHelperNewPlayer;
import com.ultimapieza.puzzledroid.db.DbNewPlayer;
import com.ultimapieza.puzzledroid.entidades.Players;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    String userName;
    int score;
    int numOfPieces;
    DbHelperNewPlayer obj;
    RecyclerView listPlayer;
    ArrayList<Players> listArrayPlayers;
    DbNewPlayer db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);
        TextView name =findViewById(R.id.viewName);
        TextView score_p = findViewById(R.id.viewScore);

        db = new DbNewPlayer(this);

        Button scoretableBtn = findViewById(R.id.scoretableBtn);

        //Recibe el valor de score y userName
        score = getIntent().getIntExtra("SCORE", 0);
        userName = getIntent().getStringExtra("USERNAME");
        Log.d("Score antes del botón", String.valueOf(score));
        Log.d("Nombre antes del botón", String.valueOf(userName));
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
                //TODO: METER SCORE EN LA BASE DE DATOS
                int finalScore = score;
                Log.d("Score después del botón", String.valueOf(finalScore));
                Log.d("Nombre después de botón", String.valueOf(userName));


                //TODO: LLAMAR A UPDATE PARA ACTUALIZAR DATOS DE SCORE
                db.updatePlayer(userName, finalScore);

                Intent intent = new Intent (v.getContext(), ScoreActivity.class);
                startActivity(intent);


            }
        });

    }

    public int getFinalScore() {
        int finalScore = score;
        return finalScore;
    }


    /*public void playAgain(View view) {
        Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
        // Pasamos la puntuación a la clase Play para que siga sumando puntos
        intent.putExtra("SCORE", score);
        intent.putExtra("NUMOFPIECES", numOfPieces);
        startActivity(intent);
    }*/

}