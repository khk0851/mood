package com.moodtoday.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.moodtoday.dto.DiaryDTO;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "diary.db";     // db 이름
    private static final int DATABASE_VERSION = 1;              // db 버전

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    // 테이블 생성
    public void createTable(SQLiteDatabase db) {
        db.execSQL(DataColumns.DiaryEntry.SQL_CREATE_TABLE);
        db.execSQL(DataColumns.MemberEntry.SQL_CREATE_TABLE);
    }

    // 일기 데이터 추가
    public long insertDiaryTable(String date, int weather, String contents, int moodNum, String music, String sWording, String member) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataColumns.DiaryEntry.COLUMN_DATE, date);
        values.put(DataColumns.DiaryEntry.COLUMN_WEATHER, weather);
        values.put(DataColumns.DiaryEntry.COLUMN_MUSIC, music);
        values.put(DataColumns.DiaryEntry.COLUMN_CONTENTS, contents);
        values.put(DataColumns.DiaryEntry.COLUMN_MOOD_NUMBER, moodNum);
        values.put(DataColumns.DiaryEntry.COLUMN_WORDING, sWording);
        values.put(DataColumns.DiaryEntry.COLUMN_MEMBER, member);

        long lResult = db.insert(DataColumns.DiaryEntry.TABLE_NAME, null, values);
        return lResult;
    }

    // 회원 데이터 추가
    public void insertMemberTable(String email, String paddword, String name, String sex) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataColumns.MemberEntry.COLUMN_EMAIL, email);
        values.put(DataColumns.MemberEntry.COLUMN_PASSWORD, paddword);
        values.put(DataColumns.MemberEntry.COLUMN_NAME, name);
        values.put(DataColumns.MemberEntry.COLUMN_SEX, sex);

        db.insert(DataColumns.MemberEntry.TABLE_NAME, null, values);
    }

    // 일기 데이터 읽어오기
    public ArrayList<DiaryDTO> selectDiaryTable(String date, String member) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DataColumns.DiaryEntry.TABLE_NAME
                + " WHERE " + DataColumns.DiaryEntry.COLUMN_DATE + " LIKE '%" + date
                + "%' AND " + DataColumns.DiaryEntry.COLUMN_MEMBER + "='" + member + "'";

        ArrayList<DiaryDTO> diaryList = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            DiaryDTO diary = new DiaryDTO();

            diary.set_id(cursor.getInt(0));
            diary.setDate(cursor.getString(1));
            diary.setWeather(cursor.getInt(2));
            diary.setMusic(cursor.getString(3));
            diary.setContents(cursor.getString(4));
            diary.setIconNum(cursor.getInt(5));
            diary.setWording(cursor.getString(6));
            diary.setMember(cursor.getString(7));

            diaryList.add(diary);
        }
        return diaryList;
    }

    // 회원 데이터 읽어오기
    public int selectMemberTable(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DataColumns.MemberEntry.TABLE_NAME
                + " WHERE " + DataColumns.MemberEntry.COLUMN_EMAIL + "='" + email
                + "' AND " + DataColumns.MemberEntry.COLUMN_PASSWORD + "='"  + password + "'";


        Cursor cursor = db.rawQuery(sql, null);

        Log.d("MyTag", cursor.getCount()+"");

        int count = cursor.getCount();

        /*MemberDTO member = new MemberDTO();
        cursor.moveToNext();
        member.set_id(cursor.getInt(0));
        member.setEmail(cursor.getString(1));
        member.setPassword(cursor.getString(2));
        member.setName(cursor.getString(3));
        member.setSex(cursor.getString(4));*/

        return count;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
