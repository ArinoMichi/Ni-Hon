package com.team.ni_hon.local_data_base;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team.ni_hon.local_data_base.database_models.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionSQLiteHelper extends SQLiteOpenHelper {
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
            COLUMN_ID_QUESTION + " TEXT, " +
            COLUMN_COMPLETE + " INTEGER, " +
            COLUMN_RETRIES + " INTEGER, " +
            COLUMN_EMAIL + " TEXT)";


    public QuestionSQLiteHelper(@Nullable Context context) {
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

    public void addQuestion(@NonNull Question question) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_LEVEL, question.getLevel());
        values.put(COLUMN_QUESTION_ES, question.getQuestionES());
        values.put(COLUMN_QUESTION_EN, question.getQuestionEN());
        values.put(COLUMN_QUESTION_CH, question.getQuestionCH());
        values.put(COLUMN_ID_QUESTION, question.getIdQuestion());
        values.put(COLUMN_COMPLETE, question.isComplete() ? 1 : 0);//1->true | 0->false
        values.put(COLUMN_RETRIES, question.getRetries());
        values.put(COLUMN_EMAIL, question.getEmail());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<Question> getQuestionsByLevelAndEmail(int level, String email) {
        List<Question> questions = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LEVEL + "=? AND " + COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(level), email});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int questionLevel = cursor.getInt(cursor.getColumnIndex(COLUMN_LEVEL));
                String questionES = cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_ES));
                String questionEN = cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_EN));
                String questionCH = cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_CH));
                String idQuestion = cursor.getString(cursor.getColumnIndex(COLUMN_ID_QUESTION));
                boolean complete = cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETE)) == 1;
                int retries = cursor.getInt(cursor.getColumnIndex(COLUMN_RETRIES));

                Question question = new Question();
                question.setLevel(questionLevel);
                question.setQuestionES(questionES);
                question.setQuestionEN(questionEN);
                question.setQuestionCH(questionCH);
                question.setIdQuestion(idQuestion);
                question.setComplete(complete);
                question.setRetries(retries);
                question.setEmail(email);

                questions.add(question);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return questions;
    }
}
