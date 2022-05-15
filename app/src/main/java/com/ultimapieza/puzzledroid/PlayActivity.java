package com.ultimapieza.puzzledroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayActivity extends AppCompatActivity {

    int score;
    String userName;
    int numOfPieces;
    // Define buttons with Butterknife
    @BindView(R.id.selectBtn) Button selectButton;
    @BindView(R.id.backBtn) Button backButton;
    private String[] tileList;
    /*ActivityResultLauncher<Intent> startForResult=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result !=null && result.getResultCode()==RESULT_OK){




            }
        }
    });*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        // Get values of score, userName and numOfPieces
        numOfPieces = getIntent().getIntExtra("NUMOFPIECES", 3);
        score = getIntent().getIntExtra("SCORE", 0);
        userName = getIntent().getStringExtra("USERNAME");


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

    // Starts PuzzleActivity and puts a value of 1 in "CAMERA"
    public void openCamera (View view) {

        Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("NUMOFPIECES", numOfPieces);
        intent.putExtra("CAMERA", 1);
        //intent.putExtra("ownPhotos", true);
        startActivity(intent);

        //Intent intent = new Intent(Intent.ACTION_PICK);
        //intent.setType("image/*");
        //startActivity(intent);
    }

}