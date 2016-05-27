package com.nektar.android.trainote.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nektar.android.trainote.Note;

import java.util.Date;
import java.util.UUID;

/**
 * Created by olo35 on 23.04.2016.
 */
public class NoteCursorWraper extends CursorWrapper{
    public NoteCursorWraper(Cursor cursor)
    {
        super(cursor);
    }
    public Note getNote()
    {
        String uuidString = getString(getColumnIndex(NoteDbSchema.NoteTable.Cols.UUID));
        String category = getString(getColumnIndex(NoteDbSchema.NoteTable.Cols.CATEGORY));
        long date = getLong(getColumnIndex(NoteDbSchema.NoteTable.Cols.DATE));
        String text = getString(getColumnIndex(NoteDbSchema.NoteTable.Cols.TEXT));
        int week = getInt(getColumnIndex(NoteDbSchema.NoteTable.Cols.WEEK));

        Note note =new Note(UUID.fromString(uuidString));
        note.setText(text);
        note.setDate(new Date(date));
        note.setCategory(category);
        note.setWeek(week);


        return note;
    }
    public String getCategory()
    {

        String cat = getString(getColumnIndex(NoteDbSchema.CategoryTable.Cols.CATEGORY));

        return cat;
    }
}
