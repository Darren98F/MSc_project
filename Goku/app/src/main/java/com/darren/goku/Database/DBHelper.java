package com.darren.goku.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.darren.goku.Constant;
import com.darren.goku.Models.Video;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private final String TAG = this.getClass().getName();

    public DBHelper(@Nullable Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Create the database successful...");
        //create the VideoList table
        String sql = "create table " + Constant.TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title varchar, path varchar, comment varchar, evaluate varchar, date varchar)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Update the database successful...");
    }


    public List<Video> queryFromDBAllTitles(){
        SQLiteDatabase db = getWritableDatabase();
        List<Video> VideoTitle = new ArrayList<>();
        //De-duplication by titles
        Cursor cursor = db.query(true, Constant.TABLE_NAME, null, null, null, "title",null,null, null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String getTitle = cursor.getString(cursor.getColumnIndex("title"));

                Video video = new Video();
                video.setTitle(getTitle);
                VideoTitle.add(video);
            }
            cursor.close();
        }
        return VideoTitle;
    }

    public List<Video> queryFromDBByTitle(String str){
        SQLiteDatabase db = getWritableDatabase();
        List<Video> videoDetailList = new ArrayList<>();

        Cursor cursor = db.query(true, Constant.TABLE_NAME, null, "title like ?", new String[]{str}, "title",null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String getTitle = cursor.getString(cursor.getColumnIndex("title"));
                String getPath = cursor.getString(cursor.getColumnIndex("path"));
                String getComment = cursor.getString(cursor.getColumnIndex("comment"));
                String getEvaluate = cursor.getString(cursor.getColumnIndex("evaluate"));
                String getDate = cursor.getString(cursor.getColumnIndex("date"));

                Video video = new Video();
                video.setTitle(getTitle);
                video.setPath(getPath);
                video.setComment(getComment);
                video.setEvaluate(getEvaluate);
                video.setDate(getDate);

                videoDetailList.add(video);
            }
            cursor.close();
        }
        return videoDetailList;
    }

    public List<Video> queryFromDBByEvaluate1(){
        SQLiteDatabase db = getWritableDatabase();
        List<Video> videoDetailList = new ArrayList<>();
        String evaluate = "1.0";
        Cursor cursor = db.query(true, Constant.TABLE_NAME, null, "evaluate = ?", new String[]{evaluate}, "title",null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String getTitle = cursor.getString(cursor.getColumnIndex("title"));
                String getPath = cursor.getString(cursor.getColumnIndex("path"));
                String getComment = cursor.getString(cursor.getColumnIndex("comment"));
                String getEvaluate = cursor.getString(cursor.getColumnIndex("evaluate"));
                String getDate = cursor.getString(cursor.getColumnIndex("date"));

                Video video = new Video();
                video.setTitle(getTitle);
                video.setPath(getPath);
                video.setComment(getComment);
                video.setEvaluate(getEvaluate);
                video.setDate(getDate);

                videoDetailList.add(video);
            }
            cursor.close();
        }
        return videoDetailList;
    }

    public List<Video> queryFromDBByEvaluate2(){
        SQLiteDatabase db = getWritableDatabase();
        List<Video> videoDetailList = new ArrayList<>();
        String evaluate = "2.0";
        Cursor cursor = db.query(true, Constant.TABLE_NAME, null, "evaluate = ?", new String[]{evaluate}, "title",null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String getTitle = cursor.getString(cursor.getColumnIndex("title"));
                String getPath = cursor.getString(cursor.getColumnIndex("path"));
                String getComment = cursor.getString(cursor.getColumnIndex("comment"));
                String getEvaluate = cursor.getString(cursor.getColumnIndex("evaluate"));
                String getDate = cursor.getString(cursor.getColumnIndex("date"));

                Video video = new Video();
                video.setTitle(getTitle);
                video.setPath(getPath);
                video.setComment(getComment);
                video.setEvaluate(getEvaluate);
                video.setDate(getDate);

                videoDetailList.add(video);
            }
            cursor.close();
        }
        return videoDetailList;
    }

    public List<Video> queryFromDBByEvaluate3(){
        SQLiteDatabase db = getWritableDatabase();
        List<Video> videoDetailList = new ArrayList<>();
        String evaluate = "3.0";
        Cursor cursor = db.query(true, Constant.TABLE_NAME, null, "evaluate = ?", new String[]{evaluate}, "title",null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String getTitle = cursor.getString(cursor.getColumnIndex("title"));
                String getPath = cursor.getString(cursor.getColumnIndex("path"));
                String getComment = cursor.getString(cursor.getColumnIndex("comment"));
                String getEvaluate = cursor.getString(cursor.getColumnIndex("evaluate"));
                String getDate = cursor.getString(cursor.getColumnIndex("date"));

                Video video = new Video();
                video.setTitle(getTitle);
                video.setPath(getPath);
                video.setComment(getComment);
                video.setEvaluate(getEvaluate);
                video.setDate(getDate);

                videoDetailList.add(video);
            }
            cursor.close();
        }
        return videoDetailList;
    }

}
