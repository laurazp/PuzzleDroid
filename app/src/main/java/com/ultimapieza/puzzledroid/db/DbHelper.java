package com.ultimapieza.puzzledroid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

// Clase para crear la Base de datos
public class DbHelper extends SQLiteOpenHelper {

    // Definimos variables de la BBDD
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "puzzle.db";
    public static final String TABLE_SCORE = "t_score";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Método para crear la BD y la tabla
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_SCORE + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "score INTEGER)");
    }

    // Método que se ejecuta al cambiar la versión de la BD (si se quieren actualizar campos, etc.)
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_SCORE);
        onCreate(sqLiteDatabase);
    }
    public String getTableScore(){
        return this.TABLE_SCORE;
    }
}
