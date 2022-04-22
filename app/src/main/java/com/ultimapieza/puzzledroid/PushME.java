package com.ultimapieza.puzzledroid;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.filament.View;

public class PushME extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;




    private void notifications(){
        createNotificationChannel();
        createNotification();

    }


         private void createNotificationChannel(){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                CharSequence name = "Notificacion";
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);

              }

         }

         private void createNotification() {
             Intent intenta = new Intent(this, PushME.class);
             intenta.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intenta, PendingIntent.FLAG_IMMUTABLE);

             NotificationCompat.Builder builder = new NotificationCompat.Builder(PushME.this, CHANNEL_ID);
             builder.setSmallIcon(R.drawable.notification_icon);
             builder.setContentTitle("HAS REALIZADO UN RECORD");
             builder.setContentText("Has realizado un nuevo record, enhorabuena. Te quedas en el ranking");
             builder.setColor(Color.GREEN);
             builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
             builder.setContentIntent(pendingIntent);
             builder.setAutoCancel(true);

             NotificationManagerCompat notificationManager = NotificationManagerCompat.from(PushME.this);
             notificationManager.notify(1, builder.build());

         }

}
