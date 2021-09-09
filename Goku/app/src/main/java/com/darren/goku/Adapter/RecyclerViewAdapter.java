package com.darren.goku.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.darren.goku.Constant;
import com.darren.goku.Database.DBHelper;
import com.darren.goku.R;
import com.darren.goku.Models.Video;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = this.getClass().getName();
    private Context mContext;
    private clickListener listener;
    private DBHelper mDbHelper;
    private SQLiteDatabase mDb;

    //database
    private String myDate;
    private String strTitle;
    private String strPath;

    public RecyclerViewAdapter(Context mContext, clickListener listener){
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.files_list,parent,false);
        //create database
        mDbHelper = new DBHelper(mContext);

        return new FileLayoutHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //read from system
        ((FileLayoutHolder)holder).videoTitle.setText("Title: " + Constant.allMediaList.get(position).getName());

        Uri uri = Uri.fromFile(Constant.allMediaList.get(position));
//        ((FileLayoutHolder)holder).videoPath.setText("Path: " + Constant.allMediaList.get(position));
        System.out.println(uri);
        //load image and colors
//        Glide.with(mContext).load(uri).thumbnail(0.1f).into(((FileLayoutHolder)holder).thumbnail);
        Glide.with(mContext).load(uri)
                .listener(GlidePalette.with(String.valueOf(uri))
                        .use(GlidePalette.Profile.VIBRANT)
                        .intoBackground(((FileLayoutHolder)holder).textVibrant, GlidePalette.Swatch.RGB)
                        .intoTextColor(((FileLayoutHolder)holder).textVibrant, GlidePalette.Swatch.BODY_TEXT_COLOR)
                        .crossfade(true)
                        .use(GlidePalette.Profile.VIBRANT_DARK)
                        .intoBackground(((FileLayoutHolder)holder).textVibrantDark, GlidePalette.Swatch.RGB)
                        .intoTextColor(((FileLayoutHolder)holder).textVibrantDark, GlidePalette.Swatch.BODY_TEXT_COLOR)
                        .crossfade(false)
                        .use(GlidePalette.Profile.VIBRANT_LIGHT)
                        .intoBackground(((FileLayoutHolder)holder).textVibrantLight, GlidePalette.Swatch.RGB)
                        .intoTextColor(((FileLayoutHolder)holder).textVibrantLight, GlidePalette.Swatch.BODY_TEXT_COLOR)
                        .crossfade(true, 1000)

                        .use(GlidePalette.Profile.MUTED)
                        .intoBackground(((FileLayoutHolder)holder).textMuted, GlidePalette.Swatch.RGB)
                        .intoTextColor(((FileLayoutHolder)holder).textMuted, GlidePalette.Swatch.BODY_TEXT_COLOR)
                        .use(GlidePalette.Profile.MUTED_DARK)
                        .intoBackground(((FileLayoutHolder)holder).textMutedDark, GlidePalette.Swatch.RGB)
                        .intoTextColor(((FileLayoutHolder)holder).textMutedDark, GlidePalette.Swatch.BODY_TEXT_COLOR)
                        .crossfade(true, 2000)
                        .use(GlidePalette.Profile.MUTED_LIGHT)
                        .intoBackground(((FileLayoutHolder)holder).textMutedLight, GlidePalette.Swatch.RGB)
                        .intoTextColor(((FileLayoutHolder)holder).textMutedLight, GlidePalette.Swatch.BODY_TEXT_COLOR)
                )
                .into(((FileLayoutHolder)holder).thumbnail);

        //read data from database
        List<Video> videoDetailList = mDbHelper.queryFromDBByTitle(Constant.allMediaList.get(position).getName());
        String evaluate = "";
        String comment = "";
        String date = "";
        for (Video v: videoDetailList) {
            evaluate = v.getEvaluate().trim();
            comment = v.getComment().trim();
            date = v.getDate().trim();
            float ev = Float.parseFloat(evaluate);
            ((FileLayoutHolder)holder).ratingBar.setRating(ev);
            ((FileLayoutHolder)holder).videoComment.setText("Comment: " + comment);
            ((FileLayoutHolder)holder).videoDate.setText("Date: " + date);
        }

    }

    @Override
    public int getItemCount() {
        return Constant.allMediaList.size();
    }

    class FileLayoutHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail;
        TextView videoTitle;
        TextView videoComment;
        TextView videoDate;
        ImageView ic_more_btn;
        TextView textVibrant;
        TextView textVibrantLight;
        TextView textVibrantDark;
        TextView textMuted;
        TextView textMutedLight;
        TextView textMutedDark;
        RatingBar ratingBar;

        public FileLayoutHolder(@NonNull View itemView, final clickListener listener) {
            super(itemView);

            textVibrant = itemView.findViewById(R.id.textVibrant);
            textVibrantLight = itemView.findViewById(R.id.textVibrantLight);
            textVibrantDark = itemView.findViewById(R.id.textVibrantDark);
            textMuted = itemView.findViewById(R.id.textMuted);
            textMutedLight = itemView.findViewById(R.id.textMutedLight);
            textMutedDark = itemView.findViewById(R.id.textMutedDark);

            thumbnail = itemView.findViewById(R.id.thumbnail);
            videoTitle = itemView.findViewById(R.id.videotitle);
            videoComment = itemView.findViewById(R.id.videocomment);
            videoDate = itemView.findViewById(R.id.videodate);
            ic_more_btn = itemView.findViewById(R.id.ic_more_btn);

            videoTitle.setMovementMethod(new ScrollingMovementMethod());//set textview scrollable
            videoTitle.setScrollbarFadingEnabled(false);//Set the scrollbar to always display
            videoTitle.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    videoTitle.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            videoComment.setMovementMethod(new ScrollingMovementMethod());//set textview scrollable
            videoComment.setScrollbarFadingEnabled(false);//Set the scrollbar to always display
            videoComment.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    videoComment.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            ic_more_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != RecyclerView.NO_POSITION)
                        listener.onIconMoreClick(getAdapterPosition());
                }
            });
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

    public interface clickListener{
        //position is the same position of video in arraylist allMediaList
        void onIconMoreClick(int position);
    }

}
