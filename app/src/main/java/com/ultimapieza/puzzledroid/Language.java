package com.ultimapieza.puzzledroid;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
public class Language extends AppCompatActivity {
    Spinner spinner;
    public static final String[] languages ={"English","Spanish","French","Japanese"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLang = adapterView.getItemAtPosition(i).toString();
                 if(selectedLang.equals("Spanish")){
                    setLocal( "es");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final Intent mainIntent = new Intent(Language.this, MainActivity.class);
                            Language.this.startActivity(mainIntent);
                            Language.this.finish();
                        }
                    }, 6000);

                }else if(selectedLang.equals("English")){
                     setLocal( "en");
                     new Handler().postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             final Intent mainIntent = new Intent(Language.this, MainActivity.class);
                             Language.this.startActivity(mainIntent);
                             Language.this.finish();
                         }
                     }, 6000);
                 } else if(selectedLang.equals("French")){
                    setLocal( "fr");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final Intent mainIntent = new Intent(Language.this, MainActivity.class);
                            Language.this.startActivity(mainIntent);
                            Language.this.finish();
                        }
                    }, 5000);
                } else if(selectedLang.equals("Japanese")){
                    setLocal( "ja");
                    new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent mainIntent = new Intent(Language.this, MainActivity.class);
                        Language.this.startActivity(mainIntent);
                        Language.this.finish();
                    }
                }, 6000);
            }
                else {
                    Toast.makeText(Language.this, "Please select a Language", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Language.this, "Please select a Language", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public  void setLocal(String langCode){
        Locale locale= new Locale(langCode);
        //Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

    }
}