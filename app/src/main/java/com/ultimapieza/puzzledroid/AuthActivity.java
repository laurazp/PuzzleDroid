package com.ultimapieza.puzzledroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.io.StringWriter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText TextEmail;
    private EditText TextPassword;
    @BindView(R.id.signUpButton) Button btnSignUp;
    @BindView(R.id.loginButton) Button btnLogin;


    private ProgressDialog progressDialog;

    //Declaramos un objeto firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        //Inicializamos el objetos de mAuth
        mAuth = FirebaseAuth.getInstance();

        //referenciamos los views
        TextEmail = (EditText) findViewById(R.id.editEmailtext);
        TextPassword = (EditText) findViewById(R.id.editPasswordtext);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        btnSignUp.setOnClickListener((View.OnClickListener) this);
        btnLogin.setOnClickListener((View.OnClickListener) this);

    }

    private void registrarUsuario(){

        //obtención del email y la contrasña desde las cajas de texto
        String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Ingrese un email",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Ingrese una contraseña",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Realizando registro en linea...");
        progressDialog.show();

        //creating a new user

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //cheking if succes
                        if (task.isSuccessful()){

                            Toast.makeText(AuthActivity.this,"Se ha registrado el email", Toast.LENGTH_LONG).show();
                        }else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) { //si ya existe usuario
                                Toast.makeText(AuthActivity.this, "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(AuthActivity.this,"No se pudo registrar el email", Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });

    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    private void loguearUsuario(){
        //obtención del email y la contrasña desde las cajas de texto
        String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Ingrese un email",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Ingrese una contraseña",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Realizando registro en linea...");
        progressDialog.show();

        //login user

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //cheking if succes
                        if (task.isSuccessful()){

                            Toast.makeText(AuthActivity.this,"Bienvenido:"+ TextEmail.getText(),Toast.LENGTH_LONG).show();
                        }else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) { //si ya existe usuario
                                Toast.makeText(AuthActivity.this, "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(AuthActivity.this,"No se pudo registrar el email", Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.signUpButton:
                //invocamos el método
                registrarUsuario();
                break;
            case R.id.loginButton:
                //invocamos el método
                loguearUsuario();


        }


    }
}