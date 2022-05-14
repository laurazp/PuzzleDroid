package com.ultimapieza.puzzledroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ultimapieza.puzzledroid.ResultActivity;


public class DbNewPlayer extends DbHelperNewPlayer {

    Context context;
    String name;
    //PuzzleActivity score_;
    int finalScore;
    //LoginActivity playerName;
    ResultActivity rs;

    public DbNewPlayer(@Nullable Context context) {
        super(context);
        this.context=context;
    }

    public String GetName(){
        return name;
    }

    // Método para introducir datos del jugador en el login (cuando introduce su nombre)
    public long insertPlayer(String nombre, int score){
        long newplayer=0;
        rs = new ResultActivity();
        finalScore = rs.getFinalScore();

        try{
            DbHelperNewPlayer dbHelper= new DbHelperNewPlayer(context);
            SQLiteDatabase db= dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("score", finalScore);
            newplayer= db.insert(TABLE_PLAYER,null, values);
        }
        catch(Exception ex){
            ex.toString();
        }
        return newplayer;
    }

    // Método para actualizar la puntuación del jugador una vez ha terminado de jugar y antes de mostrar los resultados
    public void updatePlayer(String name, int score){

        // Comprobaciones para mostrar Logs en la consola
        Log.d("Score en updatePlayer", String.valueOf(score));
        Log.d("Nombre en updatePlayer", String.valueOf(name));

        try{
            DbHelperNewPlayer dbHelper= new DbHelperNewPlayer(context);
            SQLiteDatabase db= dbHelper.getWritableDatabase();

            //db.execSQL("UPDATE t_player SET score = " + score + " WHERE nombre = '" + name + "'");
            db.execSQL("UPDATE t_player SET nombre = " + name + " WHERE score = '" + score + "'");
        }
        catch(Exception ex){
            ex.toString();
        }

    }

}
