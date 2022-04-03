package com.ultimapieza.puzzledroid;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    int score;
    int numOfPieces;

    private static final int COLUMNS = 3;
    private static final int DIMENSIONS = COLUMNS * COLUMNS;
    private static final int SELECT_PHOTO = 100;

    ImageView imageView;

    private String[] tileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //Recibe los valores de score y numOfPieces
        numOfPieces = getIntent().getIntExtra("NUMOFPIECES", 3);
        score = getIntent().getIntExtra("SCORE", 0);

        init();

        scramble();

        display();

        Button selectButton = findViewById(R.id.selectBtn);
        Button backButton = findViewById(R.id.backBtn);

        // Cambiamos el color de los botones
        backButton.setBackgroundColor(Color.parseColor("#16C282"));

        // When clicking on SelectBtn, go to Gallery
        selectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), GalleryActivity.class);
                intent.putExtra("SCORE", score);
                intent.putExtra("NUMOFPIECES", numOfPieces);
                v.getContext().startActivity(intent);
            }
        });

        // When clicking on backBtn, go back to Menu
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }

    // Function to open Device Folder and search for photos
    public void openCamera (View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivity(intent);
    }

    // Select the photo and display it on the ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {


                Uri selectImage = data.getData();
                InputStream inputStream = null;
                try {
                    assert selectImage != null;
                    inputStream = getContentResolver().openInputStream(selectImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                BitmapFactory.decodeStream(inputStream);
                imageView.setImageURI(selectImage);
            }
        }
    }

    private void display() {
        //TODO: ACCESS LIBRARY PHOTOS FROM MOBILE PHONE
    }

    private void init() {
        tileList = new String[DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++) {
            tileList[i] = String.valueOf(i);
        }
    }

    private void scramble() {
        int index;
        String temp;
        Random random = new Random();

        for (int i = tileList.length - 1; i < 0; i--) {
            index = random.nextInt(i + 1);
            temp = tileList[index];
            tileList[index] = tileList[i];
            tileList[i] = temp;
        }
    }
}