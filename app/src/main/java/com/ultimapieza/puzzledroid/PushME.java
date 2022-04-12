package com.ultimapieza.puzzledroid;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Color;
import android.os.Build;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.filament.View;

public class PushME extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;


    public void Notificaciones(){
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
             NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
             builder.setContentTitle("HAS REALIZADO UN RECORD");
             builder.setContentText("Has realizado un nuevo record, enhorabuena. Te quedas en el ranking");
             builder.setColor(Color.GREEN);
             builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);



    }

}
