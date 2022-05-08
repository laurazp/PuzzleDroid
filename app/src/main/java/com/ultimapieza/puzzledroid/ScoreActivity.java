package com.ultimapieza.puzzledroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ultimapieza.puzzledroid.adaptadores.ListPlayersAdapter;
import com.ultimapieza.puzzledroid.db.DbNewPlayer;
import com.ultimapieza.puzzledroid.entidades.Players;

import java.util.ArrayList;


public class ScoreActivity extends AppCompatActivity {
    RecyclerView listPlayer;
    //boolean ownPhotos;
    // creating a variable for
    // our Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;

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

        // Recibimos el valor de "ownPhotos"
        //Intent intent = getIntent();
        //ownPhotos = intent.getBooleanExtra("ownPhotos", false);

        /*Llamamos al método mostrarPlayers para que se muestren en la ReciclerView
        DbNewPlayer dbNewPlayer=new DbNewPlayer(ScoreActivity.this);

        ListPlayersAdapter adapters=new ListPlayersAdapter(dbNewPlayer.mostrarPlayers());
        /Importante!! Notificamos que el orden de los datos en adapters ha cambiado
        adapters.notifyDataSetChanged();*/

        //listPlayer.setAdapter(adapters);

        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("Data");

        // initializing our object class variable.

        // calling method
        // for getting data.



        // Al hacer click en el botón "Play Again", vuelve a la activity de elegir imagen para el puzzle
        galleryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Check if last image was chosen from user's gallery or static images
                /*Log.d("OwnPhotos boolean es ", String.valueOf(ownPhotos));
                if (ownPhotos) {
                    // Go to PuzzleActivity and Display image randomly from user's photo gallery
                    Intent intent = new Intent (v.getContext(), PuzzleActivity.class);
                    Log.d("OwnPhotos boolean es ", String.valueOf(ownPhotos));
                    intent.putExtra("ownPhotos", ownPhotos);
                    v.getContext().startActivity(intent);
                }
                else {*/
                    Intent intent = new Intent (v.getContext(), GalleryActivity.class);
                    v.getContext().startActivity(intent);
                //}
            }
        });
        ArrayList<String>playerScores=new ArrayList<>();
        //creando el adapter que mostrará los datos de los jugadores
        ArrayList<Players> listPlayers = new ArrayList<>();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.list_item_player,playerScores); ;
        //listPlayer.setAdapter(adapter1);


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