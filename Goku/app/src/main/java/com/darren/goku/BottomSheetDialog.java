package com.darren.goku;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Date;


public class BottomSheetDialog extends BottomSheetDialogFragment {

    private TextView videoTitle;
    private LinearLayout deleteLayout;
    private LinearLayout insertLayout;
    private LinearLayout playLayout;
    private int videoPosition;

    //database
    private String myDate;
    private String strTitle;
    private String strPath;
    public interface bottomSheetListener {
        void deleteVideoFromList();
    }
    bottomSheetListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheetlayout, container, false);

        videoTitle = view.findViewById(R.id.videotitle);
        deleteLayout = view.findViewById(R.id.deletelayout);
        insertLayout = view.findViewById(R.id.insertlayout);
        playLayout = view.findViewById(R.id.playlayout);

        videoTitle.setMovementMethod(new ScrollingMovementMethod());//set textview scrollable
        videoTitle.setScrollbarFadingEnabled(false);//Set the scrollbar to always display
        videoTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                videoTitle.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        setBottomSheetTitle();
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteVideoFromList();
                dismiss();
            }
        });

        //set date
        Date date = new Date(System.currentTimeMillis());
        myDate = date.toString();
        //get data which need to insert
        strTitle = Constant.allMediaList.get(videoPosition).getName();
        strPath = Constant.allMediaList.get(videoPosition).toString();

        insertLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InsertActivity.class);
                //data transfer
                intent.putExtra("date", myDate);
                intent.putExtra("title", strTitle);
                intent.putExtra("path", strPath);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Animatoo.animateSlideLeft(getActivity());

            }
        });

        playLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VideoPlayer.class);
                //data transfer
                intent.putExtra("path", strPath);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Animatoo.animateSlideLeft(getActivity());
            }
        });
        return view;
    }

    private void setBottomSheetTitle() {
        //set video title
        videoTitle.setText("Video title: " + Constant.allMediaList.get(videoPosition).getName()+"\n" +
                            "Video Path: " + Constant.allMediaList.get(videoPosition).toString());
    }

    void setVideoPosition(int position){
        videoPosition = position;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (bottomSheetListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement BottomSheetListener");
        }
    }
}
