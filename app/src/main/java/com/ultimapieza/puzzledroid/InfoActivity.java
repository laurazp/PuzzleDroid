package com.ultimapieza.puzzledroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.returnBtn) Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        // When clicking on backBtn, go back to Menu
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}