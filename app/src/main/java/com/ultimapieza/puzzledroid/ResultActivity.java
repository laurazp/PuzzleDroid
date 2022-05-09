package com.ultimapieza.puzzledroid;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ultimapieza.puzzledroid.db.DbHelperNewPlayer;
import com.ultimapieza.puzzledroid.db.DbNewPlayer;
import com.ultimapieza.puzzledroid.entidades.Players;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    String userName;
    int score;
    int numOfPieces;
    ImageView imageView;

    // Atributos para las animaciones
    private ObjectAnimator animatorRotation;
    private long animationDuration = 1000;
    private AnimatorSet animatorSet; // Reproduce un conjunto de ObjectAnimator en un orden especificado

    DbHelperNewPlayer obj;
    RecyclerView listPlayer;
    ArrayList<Players> listArrayPlayers;
    DbNewPlayer db;
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    DatabaseReference databaseReference;

    boolean ownPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);
        TextView name = findViewById(R.id.viewName);
        TextView score_p = findViewById(R.id.viewScore);
        Button addcalendarButton=findViewById(R.id.addcalendarButton);
        imageView = findViewById(R.id.imageView);

        // Ejecutar animación
        imageView.setImageResource(R.drawable.trophy);
        animacion();

        db = new DbNewPlayer(this);

        // Recibimos el valor de "ownPhotos"
        Intent intent = getIntent();
        ownPhotos = intent.getBooleanExtra("ownPhotos", false);

        Button scoretableBtn = findViewById(R.id.scoretableBtn);

        //Recibe el valor de score y userName
        score = getIntent().getIntExtra("SCORE", 0);
        userName = getIntent().getStringExtra("USERNAME");

        // Muestra en pantalla el resultado de score
        scoreLabel.setText(score + "");
        // Comprueba si la puntuación es la más alta, la guarda como High Score y lo muestra en pantalla
        SharedPreferences settings = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);
        
        //Obtenemos la instancia de la base de datos
        databaseReference= FirebaseDatabase.getInstance().getReference();
        //Referenciamos a un padre donde debajo irán los datos
        DatabaseReference playerReference = databaseReference.child("Players");
        // casteamos a string el score
        String stringScore=String.valueOf(score);
        //asignamos los valores a la base de datos
        playerReference.child(userName).setValue(stringScore);

         if (score > highScore) {
            highScoreLabel.setText("High Score: " + score);

            // Update high score
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
            //Integrar aqui parte del codigo para notificacion push
            createNotificationChannel();
            createNotification();

            //Enlazar con clase PushME;

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
                //Mete la puntuación en la base de datos
                int finalScore = score;
                //Log.d("Score después del botón", String.valueOf(finalScore));
                Log.d("Nombre después de botón", String.valueOf(userName));

                //Llama a la función updatePlayer para actualizar el registro de la base de datos y añadir la puntuación
                db.updatePlayer(userName, finalScore);

                Intent intent = new Intent (v.getContext(), ScoreActivity.class);
                startActivity(intent);
            }
        });

        addcalendarButton.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v) {
                 //Intent intent = new Intent (v.getContext(), Calendar.class);
                // en el titulo o en la descripción hay que poner la puntuación
                 Intent intent = new Intent(Intent.ACTION_EDIT)
                 .setData(CalendarContract.Events.CONTENT_URI)
                 .putExtra(CalendarContract.Events.TITLE," My Score" +" "+ String.valueOf(score));
                 //.putExtra(CalendarContract.Events.EVENT_LOCATION," Anywhere")
                 //.putExtra(CalendarContract.Events.DESCRIPTION,String.valueOf(score));
                 //.putExtra(CalendarContract.Events.ALL_DAY," true");
                 //en caso de que queramos añadir contactos al evento
                 //.putExtra(Intent.EXTRA_EMAIL, " player@gmail.com, player1@gmail.com" );
                 startActivity(intent);

                 //con el siguiente if comprobamos si el calendario puede añadir los cambios
                 if(intent.resolveActivity(getPackageManager())!=null){
                     startActivity(intent);
                 }else{
                     Toast.makeText(ResultActivity.this,"There is not app than can support this action",
                             Toast.LENGTH_SHORT).show();
                 }


             }

             //v.getContext().startActivity(intent);
        });

    }


    //TODO: CONFIRMAR QUE NO SE USA Y BORRAR
    public int getFinalScore() {
        int finalScore = score;
        return finalScore;
    }

    // Método para seguir jugando y aumentando la puntuación
    public void playAgain(View view) {

        if (ownPhotos) {
            // Go to PuzzleActivity and Display image randomly from user's photo gallery
            Intent intent = new Intent (view.getContext(), PuzzleActivity.class);
            intent.putExtra("ownPhotos", ownPhotos);

            // Pasamos la puntuación a la clase Puzzle para que siga sumando puntos
            intent.putExtra("SCORE", score);
            intent.putExtra("USERNAME", userName);
            intent.putExtra("NUMOFPIECES", numOfPieces);
            view.getContext().startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
            // Pasamos la puntuación a la clase Play para que siga sumando puntos
            intent.putExtra("SCORE", score);
            intent.putExtra("USERNAME", userName);
            intent.putExtra("NUMOFPIECES", numOfPieces);
            startActivity(intent);
        }

    }


    //pushME


    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }

    }

    public void createNotification() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ResultActivity.this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setContentTitle("HAS REALIZADO UN RECORD");
        builder.setContentText("Has realizado un nuevo record, enhorabuena. Te quedas en el ranking");
        builder.setColor(Color.GREEN);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ResultActivity.this);
        notificationManager.notify(1, builder.build());

    }

    // Animación
    private void animacion() {
        animatorRotation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animatorRotation.setDuration(animationDuration);
        AnimatorSet animatorSetRotation = new AnimatorSet();
        animatorSetRotation.play(animatorRotation);
        animatorSetRotation.start();
    }


}