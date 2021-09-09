package com.darren.goku.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.darren.goku.R;
import com.darren.goku.VideoPlayer;

import java.util.ArrayList;


public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private ArrayList<String> mDatas;

    public SearchRecyclerViewAdapter(Context context, ArrayList<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        return new NormalHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalHolder normalHolder = (NormalHolder) holder;
        normalHolder.videoTitle.setText(mDatas.get(position));
        String path = mDatas.get(position);
        String str = "file://" + path;
        Uri videoUrl = Uri.parse(str);

        Glide.with(mContext).load(videoUrl).thumbnail(0.1f).into(normalHolder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView videoTitle;


        public NormalHolder(View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.tv_videoTitle);
            thumbnail = itemView.findViewById(R.id.iv_searchThumbnail);

            videoTitle.setMovementMethod(new ScrollingMovementMethod());//set textview scrollable
            videoTitle.setScrollbarFadingEnabled(false);//Set the scrollbar to always display
            videoTitle.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    videoTitle.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Play: "+videoTitle.getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, VideoPlayer.class);
                    //data transfer
                    intent.putExtra("path", videoTitle.getText().toString());
                    mContext.startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Animatoo.animateSlideLeft(mContext);
                }
            });
        }

    }
}
