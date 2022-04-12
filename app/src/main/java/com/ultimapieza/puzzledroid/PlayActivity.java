package com.ultimapieza.puzzledroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
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

        // Define buttons
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

    // Una vez seleccionada la foto o hecha la foto con la cámara, vuelve a la PlayActivity con la foto seleccionada
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        //imageView.setImageBitmap(selectedImage);

//                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//                        Uri tempUri = getImageUri(getApplicationContext(), selectedImage);
//
//                        // CALL THIS METHOD TO GET THE ACTUAL PATH
//                        File finalFile = new File(getRealPathFromURI(tempUri));

                        // TODO: PRUEBAS PARA ABRIR INTENT PUZZLE CON IMAGEN SELECCIONADA
                        Intent intent = new Intent (getApplicationContext(), PuzzleActivity.class);
                        intent.putExtra("image", selectedImage);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        // Fin pruebas

                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                //TODO: ABRIR INTENT PUZZLE ??
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    // Show a menu to choose between camera or photo gallery
    public void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery", "Cancel" };

        // Open the menu as an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                    startActivity(takePicture);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                    startActivity(pickPhoto);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    // Calls selectImage() to show a menu (to Open Device Folder or Camera)
    public void openCamera (View view) {

        selectImage(this);

        //Intent intent = new Intent(Intent.ACTION_PICK);
        //intent.setType("image/*");
        //startActivity(intent);
    }

//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
//        return Uri.parse(path);
//    }
//
//    public String getRealPathFromURI(Uri uri) {
//        String path = "";
//        if (getContentResolver() != null) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                path = cursor.getString(idx);
//                cursor.close();
//            }
//        }
//        return path;
//    }

}