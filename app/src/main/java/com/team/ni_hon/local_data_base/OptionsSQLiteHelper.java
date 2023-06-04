package com.team.ni_hon.local_data_base;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team.ni_hon.local_data_base.database_models.Option;

import java.util.ArrayList;
import java.util.List;

public class OptionsSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "OPTIONS";
    private static final String COLUMN_OPTION = "option";
    private static final String COLUMN_RIGHT = "rights";
    private static final String COLUMN_ID_QUESTION = "idQuestion";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_OPTION + " TEXT, "
            + COLUMN_RIGHT + " INTEGER, "
            + COLUMN_ID_QUESTION + " TEXT)";

    public OptionsSQLiteHelper(@Nullable Context context) {
        super(context, UserSQLiteHelper.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //METODO AL ACTUALIZAR VERSION, ACTUALICE LA TABLA
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void addOption(@NonNull Option option) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_OPTION, option.getOption());
        values.put(COLUMN_RIGHT, option.isRight() ? 1 : 0); //1-> true | 0-> false
        values.put(COLUMN_ID_QUESTION, option.getIdQuestion());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteOptionsByIdQuestions(List<String> idQuestions) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COLUMN_ID_QUESTION + "=?";

        for (String idQuestion : idQuestions) {
            String[] whereArgs = {idQuestion};
            db.delete(TABLE_NAME, whereClause, whereArgs);
        }

        db.close();
    }

    @SuppressLint("Range")
    public List<Option> getOptionsByQuestionId(String idQuestion) {
        List<Option> options = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_QUESTION + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idQuestion)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String optionText = cursor.getString(cursor.getColumnIndex(COLUMN_OPTION));
                int rights = cursor.getInt(cursor.getColumnIndex(COLUMN_RIGHT));
                String questionId = cursor.getString(cursor.getColumnIndex(COLUMN_ID_QUESTION));


                if(rights==0)
                    options.add(new Option(optionText, false, questionId));

                if(rights==1)
                    options.add(new Option(optionText, true, questionId));


            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return options;
    }

}
