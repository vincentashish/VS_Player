package com.example.vs_player.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vs_player.R;
import com.example.vs_player.model.videoItem;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;

import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;

import java.io.File;
import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity {

    ArrayList<videoItem> mVideoItem = new ArrayList<>();
    PlayerView playerView;
    ExoPlayer player;
    ImageView playBtn;
    int position;
    String videoTitle;
    TextView title;
    ImageView videoBackBtn;
    ConcatenatingMediaSource concatenatingMediaSource;
    AudioAttributes audioAttributesV = new AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MOVIE)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.exoplayer_view);
        playBtn = findViewById(R.id.exo_play);
        playBtn.setOnClickListener(view -> {
            player.play();
            Log.d("Checkkk","Pressed on Play Button");
        });
        videoBackBtn = findViewById(R.id.video_back);
        videoBackBtn.setOnClickListener(view ->{
            finish();
        });
        //getSupportActionBar().hide();
        position = getIntent().getIntExtra("position", 1);
        videoTitle = getIntent().getStringExtra("video_title");
        mVideoItem = getIntent().getExtras().getParcelableArrayList("videoArrayList");

        title = findViewById(R.id.video_title);
        title.setText(videoTitle);

        playVideo();
    }



    private void playVideo() {


        player = new ExoPlayer.Builder(this)
                .setHandleAudioBecomingNoisy(true)
                .setAudioAttributes(audioAttributesV,true)
                .build();

        DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);
        concatenatingMediaSource = new ConcatenatingMediaSource();
        for (int i=0;i<mVideoItem.size();i++) {
            new File(String.valueOf(mVideoItem.get(i)));
            videoItem vItem = mVideoItem.get(i);
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(vItem.getUri())
                    .setMediaId(String.valueOf(vItem.getId()))
                    .setTag(null)
                    .build();
            Log.d("Checkkk",String.valueOf(vItem));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        player.setMediaSource(concatenatingMediaSource);
        player.prepare();
        player.seekTo(position, C.TIME_UNSET);
        playError();

    }

    private void playError() {

        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Player.Listener.super.onPlayerError(error);
                Log.d("Checkkk","Video Player Error: \n"+String.valueOf(error));
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                if(isPlaying){
                    Log.d("Checkkk","FrameRateStrategy: "+String.valueOf(player.getVideoChangeFrameRateStrategy())+"\n"
                            +"VideoFormat: "+String.valueOf(player.getVideoFormat())+"\n"
                            +"VideoSize: "+String.valueOf(player.getVideoSize())+"\n");
                }
                else{
                    Log.d("Checkkk","VideoPlayerError: "+String.valueOf(player.getPlayerError())+"\n");
                }
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                Log.d("Checkkk","PlaybackState: "+String.valueOf(playbackState));
            }
        });

        player.setPlayWhenReady(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Checkkk","VideoPlayerActivity Started");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Checkkk","VideoPlayerActivity Stopped");
        this.player.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Checkkk","VideoPlayerActivity Paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Checkkk","VideoPlayerActivity Resumed");
    }
}