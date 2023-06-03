package com.team.ni_hon.local_data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OptionsSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LocalData";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "OPTIONS";
    private static final String COLUMN_OPTION = "option";
    private static final String COLUMN_RIGHT = "right";
    private static final String COLUMN_ID_QUESTION = "idQuestion";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_OPTION + " TEXT, " +
            COLUMN_RIGHT + " INTEGER, " +
            COLUMN_ID_QUESTION + " INTEGER, " +
            "FOREIGN KEY (" + COLUMN_ID_QUESTION + ") REFERENCES " + QuestionSQLiteHelper.TABLE_NAME + "(" + QuestionSQLiteHelper.COLUMN_ID_QUESTION + "))";


    public OptionsSQLiteHelper(@Nullable Context context) {
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
