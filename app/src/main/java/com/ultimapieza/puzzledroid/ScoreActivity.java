package com.ultimapieza.puzzledroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ScoreActivity extends AppCompatActivity {
    ListView listPlayer;
    TextView scorePlayerView;
    //boolean ownPhotos;
    // creating a variable for
    // our Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;

    // Variables para el RecyclerView usando Retrofit
    //RecyclerView recyclerView;
    //List<Players> userListResponseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        //setContentView(R.layout.activity_score_retrofit); //---------- Comentado para trabajar con Retrofit ------
        //userListResponseData = new ArrayList<Players>();

        // Definimos el RecyclerView para trabajar con Retrofit
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //getUserListData(); // call a method in which we have implement our GET type web API

        // Definimos los botones
        Button galleryButton = findViewById(R.id.galleryButton);
        Button exitButton = findViewById(R.id.exitButton);

        // Definimos la ReciclerView
        listPlayer= findViewById(R.id.listPlayer);
        scorePlayerView=findViewById(R.id.scorePlayerView);


        // Recibimos el valor de "ownPhotos"
        //Intent intent = getIntent();
        //ownPhotos = intent.getBooleanExtra("ownPhotos", false);

        //Llamamos al método mostrarPlayers para que se muestren en la ReciclerView
        //DbNewPlayer dbNewPlayer=new DbNewPlayer(ScoreActivity.this);

        //ListPlayersAdapter adapters=new ListPlayersAdapter(dbNewPlayer.mostrarPlayers());
        //Importante!! Notificamos que el orden de los datos en adapters ha cambiado
        //adapters.notifyDataSetChanged();

        //listPlayer.setAdapter(adapters);

        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("Players");

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

        final ArrayList<String> playerScores=new ArrayList<>();
        //creando el adapter que mostrará los datos de los jugadores
        final ArrayAdapter adapterscore=new ArrayAdapter<String>(this,R.layout.list_item_player,R.id.viewName,playerScores);
        final ArrayAdapter adaptername=new ArrayAdapter<String>(this,R.layout.list_item_player,R.id.viewScore,playerScores);
        //asignamos los adapter
        listPlayer.setAdapter(adapterscore);
        listPlayer.setAdapter(adaptername);
        //guardamos la referncia en una variable
        DatabaseReference reference=databaseReference;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //limpiamos la lista
                playerScores.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    //debido a que tenemos dos txtview uno supone la clave y el otro el valor
                    playerScores.add(String.valueOf(dataSnapshot.getKey()));
                    playerScores.add(dataSnapshot.getValue().toString());
                }
                //nos notifica si hay algún cambio
                adaptername.notifyDataSetChanged();
                adapterscore.notifyDataSetChanged();
            }
            //TODO el score tiene que ser de los 10 mejores. Meter al menos 15 datos y que coja clo 10 con el mejor score

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    // Métodos para trabajar con Retrofit
    //---------- Comentado para trabajar con Retrofit ------------
    /*private void getUserListData() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(ScoreActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        (Api.getClient().getUsersList()).enqueue(new Callback<List<Players>>() {

            @Override
            public void onResponse(Call<List<Players>> call, Response<List<Players>> response) {
                Log.d("responseGET", response.body().get(0).getName());
                progressDialog.dismiss(); //dismiss progress dialog
                userListResponseData = response.body();
                setDataInRecyclerView();
            }

            @Override
            public void onFailure(Call<List<Players>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(ScoreActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                Log.d("ERROR RETROFIT: ", t.toString());
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });
    }

    private void setDataInRecyclerView() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ScoreActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of UsersAdapter to send the reference and data to Adapter
        UsersAdapter usersAdapter = new UsersAdapter(ScoreActivity.this, userListResponseData);
        recyclerView.setAdapter(usersAdapter); // set the Adapter to RecyclerView
        usersAdapter.notifyDataSetChanged();
    }*/

}