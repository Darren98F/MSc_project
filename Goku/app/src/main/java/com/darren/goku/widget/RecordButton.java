package com.darren.goku.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author Lance
 * @date 2018/10/8
 */
public class RecordButton extends AppCompatTextView {

    private final String TAG = this.getClass().getName();

    private OnRecordListener mListener;

    public RecordButton(Context context) {
        super(context);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mListener == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN.......................................................................................................................................");
                mListener.onRecordStart();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "ACTION_UP.......................................................................................................................................");
                mListener.onRecordStop();
                break;
        }
        return true;
    }


    public void setOnRecordListener(OnRecordListener listener) {
        mListener = listener;
    }

    public interface OnRecordListener {
        void onRecordStart();
        void onRecordStop();
    }
}
