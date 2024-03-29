package com.ultimapieza.puzzledroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ultimapieza.puzzledroid.db.DbNewPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    public EditText txtPlayerInput;
    //private Button txtStartGame;
    @BindView(R.id.txtStartGame) Button txtStartGame;
    private int score;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        txtPlayerInput=findViewById(R.id.txtPlayerInput);
       // txtStartGame= findViewById(R.id.txtStartGame);


        // Al hacer click en el botón Start, registra al jugador en la BD y empieza el juego
        txtStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DbNewPlayer dbNewPlayer= new DbNewPlayer(LoginActivity.this);
                Long id = dbNewPlayer.insertPlayer(txtPlayerInput.getText().toString(),score);

                // Asigna el input de texto a userName
                userName = txtPlayerInput.getText().toString();
                try {
                    if(id>0){
                        //Toast.makeText(LoginActivity.this, "Nuevo jugador registrado", Toast.LENGTH_LONG).show();
                        clean();
                }
                // Comprueba que se haya registrado correctamente al jugador en la BD

                }catch(Exception exception)
                {
                    //Toast.makeText(LoginActivity.this, "Error al registrar nuevo jugador", Toast.LENGTH_LONG).show();
                }

                // Crea un nuevo intent para PlayActivity y pasa el userName
                Intent intent = new Intent (view.getContext(), PlayActivity.class);
                intent.putExtra("USERNAME", userName);
                Log.d("Nombre en login", String.valueOf(userName));
                view.getContext().startActivity(intent);
            }
        });
    }

    public String GetInputName(){
        return String.valueOf(this.txtPlayerInput);
    }

    private void clean(){
        txtPlayerInput.setText(" ");
    }
}
