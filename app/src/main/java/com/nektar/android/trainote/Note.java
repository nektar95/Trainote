package com.nektar.android.trainote;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by olo35 on 11.04.2016.
 */
public class Note {
    private String mText;
    private Date mDate;
    private String mCategory;

    public int getWeek() {
        return mWeek;
    }

    public void setWeek(int week) {
        mWeek = week;
    }

    private int mWeek;

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    private UUID mId;

    public Note()
    {
        mDate = new Date();
        newDate();
        mId = UUID.randomUUID();
    }
    public Note(UUID id) {
        mId = id;
        mDate = new Date();
        newDate();
    }
    private void newDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        mWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        mDate = calendar.getTime();
    }
    /*
    public Note(String text,String category)
    {
        mText =text;
        mCategory = category;
        mDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        mDate = calendar.getTime();

        mId = UUID.randomUUID();
    }
    */
    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Date getDate() {
        return mDate;

    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }
}
