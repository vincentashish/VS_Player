package com.example.vs_player.view;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.vs_player.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ui.PlayerView;

public class WebPlayerActivity extends AppCompatActivity {

    PlayerView playerView;
    ExoPlayer webPlayer;
//    String url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_player);
        playerView = findViewById(R.id.web_player_view);
        url = getIntent().getStringExtra("web_url");
        webPlayer = new ExoPlayer.Builder(this)
                .setHandleAudioBecomingNoisy(true)
                .setAudioAttributes(new AudioAttributes.Builder().setUsage(C.USAGE_MEDIA).setContentType(C.CONTENT_TYPE_MOVIE).build(),true)
                .build();
        if(url != null){
            webPlayer.addMediaItem(MediaItem.fromUri(Uri.parse(url)));
            webPlayer.prepare();
            playerView.setPlayer(webPlayer);
            playerView.setKeepScreenOn(true);
            webPlayer.setPlayWhenReady(true);
        }
        else{
            Log.d("Checkkk","URL IS NULL");
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        webPlayer.release();
    }
}