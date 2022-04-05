package com.ultimapieza.puzzledroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.ultimapieza.puzzledroid.PuzzleActivity;
import com.ultimapieza.puzzledroid.LoginActivity;

public class DbPlayer extends DbHelper{
    public DbHelper dbH;
    Context context;
    PuzzleActivity score;
    LoginActivity playerName;

    public DbPlayer(@Nullable Context context) {
        super(context);
        this.context=context;

    }public long insertPlayer(String nombre, PuzzleActivity score){

        long player=0;
        try{
            DbHelper dbHelper= new DbHelper(context);
            SQLiteDatabase db= dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("score", score.getScore());

             player= db.insert(dbH.getTableScore(),null, values);
        }catch(Exception ex){
            ex.toString();
        }
        return player;


    }






}
