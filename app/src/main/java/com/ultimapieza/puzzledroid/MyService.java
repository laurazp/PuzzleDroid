package com.ultimapieza.puzzledroid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

// Servicio para gestionar la música y que suene a lo largo de todas las Activities
public class MyService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private MediaPlayer reproductor;
    String filePath;
    boolean ownAudio;

    // Atributos para controlar llamada entrante
    private boolean isPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Definimos un reproductor y los métodos que va a necesitar
        reproductor = new MediaPlayer();

        reproductor.setOnCompletionListener(this);
        reproductor.setOnErrorListener(this);
        reproductor.setOnInfoListener(this);
        reproductor.setOnBufferingUpdateListener(this);
        reproductor.setOnPreparedListener(this);
        reproductor.setOnSeekCompleteListener(this);
        //reproductor.reset();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Revisa si viene de elegir audio del propio móvil
        ownAudio = intent.getBooleanExtra("ownAudio", false);

        // Manage incoming calls during playback (pause/resume MediaPlayer)
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);  // Get the telephony manager
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if(reproductor != null) {
                            if (reproductor.isPlaying()) {
                                Log.d("MusicService","Pausando reproductor...");
                                pauseMedia();
                                isPausedInCall = true;
                            }
                        }
                        //stopSelf();
                        //stopMyService();
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (reproductor != null) {
                            if (isPausedInCall) {
                                Log.d("MusicService","Reanudando reproductor...");
                                reproductor.prepareAsync();
                                isPausedInCall = false;
                            }
                            //playAudio();
                        }
                        break;
                }
            }
        };

        // Register the listener with the telephony manager
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        if(ownAudio) {
            filePath = intent.getStringExtra("OwnFilePath");
            Log.d("Path to music is ", String.valueOf(filePath));
            reproductor.reset();
        }
        else {
            // Recibe el path del archivo de música
            try {
                filePath = intent.getStringExtra("FilePath");
                Log.d("Path to music is ", String.valueOf(filePath));
                reproductor.reset();
            }
            catch (Exception e) {
                Log.d("Error en path de música", e.getMessage());
            }
        }


        // Pone en marcha el reproductor de manera asíncrona
        //if (!reproductor.isPlaying()) {
        if(reproductor!=null) {
            Log.d("reproductor", "reproductor done");
            try {
                if(reproductor.isPlaying()) {
                    reproductor.stop();
                    reproductor.release();
                    Log.d("reproductor", "reproductor released");
                }

                if(ownAudio) {
                    Log.d("reproductor", "dentro de if(ownAudio)");
                    reproductor.setDataSource(filePath);
                    //reproductor.setDataSource(new FileInputStream(new File(filePath)).getFD());
                    //reproductor.setDataSource("/storage/emulated/0/Download/sedative-110241.mp3");

                    //String destination = Environment.getExternalStorageDirectory().getPath() + File.separator;
                    //Log.d("destination", destination);

                }
                else {
                    Log.d("reproductor", "dentro de else (ownAudio)");
                    reproductor.setDataSource(filePath);
                }

                //reproductor.setDataSource(filePath);
                //Log.d("audioPath", filePath);
                Log.d("reproductor", "setDataSource done");
                reproductor.setOnPreparedListener(this);
                Log.d("reproductor", "setOnPrepared done");
                reproductor.prepareAsync();
                Log.d("reproductor", "prepareAsync done");
            } catch (Exception e) {
                Toast.makeText(this, "Error while preparing music to play: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Error", String.valueOf(e));
            }
        }
        // Si el servicio es destruido por baja memoria, Android reinicia el servicio
        return START_STICKY;
    }


    // Detiene el reproductor y libera los recursos
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(reproductor != null){
            reproductor.stop();
        }
        reproductor.reset();
        reproductor.release();
        reproductor = null;
        //isPausedInCall = true;

        // Destroy the phoneStateListener
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mediaPlayer.isPlaying()) {
            //mediaPlayer.stop();
        }
        stopSelf();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

        switch(i) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Toast.makeText(this, "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Toast.makeText(this, "MEDIA ERROR SERVER DIED", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Toast.makeText(this, "MEDIA ERROR UNKNOWN", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        //mediaPlayer.setLooping(true);
        //mediaPlayer.setVolume(1, 1);
        mediaPlayer.start();
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    public void pauseMedia() {
        if (reproductor != null) {
            if (reproductor.isPlaying()) {
                reproductor.stop();
            }
        }
    }


}