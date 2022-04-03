package com.ultimapieza.puzzledroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.ultimapieza.puzzledroid.LoginActivity;
import com.ultimapieza.puzzledroid.PuzzleActivity;
import com.ultimapieza.puzzledroid.ResultActivity;


public class DbNewPlayer extends DbHelperNewPlayer {

    Context context;
    String name;
    PuzzleActivity score_;
    int score;
    LoginActivity playerName;
    ResultActivity rs;

    public DbNewPlayer(@Nullable Context context) {
        super(context);
        this.context=context;
    }
    public String GetName(){
        return name;
    }
    //int id, , int score
    public long insertPlayer(String nombre, int score){
        long newplayer=0;
        rs = new ResultActivity();
        score = rs.getFinalScore();
        try{
            DbHelperNewPlayer dbHelper= new DbHelperNewPlayer(context);
            SQLiteDatabase db= dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("score", score);

             newplayer= db.insert(TABLE_PLAYER,null, values);
        }catch(Exception ex){
            ex.toString();
        }
        return newplayer;


    }


}
