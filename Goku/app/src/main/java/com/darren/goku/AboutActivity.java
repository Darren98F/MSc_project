package com.darren.goku;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class AboutActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private TextView tvFunction, tvSC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Launch initialize in MeActivity......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tvFunction = findViewById(R.id.tv_Function);
        tvSC = findViewById(R.id.tv_CS);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // move back to HomeActivity
    public void backBtn(View view) {
        Intent intent = new Intent(AboutActivity.this, com.darren.goku.HomeActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideDown(this);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AboutActivity.this, com.darren.goku.HomeActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideDown(this);
        finish();
    }

    public void gotoFunction(View view) {
        Intent intent = new Intent(this, FunctionActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideLeft(this);
        finish();
    }

    public void gotoCS(View view) {
        Intent intent = new Intent(this, CSActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideLeft(this);
        finish();
    }
}
