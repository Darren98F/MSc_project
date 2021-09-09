package com.darren.goku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.darren.goku.Adapter.RecyclerViewAdapter;
import com.darren.goku.Database.DBHelper;
import com.darren.goku.Models.Video;

import java.util.List;

public class InsertActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    //ui
    private TextView tvDate, tvTitle, tvPath;
    private EditText etComment;
    private RatingBar rbEvaluate;
    private Button btnSave, btnUpdate;
    //database
    private String haveTitle;
    private String myDate;
    private String strTitle;
    private String strPath;
    private String strComment;
    private float starNum;
    DBHelper helper;
    SQLiteDatabase mSQLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Launch initialize in InsertActivity......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ui
        tvDate = findViewById(R.id.tv_date_insert);
        tvTitle = findViewById(R.id.tv_title_insert);
        tvPath = findViewById(R.id.tv_path_insert);
        etComment = findViewById(R.id.et_comment_insert);
        rbEvaluate = findViewById(R.id.rb_evaluate_insert);
        btnSave = findViewById(R.id.btn_save_insert);
        btnUpdate = findViewById(R.id.btn_update_insert);

        //Get the data from BottomSheetDialog
        myDate = getIntent().getStringExtra("date");
        strTitle = getIntent().getStringExtra("title");
        strPath = getIntent().getStringExtra("path");


        //Database creation
        helper = new DBHelper(InsertActivity.this);
        mSQLiteDatabase = helper.getWritableDatabase();


        tvDate.setText("Date: \n" + myDate);
        tvTitle.setText("Video Title: \n" + strTitle);
        tvPath.setText("Path: \n" + strPath);

        //read from database
        List<Video> videoDetailList = helper.queryFromDBByTitle(strTitle);
        String comment = "";
        String evaluate = "";
        for (Video v: videoDetailList) {
            comment = v.getComment().trim();
            etComment.setText(comment);
            evaluate = v.getEvaluate().trim();
            float ev = Float.parseFloat(evaluate);
            rbEvaluate.setRating(ev);

            haveTitle = v.getTitle().trim();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(strTitle.equals(haveTitle)){
                   Toast.makeText(InsertActivity.this,"Already inserted, need update?",Toast.LENGTH_SHORT).show();
               }else{
                   //get the user input data
                   strComment = etComment.getText().toString();
                   starNum = rbEvaluate.getRating();
                   writeDatabase();
                   Intent intent = new Intent(InsertActivity.this, HomeActivity.class);
                   startActivity(intent);
                   finish();
               }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(strTitle.equals((haveTitle))){
                   strComment = etComment.getText().toString();
                   starNum = rbEvaluate.getRating();
                   updateDatabase(strTitle);
                   Intent intent = new Intent(InsertActivity.this, HomeActivity.class);
                   startActivity(intent);
                   finish();
               }else {
                   Toast.makeText(InsertActivity.this,"Please save first!",Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    //functions to insert data into database
    private void writeDatabase() {
        Log.d(TAG, "write to database........");
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", strTitle);
        contentValues.put("path", strPath);
        contentValues.put("date", myDate);
        contentValues.put("comment", strComment);
        contentValues.put("evaluate", starNum);
        mSQLiteDatabase.insert(Constant.TABLE_NAME,null,contentValues);
        Toast.makeText(this, "Insert successfully!", Toast.LENGTH_SHORT).show();
        //close database
        helper.close();
        mSQLiteDatabase.close();
    }

    public void updateDatabase(String title){
        Log.d(TAG, "update to database........");
        ContentValues contentValues = new ContentValues();
        contentValues.put("comment", strComment);
        contentValues.put("evaluate", starNum);
        mSQLiteDatabase.update(Constant.TABLE_NAME, contentValues, "title like ?", new String[]{title});
        Toast.makeText(this, "Update successfully!", Toast.LENGTH_SHORT).show();
        //close database
        helper.close();
        mSQLiteDatabase.close();
    }


    public void backBtn(View view) {
        Intent intent = new Intent(InsertActivity.this, HomeActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideRight(this);
        finish();
    }
}