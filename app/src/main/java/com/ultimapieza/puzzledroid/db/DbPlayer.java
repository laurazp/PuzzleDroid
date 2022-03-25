package com.ultimapieza.puzzledroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class DbPlayer extends DbHelper{
    public DbHelper dbH;
    Context context;

    public DbPlayer(@Nullable Context context) {
        super(context);
        this.context=context;
    }
    public long insertPlayer(int id, String nombre, int score){
        long player=0;
        try{
            DbHelper dbHelper= new DbHelper(context);
            SQLiteDatabase db= dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("nombre", nombre);
            values.put("score", score);

             player= db.insert(dbH.getTableScore(),null, values);
        }catch(Exception ex){
            ex.toString();
        }
        return player;


    }
}
