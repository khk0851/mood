package com.moodtoday.db;

import android.provider.BaseColumns;

public class DataColumns {
    public static class DiaryEntry implements BaseColumns {
        public static final String TABLE_NAME = "DIARY_TABLE";
        public static final String COLUMN_DATE = "DATE";
        public static final String COLUMN_WEATHER = "WEATHER";
        public static final String COLUMN_MUSIC = "MUSIC";
        public static final String COLUMN_CONTENTS = "CONTENTS";
        public static final String COLUMN_MOOD_NUMBER = "MOOD_NUM";
        public static final String COLUMN_WORDING = "WORDING";
        public static final String COLUMN_MEMBER = "MEMBER";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        _ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_DATE + " TEXT NOT NULL, " +
                        COLUMN_WEATHER + " INTEGER NOT NULL," +
                        COLUMN_MUSIC + " TEXT NOT NULL, " +
                        COLUMN_CONTENTS + " TEXT NOT NULL, " +
                        COLUMN_MOOD_NUMBER + " INTEGER NOT NULL, " +
                        COLUMN_WORDING + " TEXT NOT NULL, " +
                        COLUMN_MEMBER + " TEXT NOT NULL)";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class MemberEntry implements BaseColumns {
        public static final String TABLE_NAME = "MEMBER_TABLE";
        public static final String COLUMN_EMAIL = "EMAIL";
        public static final String COLUMN_PASSWORD = "PASSWORD";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_SEX = "SEX";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        _ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_EMAIL + " TEXT NOT NULL, " +
                        COLUMN_PASSWORD + " TEXT NOT NULL," +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_SEX + " TEXT NOT NULL)";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
