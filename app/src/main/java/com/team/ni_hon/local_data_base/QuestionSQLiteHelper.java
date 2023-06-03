package com.team.ni_hon.local_data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class QuestionSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LocalData";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "QUESTIONS";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_QUESTION_ES = "questionES";
    private static final String COLUMN_QUESTION_EN = "questionEN";
    private static final String COLUMN_QUESTION_CH = "questionCH";
    public static final String COLUMN_ID_QUESTION = "idQuestion";
    private static final String COLUMN_COMPLETE = "complete";
    private static final String COLUMN_RETRIES = "retries";
    public static final String COLUMN_EMAIL = "email";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_LEVEL + " INTEGER, " +
            COLUMN_QUESTION_ES + " TEXT, " +
            COLUMN_QUESTION_EN + " TEXT, " +
            COLUMN_QUESTION_CH + " TEXT, " +
            COLUMN_ID_QUESTION + " INTEGER UNIQUE, " +
            COLUMN_COMPLETE + " INTEGER, " +
            COLUMN_RETRIES + " INTEGER, " +
            COLUMN_EMAIL + " TEXT, " +
            "FOREIGN KEY (" + COLUMN_EMAIL + ") REFERENCES "
            + UserSQLiteHelper.TABLE_NAME + "(" + UserSQLiteHelper.COLUMN_EMAIL + "))";


    public QuestionSQLiteHelper(@Nullable Context context) {
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
