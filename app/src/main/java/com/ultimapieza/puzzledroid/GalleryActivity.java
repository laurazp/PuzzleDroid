package com.ultimapieza.puzzledroid;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    String userName;
    int score;
    int numOfPieces;

    // Referencia para trabajar con Firebase Storage
    private StorageReference mStorageRef;
    File localFile = null;
    FirebaseAuth mAuth;
    FirebaseUser user;
    boolean firebaseApp ;
    FirebaseAppCheck firebaseAppCheck;

    ArrayList<String> pathsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        numOfPieces = getIntent().getIntExtra("NUMOFPIECES", 3);
        score = getIntent().getIntExtra("SCORE", 0);
        userName = getIntent().getStringExtra("USERNAME");
        Log.d("NumOfPieces = ", String.valueOf(numOfPieces));

        pathsList = new ArrayList<>();

        ProgressBar progressBar=findViewById(R.id.progressBar);
        ArrayList<String> imageList = new ArrayList<>();
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        ImageAdapter adapter= new ImageAdapter(imageList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images");
        progressBar.setVisibility(View.VISIBLE);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(GalleryActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });


        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                firebaseApp = accessFireBase();
                
                if (firebaseApp){
                    for( StorageReference fileRef: listResult.getItems() ){
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageList.add(uri.toString());
                                Log.d("item", uri.toString());
                                String imagePath = uri.toString();
                                pathsList.add(imagePath);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(GalleryActivity.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                                @Override public void onItemClick(View view, int position) {
                                    // do whatever
                                    String chosenImagePath = pathsList.get(position);
                                    Log.d("imagePath", chosenImagePath);

                                    Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                                    intent.putExtra("assetName", chosenImagePath);
                                    intent.putExtra("SCORE", score);
                                    intent.putExtra("USERNAME", userName);
                                    intent.putExtra("NUMOFPIECES", numOfPieces);
                                    startActivity(intent);
                                }

                                @Override public void onLongItemClick(View view, int position) {
                                    // do whatever
                                }
                            })
                    );


                }

            }
        });

        // Al hacer click, se inicia el puzzle con la imagen elegida
        /*recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                ImageAdapter.ViewHolder child = rv.getChildItemId(recyclerView);

                Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                intent.putExtra("assetName", );
                intent.putExtra("SCORE", score);
                intent.putExtra("USERNAME", userName);
                intent.putExtra("NUMOFPIECES", numOfPieces);
                startActivity(intent);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/



        // Inicializamos la referencia de Firebase Storage
        /*mStorageRef = FirebaseStorage.getInstance().getReference("images/flower.jpg");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        /*if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }*/

        /*if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(GalleryActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });

        //Recibe los valores de score, userName y numOfPieces
        numOfPieces = getIntent().getIntExtra("NUMOFPIECES", 3);
        score = getIntent().getIntExtra("SCORE", 0);
        userName = getIntent().getStringExtra("USERNAME");
        Log.d("NumOfPieces = ", String.valueOf(numOfPieces));
        private void signInAnonymously() {
            mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    // do your stuff
                }
            })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("FIREBASE", "signInAnonymously:FAILURE", exception);
                        }
                    });
        }

        // TODO: Muestra las imágenes desde Firebase Storage

        //File localFile = null;


         */
        /*try {
            localFile = File.createTempFile("tmpfile", ".jpg");
            Log.d("FILE", "File from Firebase Storage = " + localFile.getAbsolutePath().toString());
        } catch (IOException e) {
            Log.d("FILE ERROR", "Error creating file from Firebase Storage = " + e);
            e.printStackTrace();
        }

        if (localFile != null) {
            mStorageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            Log.d("FILE", "File from Firebase Storage downloaded successfully.");

                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                            //TODO: Añadir la imágenes al grid para elegir
                            /*AssetManager am = getAssets();
        try {
            final String[] files  = am.list("img");

            GridView grid = findViewById(R.id.grid);
            grid.setAdapter(new ImageAdapter(this));

            // Al hacer click, se inicia el puzzle con la imagen elegida
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                    intent.putExtra("assetName", files[i % files.length]);
                    intent.putExtra("SCORE", score);
                    intent.putExtra("USERNAME", userName);
                    intent.putExtra("NUMOFPIECES", numOfPieces);
                    startActivity(intent);
                }
            });

        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        }*/


                           /* Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                            intent.putExtra("assetName", localFile.getAbsolutePath());
                            intent.putExtra("SCORE", score);
                            intent.putExtra("USERNAME", userName);
                            intent.putExtra("NUMOFPIECES", numOfPieces);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    Log.d("FILE ERROR", "Error downloading files from Firebase Storage" + exception);
                }
            });
        }*/

        // Muestra las imágenes de la carpeta assets en el grid
        // Código para trabajar con imágenes estáticas de la carpeta assets/img
        /*AssetManager am = getAssets();
        try {
            final String[] files  = am.list("img");

            GridView grid = findViewById(R.id.grid);
            grid.setAdapter(new ImageAdapter(this));

            // Al hacer click, se inicia el puzzle con la imagen elegida
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                    intent.putExtra("assetName", files[i % files.length]);
                    intent.putExtra("SCORE", score);
                    intent.putExtra("USERNAME", userName);
                    intent.putExtra("NUMOFPIECES", numOfPieces);
                    startActivity(intent);
                }
            });

        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        }
    }*/

    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("FIREBASE", "signInAnonymously:FAILURE", exception);
                    }
                });
    }*/
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("FIREBASE", "signInAnonymously:FAILURE", exception);
                    }
                });
    }
    public boolean accessFireBase(){
        firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());

        if(firebaseAppCheck != null) {
            firebaseApp = true;
        }
        return firebaseApp;
    }
}