package com.nektar.android.trainote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.nektar.android.trainote.database.NoteBaseHelper;
import com.nektar.android.trainote.database.NoteCursorWraper;
import com.nektar.android.trainote.database.NoteDbSchema;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by olo35 on 18.04.2016.
 */
public class NoteContainer {
    private static NoteContainer sNoteContainer;
    private Context mContext;
    private SQLiteDatabase mDatabase;



    public static NoteContainer get(Context context)
    {
        if(sNoteContainer == null)
        {
            sNoteContainer = new NoteContainer(context);

        }
        return sNoteContainer;
    }
    public NoteContainer(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new NoteBaseHelper(mContext).getWritableDatabase();

    }
    public List<Note> getNotes()
    {
        List<Note> notes = new ArrayList<>();


        Cursor c = mDatabase.query(NoteDbSchema.NoteTable.NAME,null,null,null,null,null, NoteDbSchema.NoteTable.Cols.DATE+" ASC");

        NoteCursorWraper cursor = new NoteCursorWraper(c);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }

        }finally{
            cursor.close();
        }
        return notes;
    }
    public List<String> getCategories()
    {
        List<String> notes = new ArrayList<>();


        Cursor c = mDatabase.query(NoteDbSchema.CategoryTable.NAME,null,null,null,null,null, NoteDbSchema.CategoryTable.Cols.CATEGORY+" ASC");

        NoteCursorWraper cursor = new NoteCursorWraper(c);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                notes.add(cursor.getCategory());
                cursor.moveToNext();
            }

        }finally{
            cursor.close();
        }
        return notes;
    }

    public Note getNote(UUID id)
    {

        NoteCursorWraper cursor = queryNotes(NoteDbSchema.NoteTable.Cols.UUID+"=?",new String[]{id.toString()});

        try{

            if(cursor.getCount()==0)
            {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getNote();

        }finally{
            cursor.close();
        }
    }

    public List<List<Note>> getWeeks()
    {
        List<Note> list =sNoteContainer.getNotes();
        List<List<Note>> weeks = new ArrayList<List<Note>>();

        if(list.size()==0)
        {
            weeks.add(new ArrayList<Note>());
            return weeks;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(list.get(0).getDate());

        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);

        List<Note> temp = new ArrayList<>();


        for(int j=0;j<list.size();j++)
        {
            calendar.setTime(list.get(j).getDate());

            int weekOfYear2 = calendar.get(Calendar.WEEK_OF_YEAR);
            int year2 = calendar.get(Calendar.YEAR);

            if(weekOfYear==weekOfYear2 && year==year2)
            {
                temp.add(list.get(j));

            }
            else {
                weeks.add(temp);

                temp = new ArrayList<>();
                temp.add(list.get(j));

                calendar.setTime(list.get(j).getDate());

                weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                year = calendar.get(Calendar.YEAR);
            }
        }
        if(temp.size()!=0)
        {
            weeks.add(temp);
        }


        boolean isCurrent = false;
        calendar.setTime(new Date());
        int numberOfWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        for (int i = 0; i < weeks.size(); i++) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(weeks.get(i).get(0).getDate());
            if (calendar1.get(Calendar.WEEK_OF_YEAR) == numberOfWeek) {
                isCurrent = true;
                break;
            }
        }
        if(!isCurrent)
        {
            weeks.add(new ArrayList<Note>());
        }


        //Log.i("TAG",weeks.size() + "SIZE ");
        return weeks;
    }

    public List<String> getWeekPeriods()
    {
        List<String> list = new ArrayList<>();
        List<List<Note>> weeks = sNoteContainer.getWeeks();

        for (int i=0;i<weeks.size();i++)
        {
            Calendar calendar = Calendar.getInstance();
            if(weeks.get(i).size()==0)
            {
                calendar.setTime(new Date());
            }
            else {
                calendar.setTime(weeks.get(i).get(0).getDate());
            }
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if((dayOfWeek-2)<0)
            {
                calendar.add(Calendar.DAY_OF_YEAR, -6);
            }
            else {
                calendar.add(Calendar.DAY_OF_YEAR, (-1) * (dayOfWeek - 2));
            }
            int sday = calendar.get(Calendar.DAY_OF_MONTH);
            int smonth = calendar.get(Calendar.MONTH);

            calendar.add(Calendar.DAY_OF_YEAR, 6);

            int eday = calendar.get(Calendar.DAY_OF_MONTH);
            int emonth = calendar.get(Calendar.MONTH);
            smonth++;
            emonth++;
            list.add("Tydzień: "+sday+"."+smonth+" - "+eday+"."+emonth);
        }

        return list;
    }

    public boolean isNote(Date date)
    {

        NoteCursorWraper cursor = queryNotes(NoteDbSchema.NoteTable.Cols.DATE+"=?",new String[]{date.toString()});


        try{

            if(cursor.getCount()==0)
            {
                return false;
            }
            return true;

        }finally{
            cursor.close();
        }
    }
    public boolean isCategory(String cat)
    {

        NoteCursorWraper cursor = queryNotesCategory(NoteDbSchema.CategoryTable.Cols.CATEGORY+"=?",new String[]{cat});

        try{

            if(cursor.getCount()==0)
            {
                return false;
            }
            return true;

        }finally{
            cursor.close();
        }
    }
    private NoteCursorWraper queryNotes(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(NoteDbSchema.NoteTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new NoteCursorWraper(cursor);
    }
    private NoteCursorWraper queryNotesCategory(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(NoteDbSchema.CategoryTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new NoteCursorWraper(cursor);
    }
    public void updateNote(Note note)
    {
        String uuidString = note.getId().toString();
        ContentValues values = getContentValues(note);

        mDatabase.update(NoteDbSchema.NoteTable.NAME,values, NoteDbSchema.NoteTable.Cols.UUID + "=?",new String[]{uuidString});
    }
    public void add(Note n)
    {
        ContentValues values=getContentValues(n);
        mDatabase.insert(NoteDbSchema.NoteTable.NAME,null,values);
    }
    public void addCategroy(String cat)
    {
        ContentValues values=getContentValues(cat);
        mDatabase.insert(NoteDbSchema.CategoryTable.NAME,null,values);
    }
    public void delete(Note n)
    {
        mDatabase.delete(NoteDbSchema.NoteTable.NAME, NoteDbSchema.NoteTable.Cols.UUID+"=?",new String[]{n.getId().toString()});
    }
    public void deleteCategory(String cat)
    {
        mDatabase.delete(NoteDbSchema.CategoryTable.NAME, NoteDbSchema.CategoryTable.Cols.CATEGORY+"=?",new String[]{cat});
    }

    private static ContentValues getContentValues(Note note)
    {
        ContentValues values = new ContentValues();
        values.put(NoteDbSchema.NoteTable.Cols.UUID,note.getId().toString());
        values.put(NoteDbSchema.NoteTable.Cols.CATEGORY,note.getCategory());
        values.put(NoteDbSchema.NoteTable.Cols.DATE,note.getDate().getTime());
        values.put(NoteDbSchema.NoteTable.Cols.TEXT, note.getText());
        values.put(NoteDbSchema.NoteTable.Cols.WEEK,note.getWeek());

        return values;
    }
    private static ContentValues getContentValues(String cat)
    {
        ContentValues values = new ContentValues();
        values.put(NoteDbSchema.CategoryTable.Cols.CATEGORY,cat);


        return values;
    }
}
