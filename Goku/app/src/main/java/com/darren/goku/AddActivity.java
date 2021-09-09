package com.darren.goku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.darren.goku.widget.CameraView;
import com.darren.goku.widget.RecordButton;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class AddActivity extends AppCompatActivity implements RecordButton.OnRecordListener, RadioGroup.OnCheckedChangeListener {

    private CameraView cameraView;
    private Chronometer chronometer;
    private Button lightButton, backButton, saveButton;
    public int current = 0;
    final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        myRequestPermission();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cameraView = findViewById(R.id.cameraView);
        RecordButton btn_record = findViewById(R.id.btn_record);
        btn_record.setOnRecordListener(this);

        RadioGroup rgSpeed = findViewById(R.id.rg_speed);
        rgSpeed.setOnCheckedChangeListener(this);

        //record time
        chronometer = findViewById(R.id.recordTime);
        chronometer.setText(chronometerFormat(current));

        //light (start or stop)
        lightButton = findViewById(R.id.point);

        // move back to HomeActivity
        backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        animation.setDuration(500); // duration - half a second

        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate

        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely

        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

    }

    private void myRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            Toast.makeText(this,"PERMISSION GRANTED!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btn_extra_slow:
                cameraView.setSpeed(CameraView.Speed.MODE_EXTRA_SLOW);
                break;
            case R.id.btn_slow:
                cameraView.setSpeed(CameraView.Speed.MODE_SLOW);
                break;
            case R.id.btn_normal:
                cameraView.setSpeed(CameraView.Speed.MODE_NORMAL);
                break;
            case R.id.btn_fast:
                cameraView.setSpeed(CameraView.Speed.MODE_FAST);
                break;
            case R.id.btn_extra_fast:
                cameraView.setSpeed(CameraView.Speed.MODE_EXTRA_FAST);
                break;
        }
    }

    public static String chronometerFormat(int time){
        String hh=time/3600>9?time/3600+"":"0"+time/3600;
        String mm=(time% 3600)/60>9?(time% 3600)/60+"":"0"+(time% 3600)/60;
        String ss=(time% 3600) % 60>9?(time% 3600) % 60+"":"0"+(time% 3600) % 60;
        return hh+":"+mm+":"+ss;
    }

    @Override
    public void onRecordStart() {
        Toast toast = Toast.makeText(this, "RECORD START, Hold to Continue, Release to Stop", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        cameraView.startRecord();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        lightButton.startAnimation(animation);
    }

    @Override
    public void onRecordStop() {
        Toast toast = Toast.makeText(this, "RECORD STOP", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        cameraView.stopRecord();
        chronometer.stop();
        lightButton.clearAnimation();

    }

    // move back to HomeActivity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddActivity.this, HomeActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideDown(this);
        finish();
    }


}
