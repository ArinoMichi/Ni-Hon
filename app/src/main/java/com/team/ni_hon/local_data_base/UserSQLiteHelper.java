package com.team.ni_hon.local_data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LocalData";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "USER";
    public static final String COLUMN_IDQUEST = "id";
    public static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ICON = "icon";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_IDQUEST + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_EMAIL + " TEXT UNIQUE, " +
            COLUMN_ICON + " INTEGER)";


    public UserSQLiteHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //METODO PARA ACTUALIZAR VERSION SI QUIERO MODIFICAR LA TABLA
    }
}
