package com.ultimapieza.puzzledroid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelperNewPlayer extends SQLiteOpenHelper {

    // Definimos variables de la BBDD
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "player_puzzle.db";
    public static final String TABLE_PLAYER = "t_player";

    public DbHelperNewPlayer(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
}
