package com.darren.goku;

import java.io.File;
import java.util.ArrayList;

public class Constant {
    public static String[] videoExtensions = {".mp4",".ts",".mkv",".mov",
            ".3gp",".mv2",".m4v",".webm",".mpeg1",".mpeg2",".mts",".ogm",
            ".bup", ".dv",".flv",".m1v",".m2ts",".mpeg4",".vlc",".3g2",
            ".avi",".mpeg",".mpg",".wmv",".asf"};

    //all loaded files will be here
    public static ArrayList<File> allMediaList = new ArrayList<>();

    //database
    public static final String DATABASE_NAME = "gokuPlayer.db";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "VideoList";

}
