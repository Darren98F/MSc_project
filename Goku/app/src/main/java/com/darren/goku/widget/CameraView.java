package com.darren.goku.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class CameraView extends GLSurfaceView {
    private  CameraRender renderer;
    private Speed mSpeed = Speed.MODE_NORMAL;

    public enum Speed {
        MODE_EXTRA_SLOW, MODE_SLOW, MODE_NORMAL, MODE_FAST, MODE_EXTRA_FAST
    }

    public CameraView(Context context) {
        this(context,null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //使用OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        //設置渲染回調接口
        renderer = new CameraRender(this);
        setRenderer(renderer);

        /**
         *     RENDERMODE_WHEN_DIRTY
         *     RENDERMODE_CONTINUOUSLY
         */
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);



    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        renderer.onSurfaceDestroyed();
    }

    public void setSpeed(Speed speed) {
        this.mSpeed = speed;
    }

    public void startRecord(){
        //速度  时间/速度 speed小于就是放慢 大于1就是加快
        float speed = 1.f;
        switch (mSpeed) {
            case MODE_EXTRA_SLOW:
                speed = 0.3f;
                break;
            case MODE_SLOW:
                speed = 0.5f;
                break;
            case MODE_NORMAL:
                speed = 1.f;
                break;
            case MODE_FAST:
                speed = 2.f;
                break;
            case MODE_EXTRA_FAST:
                speed = 5.f;
                break;
        }
        renderer.startRecord(speed);
    }

    public void stopRecord(){
        renderer.stopRecord();
    }
}
