package com.nektar.android.trainote.database;

/**
 * Created by olo35 on 23.04.2016.
 */
public class NoteDbSchema {
    public static final class NoteTable
    {
        public static final String NAME = "notes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String CATEGORY = "category";
            public static final String DATE = "date";
            public static final String TEXT = "text";
            public static final String WEEK = "week";
        }
    }
    public static final class CategoryTable
    {
        public static final String NAME = "categories";

        public static final class Cols {
            public static final String CATEGORY = "category";
        }
    }
}
