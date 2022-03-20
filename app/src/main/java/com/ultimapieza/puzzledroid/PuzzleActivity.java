package com.ultimapieza.puzzledroid;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class PuzzleActivity extends AppCompatActivity {

    ArrayList<Bitmap> pieces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        final ConstraintLayout layout = findViewById(R.id.layout);
        ImageView imageView = findViewById(R.id.imageView);

        // run image related code after the view was laid out
        // to have all dimensions calculated
        imageView.post(new Runnable() {
            @Override
            public void run() {
                pieces = splitImage();
                for(Bitmap piece : pieces) {
                    ImageView iv = new ImageView(getApplicationContext());
                    iv.setImageBitmap(piece);
                    layout.addView(iv);
                }
            }
        });
    }

    private ArrayList<Bitmap> splitImage() {
        int piecesNumber = 12;
        int rows = 4;
        int cols = 3;

        ImageView imageView = findViewById(R.id.imageView);
        ArrayList<Bitmap> pieces = new ArrayList<>(piecesNumber);

        // Get the bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        // Calculate the with and height of the pieces
        int pieceWidth = bitmap.getWidth()/cols;
        int pieceHeight = bitmap.getHeight()/rows;

        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                pieces.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, pieceWidth, pieceHeight));
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }

        return pieces;
    }

}