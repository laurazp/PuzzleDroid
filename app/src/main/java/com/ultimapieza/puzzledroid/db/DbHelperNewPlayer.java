package com.ultimapieza.puzzledroid.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ultimapieza.puzzledroid.entidades.Players;

import java.util.ArrayList;

public class DbHelperNewPlayer extends SQLiteOpenHelper {

    // Definimos variables de la BBDD
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "player_puzzle.db";
    public static final String TABLE_PLAYER = "t_player";
    public Context context;

    public DbHelperNewPlayer(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    //Método para crear la BD y la tabla
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PLAYER + "(" +
                "nombre TEXT PRIMARY KEY,"+
                "score INTEGER)");

    }

    // Método que se ejecuta al cambiar la versión de la BD (si se quieren actualizar campos, etc.)
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_PLAYER);
        onCreate(sqLiteDatabase);
    }
    public String getTablePlayer(){

        return this.TABLE_PLAYER;
    }
    public Cursor getData(){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="select * from "+ TABLE_PLAYER;
        Cursor crs =db.rawQuery(query, null);
        return crs;
    }
    public ArrayList<Players> mostrarPlayers(){

        DbHelperNewPlayer dbHelper= new DbHelperNewPlayer(context);
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        ArrayList<Players> listPlayer= new ArrayList<>();
        Cursor cursorPlayers;

        cursorPlayers=db.rawQuery("SELECT * FROM " + TABLE_PLAYER,null);
        if(cursorPlayers.moveToFirst()){
            do {
                Players player = new Players();
                player.setName(cursorPlayers.getString(0));
                Log.d("Nombre del jugador", cursorPlayers.getString(0));
                //player.setScore(cursorPlayers.getInt(1));
                listPlayer.add(player);

            }while(cursorPlayers.moveToNext());
        }
        cursorPlayers.close();


        return listPlayer;
    }
}
