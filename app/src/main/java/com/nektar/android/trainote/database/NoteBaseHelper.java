package com.nektar.android.trainote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.nektar.android.trainote.NoteContainer;
import com.nektar.android.trainote.database.NoteDbSchema;

import com.nektar.android.trainote.Note;

/**
 * Created by olo35 on 23.04.2016.
 */
public class NoteBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME="noteBase.db";

    public NoteBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ NoteDbSchema.NoteTable.NAME + "("+" _id integer primary key autoincrement, "+
                NoteDbSchema.NoteTable.Cols.UUID+", "+NoteDbSchema.NoteTable.Cols.CATEGORY+", "+NoteDbSchema.NoteTable.Cols.DATE+", "+NoteDbSchema.NoteTable.Cols.TEXT+", "+ NoteDbSchema.NoteTable.Cols.WEEK +")");
        db.execSQL("create table "+ NoteDbSchema.CategoryTable.NAME + "("+" _id integer primary key autoincrement, "+NoteDbSchema.CategoryTable.Cols.CATEGORY+")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
