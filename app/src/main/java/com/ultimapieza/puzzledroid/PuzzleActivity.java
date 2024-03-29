package com.ultimapieza.puzzledroid;

import static java.lang.Math.abs;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class PuzzleActivity extends AppCompatActivity {

    ArrayList<PuzzlePiece> pieces;
    private int score;
    String userName;
    int camera;
    int rows;
    int numOfPieces;
    boolean ownPhotos;
    String firstPhotoSelected;
    boolean photoUsed;
    ImageView imageView;
    Bitmap selectedImage;
    RelativeLayout layout;
    String path;
    View view;
    File randomPhoto;
    // Store image Uris in this ArrayList
    private ArrayList<Uri> imageUris;

    Handler handler = new Handler();

    protected int counter = 0;
    //private ImageView mImageView;
    private Bitmap currentBitmap = null;

    // Request code to pick images
    private static final int PICK_IMAGES_CODE =0;

    // Position of selected random image

    // Declaring a Timer
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    public int countPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PuzzleActivity.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);

        }



        layout = findViewById(R.id.layout);
        imageView = findViewById(R.id.imageView);

        // Recibe el nombre de la imagen elegida desde las imágenes estáticas de la app
        Intent intent = getIntent();
        final String assetName = intent.getStringExtra("assetName");
        final String mCurrentPhotoUri = intent.getStringExtra("mCurrentPhotoUri");
        HideFile hidePhotoUsed=new HideFile();

        ownPhotos = intent.getBooleanExtra("ownPhotos", false);
        photoUsed=intent.getBooleanExtra("photoUsed", false);

        //Recibe los valores de score, username, numOfPieces y camera
        numOfPieces = getIntent().getIntExtra("NUMOFPIECES", 3);
        score = getIntent().getIntExtra("SCORE", 0);
        userName = getIntent().getStringExtra("USERNAME");
        camera = getIntent().getIntExtra("CAMERA", 0);

        // Asigna el valor de numOfPieces a las filas del puzzle
        rows = numOfPieces;


        // Si se ha elegido seleccionar foto desde la propia cámara, se llama a selectImage() que lanza el menú de opciones de cámara
        if (camera == 1) {
            // Si es la primera vez que seleccionas desde tus fotos
            selectImage(this);
        }
        else {
            // Si vienes de darle al botón de PlayAgain
            if(ownPhotos) {
                writeStoragePermissionGranted();
                //Log.d("IF", "Ha entrado en el if(ownPhotos)!!");
                // Display image randomly from user's photo gallery

                // Crea un Array con los valores de las imágenes de la galería del usuario
                String[] projection = new String[]{
                        MediaStore.Images.Media.DATA,
                };

                Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                //creamos array para guardar las fotos que ya se usaron
                ArrayList<String> playerPhotosRepeated = new ArrayList<>();
                Cursor cur = getContentResolver().query(images,
                        projection,
                        null,
                        null,
                        null);
                final ArrayList<String> imagesPath = new ArrayList<>();
                //guardamos el size de los arrays en una variable
                int size_array_user=imagesPath.size();
                int size_array_user_photo_used=playerPhotosRepeated.size();
                if (cur.moveToFirst()) {

                    int dataColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.DATA);
                    // Añade las imágenes al Array
                    do {
                        imagesPath.add(cur.getString(dataColumn));

                    } while (cur.moveToNext());

                }
                if(size_array_user!=0 && size_array_user==size_array_user_photo_used){
                    intent=new Intent(getApplicationContext(),GalleryActivity.class);
                    view.getContext().startActivity(intent);

                }


                cur.close();
                final Random random = new Random();
                final int[] count = {imagesPath.size()};

                handler.post(new Runnable() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void run() {
                        //bucle que itera sibre las fotos del jugador
                        // Selecciona imagen aleatoriamente

                        int number = random.nextInt(count[0]);
                        //comprobamos que la foto no esté repetida
                        for(String userPhoto :  imagesPath){
                            randomPhoto=new File(userPhoto);
                            Log.d("dentro for",String.valueOf(randomPhoto));

                        }
                        //Una vez acabado el bucle miramos si la foto está repetida o no
                        if(!randomPhoto.isHidden()) {
                            Log.d("NO estoy escondido",String.valueOf(randomPhoto));
                            path=imagesPath.get(number);
                            hidePhotoUsed.setHiddenFile(randomPhoto);
                            playerPhotosRepeated.add(path);

                        }
                        else{
                            Log.d(" Estoy escondido",String.valueOf(randomPhoto));
                            // si está repetida pasará a la siguiente imagen
                            number=number+1;
                            path=imagesPath.get(number);

                        }


                        if (currentBitmap != null)
                        currentBitmap.recycle();
                        currentBitmap = BitmapFactory.decodeFile(path);
                        // Establece la foto aleatoria como imagen para el puzzle
                        imageView.setImageBitmap(currentBitmap);
                        //handler.postDelayed(this, 1000);
                        // Rompe la imagen en piezas
                        if (path != null ) {
                            setPicFromAsset(path, imageView);
                        }
                        // Split the image into pieces
                        //pieces = splitImage(numOfPieces + 1);
                        TouchListener touchListener;
                        touchListener = new TouchListener(PuzzleActivity.this);

                        // Shuffle pieces order
                        Collections.shuffle(pieces);
                        for (PuzzlePiece piece : pieces) {
                            piece.setOnTouchListener(touchListener);
                            layout.addView(piece);

                            // Randomize position, on the bottom of the screen
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                            lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                            lParams.topMargin = layout.getHeight() - piece.pieceHeight;
                            piece.setLayoutParams(lParams);

                        }
                        // en el caso de que el telefono tenga pocas fotos, y ya se hayan usado todas
                        //nos llevará otravez a la galeria

                    }

                });
            }
            else {
                // Run image related code after the view was laid out to have all dimensions calculated
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (assetName != null) {

                            Log.d("assetname", assetName);
                            //Bitmap bitmap = BitmapFactory.decodeFile(assetName);
                            Picasso.get().load(assetName).into(imageView);
                            //setPicFromAsset(assetName, imageView);
                            //imageView.setImageBitmap(bitmap);
                            pieces = splitImage((numOfPieces + 1));
                            /*File imgFile = new  File(assetName);
                            if(imgFile.exists()) {
                                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            }*/
                        }
                        /*if (mCurrentPhotoUri != null) {
                            setPicFromAsset(mCurrentPhotoUri, imageView);
                        }*/

                        // Split the image into pieces
                        //pieces = splitImage(numOfPieces + 1);
                        TouchListener touchListener;
                        touchListener = new TouchListener(PuzzleActivity.this);

                        // Shuffle pieces order
                        Collections.shuffle(pieces);
                        for(PuzzlePiece piece : pieces) {
                            piece.setOnTouchListener(touchListener);
                            layout.addView(piece);

                            // Randomize position, on the bottom of the screen
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                            lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                            lParams.topMargin = layout.getHeight() - piece.pieceHeight;
                            piece.setLayoutParams(lParams);
                        }
                    }
                });
            }
        }

        // Set the timer on
        timer = new Timer();
        startTimer();
    }


    // Una vez hecha la foto con la cámara o seleccionada de la galería, vuelve a la PlayActivity con esa foto
    // onActivityResult se activa después de los eventos de la cámara, porque se vuelve a la activity desde la que se llamaron
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        HideFile hidephoto=new HideFile();
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0: // TakePhoto
                    if (resultCode == RESULT_OK && data != null) {
                        selectedImage = (Bitmap) data.getExtras().get("data");
                        // Establece la foto hecha con la cámara como fondo del ImageView
                        imageView.setImageBitmap(selectedImage);

                        // Llama al método que parte la imagen en piezas
                        //pieces = splitImage(numOfPieces + 1);
                        TouchListener touchListener;
                        touchListener = new TouchListener(PuzzleActivity.this);

                        // Shuffle pieces order
                        Collections.shuffle(pieces);
                        for(PuzzlePiece piece : pieces) {
                            piece.setOnTouchListener(touchListener);
                            layout.addView(piece);

                            // Randomize position, on the bottom of the screen
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                            lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                            lParams.topMargin = layout.getHeight() - piece.pieceHeight;
                            piece.setLayoutParams(lParams);
                        }
                    }
                    break;
                case 1: //Choose from Gallery
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null )  {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                firstPhotoSelected= cursor.getString(columnIndex);
                                // adding id to photo
                                // Grant permissions
                                writeStoragePermissionGranted();

                                // Establece la foto seleccionada de la galería como fondo del ImageView
                                setPicFromPath(firstPhotoSelected, imageView);
                                //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                                try {
                                    pieces = splitImageUri((numOfPieces + 1), selectedImage, cursor);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                TouchListener touchListener;
                                touchListener = new TouchListener(PuzzleActivity.this);
                                ownPhotos = true;
                                Collections.shuffle(pieces);
                                for(PuzzlePiece piece : pieces) {
                                    piece.setOnTouchListener(touchListener);
                                    layout.addView(piece);

                                    // Randomize position, on the bottom of the screen
                                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                                    lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                                    lParams.topMargin = layout.getHeight() - piece.pieceHeight;
                                    piece.setLayoutParams(lParams);
                                }
                                File userFirtsPhotoSelected = new File(firstPhotoSelected);
                                hidephoto.setHiddenFile(userFirtsPhotoSelected);







                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }


    private void pickImagesIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image(s)"), PICK_IMAGES_CODE);
    }

    // Method to set the timer on
    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                    }
                });
            }
        };
        // The timer will count every second (1000 miliseconds)
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    // Checking if the game is finished
    public void checkGameOver() {
        if (isGameOver()) {

            // Muestra animación final del puzzle
            for(PuzzlePiece piece : pieces) {
                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(PuzzleActivity.this,
                        R.animator.property_animator);
                set.setTarget(piece);
                set.start();
            }

            // Calculate total time spent in finishing the puzzle
            int totalTime = getTime();
            Log.d("Total time spent ", String.valueOf(totalTime));  // Prints the time to the console

            // Calcula la puntuación obtenida y la suma a la anterior puntuación
            score += (1000 - (totalTime * 5));
            Log.d("SCORE is ", String.valueOf(score));  // Prints the score to the console

            // Retardar unos segundos el lanzamiento de la siguiente activity (para mostrar animación final)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Show ResultActivity and passes the values of score, userName and numOfPieces
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("SCORE", score);
                    intent.putExtra("USERNAME", userName);
                    intent.putExtra("NUMOFPIECES", numOfPieces + 1);
                    Log.d("ownPhotos al finalizar", String.valueOf(ownPhotos));
                    if (ownPhotos) {
                        intent.putExtra("ownPhotos", true);
                    }
                    startActivity(intent);
                    //PuzzleActivity.this.finish();
                }
            },1500);

/*            // Show ResultActivity and passes the values of score, userName and numOfPieces
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("SCORE", score);
            intent.putExtra("USERNAME", userName);
            intent.putExtra("NUMOFPIECES", numOfPieces + 1);
            Log.d("ownPhotos al finalizar", String.valueOf(ownPhotos));
            if (ownPhotos) {
                intent.putExtra("ownPhotos", true);
            }
            startActivity(intent);
*/            //finish();
        }
    }

    public int getScore(){
        int finalScore=0;
        if(isGameOver()){
            finalScore=score;

        }
        return finalScore;
    }

    // Calculate seconds from timer and round it
    private int getTime() {
        int rounded = (int) Math.round(time);

        return rounded;
    }

    // Boolean to check if the game is over
    private boolean isGameOver() {
        for (PuzzlePiece piece : pieces) {
            if (piece.canMove) {
                    return false;
            }
        }
        return true;
    }

    // Setting the selected image into the ImageView for the puzzle
    private void setPicFromAsset(String assetName, ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        AssetManager am = getAssets();
        try {
            InputStream is = am.open("img/" + assetName);
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image for the puzzle
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            is.reset();

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), bmOptions);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Setting the selected image into the ImageView for the puzzle
    private void setPicFromPath(String picturePath, ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        AssetManager am = getAssets();
        try {
            //InputStream is = am.open("img/" + assetName);
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, bmOptions); //new Rect(-1, -1, -1, -1)
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image for the puzzle
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            //is.reset();

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            BitmapFactory.decodeFile(picturePath, bmOptions); //new Rect(-1, -1, -1, -1)
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Splitting the image into a number of pieces for the puzzle
    private ArrayList<PuzzlePiece> splitImage(int rows) {
        Bitmap bitmap;
        rows = rows;
        int cols = 1;
        int piecesNumber = rows * cols;

        ImageView imageView = findViewById(R.id.imageView);
        ArrayList<PuzzlePiece> pieces = new ArrayList<>(piecesNumber);

        // Get the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        //Bitmap bitmap = drawable.getBitmap();

        if (drawable == null) {
            bitmap = selectedImage;
        }
        else {
            bitmap = drawable.getBitmap();
        }

        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        // Calculate the width and height of the pieces
        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;

        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                // Calculate offset for each piece
                int offsetX = 0;
                int offsetY = 0;
                if (col > 0) {
                    offsetX = pieceWidth / 3;
                }
                if (row > 0) {
                    offsetY = pieceHeight / 3;
                }

                // Apply the offset to each piece
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord - offsetX, yCoord - offsetY, pieceWidth + offsetX, pieceHeight + offsetY);
                PuzzlePiece piece = new PuzzlePiece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.xCoord = xCoord - offsetX + imageView.getLeft();
                piece.yCoord = yCoord - offsetY + imageView.getTop();
                piece.pieceWidth = pieceWidth + offsetX;
                piece.pieceHeight = pieceHeight + offsetY;

                // This bitmap will hold our final puzzle piece image
                Bitmap puzzlePiece = Bitmap.createBitmap(pieceWidth + offsetX, pieceHeight + offsetY, Bitmap.Config.ARGB_8888);

                // Draw path (to change the border of the piece to be like a real puzzle piece)
                int bumpSize = pieceHeight / 4;
                Canvas canvas = new Canvas(puzzlePiece);
                Path path = new Path();
                path.moveTo(offsetX, offsetY);
                if (row == 0) {
                    // top side piece
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                } else {
                    // top bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3, offsetY);
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, offsetY);
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                }

                if (col == cols - 1) {
                    // right side piece
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                } else {
                    // right bump
                    path.lineTo(pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.cubicTo(pieceBitmap.getWidth() - bumpSize,offsetY + (pieceBitmap.getHeight() - offsetY) / 6, pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                }

                if (row == rows - 1) {
                    // bottom side piece
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                } else {
                    // bottom bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, pieceBitmap.getHeight());
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5,pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6, pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3, pieceBitmap.getHeight());
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                }

                if (col == 0) {
                    // left side piece
                    path.close();
                } else {
                    // left bump
                    path.lineTo(offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.cubicTo(offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6, offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.close();
                }

                // Mask the piece to look like a real puzzle piece
                Paint paint = new Paint();
                paint.setColor(0XFF000000);
                paint.setStyle(Paint.Style.FILL);

                canvas.drawPath(path, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(pieceBitmap, 0, 0, paint);

                // Draw a white border
                Paint border = new Paint();
                border.setColor(0X80FFFFFF);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(8.0f);
                canvas.drawPath(path, border);

                // Draw a black border
                border = new Paint();
                border.setColor(0X80000000);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(3.0f);
                canvas.drawPath(path, border);

                // Set the resulting bitmap to the piece
                piece.setImageBitmap(puzzlePiece);

                pieces.add(piece);
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }
        return pieces;
    }

    // Splitting the image into a number of pieces for the puzzle (for Uri)
    private ArrayList<PuzzlePiece> splitImageUri(int rows, Uri selectedImage, Cursor cursor) throws IOException {
        Bitmap bitmap = null;
        rows = rows;
        int cols = 1;
        int piecesNumber = rows * cols;

        ImageView imageView = findViewById(R.id.imageView);
        ArrayList<PuzzlePiece> pieces = new ArrayList<>(piecesNumber);

        // Get the scaled bitmap of the source image
        //BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        //Bitmap bitmap = drawable.getBitmap();
        //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
        //bitmap = uriToBitmap(selectedImage);

        if (Build.VERSION.SDK_INT >= 29) {
            // You can replace '0' by 'cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)'
            // Note that now, you read the column '_ID' and not the column 'DATA'
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getInt(0));

            // now that you have the media URI, you can decode it to a bitmap
            ParcelFileDescriptor pfd = null;
            try {
                pfd = this.getContentResolver().openFileDescriptor(selectedImage, "r");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (pfd != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
            }
        } else {
            // Repeat the code you already are using
        }

        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);


        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        // Calculate the width and height of the pieces
        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;

        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                // Calculate offset for each piece
                int offsetX = 0;
                int offsetY = 0;
                if (col > 0) {
                    offsetX = pieceWidth / 3;
                }
                if (row > 0) {
                    offsetY = pieceHeight / 3;
                }

                // Apply the offset to each piece
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord - offsetX, yCoord - offsetY, pieceWidth + offsetX, pieceHeight + offsetY);
                PuzzlePiece piece = new PuzzlePiece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.xCoord = xCoord - offsetX + imageView.getLeft();
                piece.yCoord = yCoord - offsetY + imageView.getTop();
                piece.pieceWidth = pieceWidth + offsetX;
                piece.pieceHeight = pieceHeight + offsetY;

                // This bitmap will hold our final puzzle piece image
                Bitmap puzzlePiece = Bitmap.createBitmap(pieceWidth + offsetX, pieceHeight + offsetY, Bitmap.Config.ARGB_8888);

                // Draw path (to change the border of the piece to be like a real puzzle piece)
                int bumpSize = pieceHeight / 4;
                Canvas canvas = new Canvas(puzzlePiece);
                Path path = new Path();
                path.moveTo(offsetX, offsetY);
                if (row == 0) {
                    // top side piece
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                } else {
                    // top bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3, offsetY);
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, offsetY);
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                }

                if (col == cols - 1) {
                    // right side piece
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                } else {
                    // right bump
                    path.lineTo(pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.cubicTo(pieceBitmap.getWidth() - bumpSize,offsetY + (pieceBitmap.getHeight() - offsetY) / 6, pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                }

                if (row == rows - 1) {
                    // bottom side piece
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                } else {
                    // bottom bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, pieceBitmap.getHeight());
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5,pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6, pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3, pieceBitmap.getHeight());
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                }

                if (col == 0) {
                    // left side piece
                    path.close();
                } else {
                    // left bump
                    path.lineTo(offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.cubicTo(offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6, offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.close();
                }

                // Mask the piece to look like a real puzzle piece
                Paint paint = new Paint();
                paint.setColor(0XFF000000);
                paint.setStyle(Paint.Style.FILL);

                canvas.drawPath(path, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(pieceBitmap, 0, 0, paint);

                // Draw a white border
                Paint border = new Paint();
                border.setColor(0X80FFFFFF);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(8.0f);
                canvas.drawPath(path, border);

                // Draw a black border
                border = new Paint();
                border.setColor(0X80000000);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(3.0f);
                canvas.drawPath(path, border);

                // Set the resulting bitmap to the piece
                piece.setImageBitmap(puzzlePiece);

                pieces.add(piece);
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }
        return pieces;
    }

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Calculating image position into the ImageView
    private int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }


    // Show a menu to choose between camera or photo gallery
    public void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery", "Cancel" };

        // Open the menu as an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an action");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void writeStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //startPeriodicRequest();
            } else {
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
        } else {
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

}