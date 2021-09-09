package com.darren.goku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class VideoPlayer extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    //Initialize variable
    private PlayerView playerView;
    private ProgressBar progressBar;
    private ImageView btFullscreen;
    private SimpleExoPlayer simpleExoPlayer;
    private Boolean flag = false;
    private String videoUrl;
    private Button btnSpeed, btnSpeed1, btnSpeed2, btnSpeed3;

    public static VideoPlayer newInstance() {
        return new VideoPlayer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Launch VideoPlayerAvtivity......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Assign variable
        playerView = findViewById(R.id.video_pv);
        progressBar = findViewById(R.id.progress_bar);
        btFullscreen = playerView.findViewById(R.id.bt_fullscreen);
        videoUrl = getIntent().getStringExtra("path");
        btnSpeed = playerView.findViewById(R.id.btn_speed);
        btnSpeed1 = playerView.findViewById(R.id.btn_speed1);
        btnSpeed2 = playerView.findViewById(R.id.btn_speed2);
        btnSpeed3 = playerView.findViewById(R.id.btn_speed3);

        String str = "file://" + videoUrl;
        //video url
        Uri videoUrl = Uri.parse(str);

        //Initialize load control
        LoadControl loadControl = new DefaultLoadControl();

        //Initialize band width meter
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        //Initialize track selector
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

        //Initialize simple exo player
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        //Initialize data source factory
        DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");

        //Initialize extractors factory
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        //Initialize media source
        MediaSource mediaSource = buildMediaSource(videoUrl);

        //Set player
        playerView.setPlayer(simpleExoPlayer);

        //Keep screen on
        playerView.setKeepScreenOn(true);

        //Prepare media
        simpleExoPlayer.prepare(mediaSource);

        //set up video speed
        btnSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set speed
                PlaybackParameters param = new PlaybackParameters(0.5f);
                simpleExoPlayer.setPlaybackParameters(param);
            }
        });
        btnSpeed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set speed
                PlaybackParameters param = new PlaybackParameters(1.0f);
                simpleExoPlayer.setPlaybackParameters(param);
            }
        });
        btnSpeed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set speed
                PlaybackParameters param = new PlaybackParameters(1.5f);
                simpleExoPlayer.setPlaybackParameters(param);
            }
        });
        btnSpeed3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set speed
                PlaybackParameters param = new PlaybackParameters(2.0f);
                simpleExoPlayer.setPlaybackParameters(param);
            }
        });

        //play video when ready
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                //check condition
                switch (playbackState){
                    case Player.STATE_BUFFERING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                    case Player.STATE_READY:
                            progressBar.setVisibility(View.GONE);
                            break;

                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        btFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check condition
                if (flag){
                    //when flag is true
                    //set enter full screen image
                    btFullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
                    //set portrait orientation
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    //set flag value false
                    flag = false;
                }else {
                    //when flag is false
                    //set exit full screen image
                    btFullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                    //set portrait orientation
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    //set flag value true
                    flag = true;
                }
            }
        });

    }

    private MediaSource buildMediaSource(Uri uri) {
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, "com.example.videoapp.Video");
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        return videoSource;
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop video when ready
        simpleExoPlayer.setPlayWhenReady(false);
        //get playback state
        simpleExoPlayer.getPlaybackState();
        simpleExoPlayer.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        //play video when ready
        simpleExoPlayer.setPlayWhenReady(true);
        //get playback state
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        simpleExoPlayer.release();
    }

}