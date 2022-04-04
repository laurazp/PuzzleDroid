package com.ultimapieza.puzzledroid;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private MediaPlayer reproductor;
    String filePath;

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

        reproductor = new MediaPlayer();

        reproductor.setOnCompletionListener(this);
        reproductor.setOnErrorListener(this);
        reproductor.setOnInfoListener(this);
        reproductor.setOnBufferingUpdateListener(this);
        reproductor.setOnPreparedListener(this);
        reproductor.setOnSeekCompleteListener(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        filePath = intent.getStringExtra("FilePath");
        Log.d("Path to music is ", String.valueOf(filePath));
        reproductor.reset();

        if (!reproductor.isPlaying()) {
            try {
                reproductor.setDataSource(filePath);
                reproductor.prepareAsync();
            } catch (Exception e) {
                Toast.makeText(this, "Error while getting source: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(reproductor != null){
            reproductor.stop();
            reproductor.reset();
            reproductor.release();
            reproductor = null;
        }
    }

        /*if (reproductor != null) {
            if (reproductor.isPlaying()) {
                reproductor.stop();
            }
            reproductor.release();
        }*/

    //}

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
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
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(1, 1);
            mediaPlayer.start();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }
}