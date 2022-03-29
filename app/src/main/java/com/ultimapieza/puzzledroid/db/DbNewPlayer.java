package com.ultimapieza.puzzledroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;                                    //retornanr nombre desde aquí

import com.ultimapieza.puzzledroid.Player;

import java.util.ArrayList;
import com.ultimapieza.puzzledroid.PuzzleActivity;
import com.ultimapieza.puzzledroid.Player;
import com.ultimapieza.puzzledroid.LoginActivity;


public class DbNewPlayer extends DbHelperNewPlayer {

    public DbHelperNewPlayer dbH;
    Context context;
    String name;
    PuzzleActivity score_;
    int score;
    LoginActivity playerName;

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
        score=0;
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
    public ArrayList<Player> PlayerPuzzleDroid(){
        DbHelper dbH= new DbHelper(context);
        SQLiteDatabase db =dbH.getWritableDatabase();
        ArrayList<Player> playerList= new ArrayList<>();
        Cursor cursor=null;
        Player player=null;


        cursor=db.rawQuery("SELECT * FROM " + TABLE_PLAYER,null);
        if(cursor.moveToFirst()){
            do {
                player= new Player();
                player.setName(cursor.getString(0));
                player.setScore(cursor.getInt(1));
                playerList.add(player);

            }while(cursor.moveToNext());
        }
        cursor.close();


        return playerList;
    }

}
