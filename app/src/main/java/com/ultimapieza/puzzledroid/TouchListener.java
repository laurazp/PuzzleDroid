package com.ultimapieza.puzzledroid;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class TouchListener implements View.OnTouchListener {
    private float xDelta;
    private float yDelta;
    private PuzzleActivity activity;
    MediaPlayer mediaPlayer;
    PuzzlePiece piece;

    // Atributos para las animaciones
    private ObjectAnimator animatorAlpha;
    private long animationDuration = 1000;
    private AnimatorSet animatorSet; // Reproduce un conjunto de ObjectAnimator en un orden especificado


    public TouchListener(PuzzleActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();
        final double tolerance = sqrt(pow(view.getWidth(), 2) + pow(view.getHeight(), 2)) / 10;

        // Play a sound when touching the puzzle pieces
        mediaPlayer = MediaPlayer.create(activity, R.raw.click);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(mediaPlayer!=null) {
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            };
        });

        piece = (PuzzlePiece) view;
        if (!piece.canMove) {
            return true;
        }


        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                piece.bringToFront();
                break;
            case MotionEvent.ACTION_MOVE:
                lParams.leftMargin = (int) (x - xDelta);
                lParams.topMargin = (int) (y - yDelta);
                view.setLayoutParams(lParams);
                break;
            case MotionEvent.ACTION_UP:
                int xDiff = abs(piece.xCoord - lParams.leftMargin);
                int yDiff = abs(piece.yCoord - lParams.topMargin);
                if (xDiff <= tolerance && yDiff <= tolerance) {
                    lParams.leftMargin = piece.xCoord;
                    lParams.topMargin = piece.yCoord;
                    piece.setLayoutParams(lParams);

                    // Add animation
                    animacion();

                    piece.canMove = false;
                    sendViewToBack(piece);
                    //Calling checkGameOver function
                    activity.checkGameOver();
                }
                break;
        }
        return true;
    }

    public void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    // Animación
    private void animacion() {
        animatorAlpha = ObjectAnimator.ofFloat(piece, View.ALPHA, 0.2f, 1.0f);
        animatorAlpha.setDuration(animationDuration);
        AnimatorSet animatorSetAlpha = new AnimatorSet();
        animatorSetAlpha.play(animatorAlpha);
        animatorSetAlpha.start();
    }

}