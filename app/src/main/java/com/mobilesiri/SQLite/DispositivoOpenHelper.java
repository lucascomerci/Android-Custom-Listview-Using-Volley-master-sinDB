package com.mobilesiri.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vicentico on 10/7/17.
 */

public class DispositivoOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public static String getDispositivosTableName() {
        return DISPOSITIVOS_TABLE_NAME;
    }

    private static final String DISPOSITIVOS_TABLE_NAME = "TDispositivos";
    private String sqlCrear = "CREATE TABLE TDispositivos (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, detalles TEXT, ip TEXT, MAC TEXT)";


    public DispositivoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCrear);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int anterior, int nueva) {
        db.execSQL("DROP TABLE IF EXISTS TDispositivos");
        db.execSQL(sqlCrear);
    }
}
