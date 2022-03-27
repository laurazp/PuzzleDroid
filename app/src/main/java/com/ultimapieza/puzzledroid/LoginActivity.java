package com.ultimapieza.puzzledroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ultimapieza.puzzledroid.db.DbNewPlayer;
import com.ultimapieza.puzzledroid.db.DbPlayer;

public class LoginActivity extends AppCompatActivity {

    public EditText txtPlayerInput;
    private Button txtStartGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtPlayerInput=findViewById(R.id.txtPlayerInput);
        txtStartGame= findViewById(R.id.txtStartGame);

        txtStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DbNewPlayer dbNewPlayer= new DbNewPlayer(LoginActivity.this);
                Long id = dbNewPlayer.insertPlayer(txtPlayerInput.getText().toString());

                if(id>0){
                    Toast.makeText(LoginActivity.this, "Nuevo jugador registrado", Toast.LENGTH_LONG).show();
                    clean();
                }else{
                    Toast.makeText(LoginActivity.this, "Error al registrar nuevo jugador", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent (view.getContext(), PlayActivity.class);
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
