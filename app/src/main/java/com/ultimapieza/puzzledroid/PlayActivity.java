package com.ultimapieza.puzzledroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {

    int score;
    String userName;
    int numOfPieces;

    ImageView imageView;

    private String[] tileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Get values of score, userName and numOfPieces
        numOfPieces = getIntent().getIntExtra("NUMOFPIECES", 3);
        score = getIntent().getIntExtra("SCORE", 0);
        userName = getIntent().getStringExtra("USERNAME");


        Button selectButton = findViewById(R.id.selectBtn);
        Button backButton = findViewById(R.id.backBtn);

        // Change button color
        backButton.setBackgroundColor(Color.parseColor("#16C282"));

        // When clicking on SelectBtn, go to Gallery
        selectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), GalleryActivity.class);
                intent.putExtra("SCORE", score);
                intent.putExtra("USERNAME", userName);
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

    // Open Device Folder and search for photos
    public void openCamera (View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivity(intent);
    }

}