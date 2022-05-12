package com.example.vs_player.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
    int position;
    String videoTitle;
    TextView title;
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
        player.prepare(concatenatingMediaSource);
        player.seekTo(position, C.TIME_UNSET);
        playError();

    }

    private void playError() {
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Toast.makeText(VideoPlayerActivity.this,"Video Playing Error", Toast.LENGTH_SHORT).show();
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