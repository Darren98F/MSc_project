package com.darren.goku;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.darren.goku.Adapter.SearchRecyclerViewAdapter;
import com.darren.goku.Database.DBHelper;
import com.darren.goku.Models.Video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private String searchTitle;
    private DBHelper mDbHelper;
    private AutoCompleteTextView myAutoCompleteTextView;
    private Button startButton1, startButton2, startButton3, searchBtn;
    private RecyclerView rvResult;
    private final ArrayList<String> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Launch initialize in SearchActivity......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myAutoCompleteTextView = findViewById(R.id.myAutoCompleteTextView);
        startButton1 = findViewById(R.id.btn_evaluate1);
        startButton2 = findViewById(R.id.btn_evaluate2);
        startButton3 = findViewById(R.id.btn_evaluate3);
        searchBtn = findViewById(R.id.btn_search);

        myAutoCompleteTextView.setMovementMethod(new ScrollingMovementMethod());//set textview scrollable
        myAutoCompleteTextView.setScrollbarFadingEnabled(false);//Set the scrollbar to always display
        myAutoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myAutoCompleteTextView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //create database
        mDbHelper = new DBHelper(this);

        //get all titles
        List<Video> videoTitles = mDbHelper.queryFromDBAllTitles();
        String titles = "";
        for (Video video: videoTitles) {
            titles += video.getTitle() + ",";
        }
        //make it to String[]
        String[] titles1 = titles.split(",");
        ArrayAdapter<String> aa = new ArrayAdapter<String>( this, android.R.layout.simple_dropdown_item_1line, titles1);
        myAutoCompleteTextView.setAdapter(aa);   //Set up the adapter
        myAutoCompleteTextView.setThreshold(1);  //Define the number of characters that need to be entered by the user

        //recyclerView setting
        rvResult = findViewById(R.id.rv_result);
        //线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResult.setLayoutManager(linearLayoutManager);
        SearchRecyclerViewAdapter adapter = new SearchRecyclerViewAdapter(this, mDatas);
        rvResult.setAdapter(adapter);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.clear();
                searchTitle = myAutoCompleteTextView.getText().toString().trim();
                List<Video> videoDetailList = mDbHelper.queryFromDBByTitle(searchTitle);
                String res = "";
                for (Video video: videoDetailList) {
//                    res += "Title: " +video.getTitle() + ", Path: " + video.getPath() + ", Comment: " +video.getComment() + ", Evaluate: " + video.getEvaluate() + ", Date: " + video.getDate() + "\n";
                    res += video.getPath() + ",";
                }
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(res.split(",")));
                for (int i=0; i<list.size(); i++){
                    mDatas.add(list.get(i));
                }
                adapter.notifyDataSetChanged();
//                if(res != ""){
//                    Toast.makeText(this,"Video found", Toast.LENGTH_SHORT).show();
////            adapter.notifyDataSetChanged();
//                }else{
//                    Toast.makeText(this,"Video not found", Toast.LENGTH_SHORT).show();
//                }
                //close database
                mDbHelper.close();
            }
        });
        startButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.clear();
                List<Video> videoDetailList = mDbHelper.queryFromDBByEvaluate1();
                String res = "";
                for (Video video: videoDetailList) {
                    res += video.getPath() + ",";
                }
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(res.split(",")));
                for (int i=0; i<list.size(); i++){
                    mDatas.add(list.get(i));
                }
//                myAutoCompleteTextView.setText("Find videos which evaluate is 1");
                adapter.notifyDataSetChanged();

                //close database
                mDbHelper.close();
            }
        });

        startButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.clear();
                List<Video> videoDetailList = mDbHelper.queryFromDBByEvaluate2();
                String res = "";
                for (Video video: videoDetailList) {
                    res += video.getPath() + ",";
                }
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(res.split(",")));
                for (int i=0; i<list.size(); i++){
                    mDatas.add(list.get(i));
                }
//                myAutoCompleteTextView.setText("Find videos which evaluate is 2");
                adapter.notifyDataSetChanged();

                //close database
                mDbHelper.close();
            }
        });

        startButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.clear();
                List<Video> videoDetailList = mDbHelper.queryFromDBByEvaluate3();
                System.out.println("videoDetailList size: " + videoDetailList.size());
                String res = "";
                for (Video video: videoDetailList) {
                    res += video.getPath() + ",";
                }
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(res.split(",")));
                for (int i=0; i<list.size(); i++){
                    mDatas.add(list.get(i));
                }
//                myAutoCompleteTextView.setText("Find videos which evaluate is 3");
                adapter.notifyDataSetChanged();

                //close database
                mDbHelper.close();
            }
        });


    }

    // move back to HomeActivity
    public void backBtn(View view) {
        Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideDown(this);
        finish();
    }
    // move back to HomeActivity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchActivity.this, com.darren.goku.HomeActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideDown(this);
        finish();
    }


}