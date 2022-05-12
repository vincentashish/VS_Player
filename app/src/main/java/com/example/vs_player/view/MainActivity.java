package com.example.vs_player.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vs_player.R;
import com.example.vs_player.adapter.ViewPagerAdapter;
import com.example.vs_player.view.tabs.AudioFragment;
import com.example.vs_player.view.tabs.VideoFragment;
import com.example.vs_player.view.tabs.WebFragment;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<String> storagePermissionLauncher;

    TextView playingSongNameView, skipPrevSongBtn, skipNextSongBtn;
    ImageButton playPauseBtn,repeatBtn;
    ImageView albumArtHolder;
    Fragment audioFragment;
    AudioPlayer audioPlayer;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playingSongNameView = findViewById(R.id.playingSongNameView);
        playingSongNameView.setSelected(true); //for marquee
        skipPrevSongBtn = findViewById(R.id.prevBtn);
        skipNextSongBtn = findViewById(R.id.nextBtn);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        repeatBtn = findViewById(R.id.exo_repeat_toggle);
        albumArtHolder = findViewById(R.id.albumart);

       audioPlayer = new AudioPlayer();
       audioPlayer.initPlayer(getApplicationContext());



        playerControls();

        storagePermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if(!result){
                onPermissionDenied();
            }
        });
        //Storage Permission Launcher
        storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        audioFragment = new AudioFragment(audioPlayer.player);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerAdapter.addFragment(audioFragment,"Audio");
        viewPagerAdapter.addFragment(new VideoFragment(),"Video");
        viewPagerAdapter.addFragment(new WebFragment(),"Web");
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setText(viewPagerAdapter.getFragmentTitle().get(position));
                }
        ).attach();

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onPermissionDenied() {
        //
        if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(this)
                    .setTitle("Requesting Permission")
                    .setMessage("This permission is required to fetch song on your device")
                    .setPositiveButton("Allow", (dialogInterface, i) -> storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    .setNegativeButton("Deny", (dialogInterface, i) -> Toast.makeText(getApplicationContext(),"You denied to fetch songs", Toast.LENGTH_LONG).show())
                    .show();
        }
        else{
            Toast.makeText(this,"You denied to fetch songs", Toast.LENGTH_LONG).show();
        }
    }

    private void playerControls() {

        repeatBtn.setOnClickListener(view -> {
            if(audioPlayer.player.getRepeatMode() == ExoPlayer.REPEAT_MODE_ALL){
                audioPlayer.player.setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
                repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_one);
            }
            else{
                audioPlayer.player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
                repeatBtn.setImageResource(R.drawable.ic_baseline_repeat);
            }
        });

        skipNextSongBtn.setOnClickListener(view -> {
            if(audioPlayer.player.hasNextMediaItem()){
                audioPlayer.player.seekToNext();
                playIfPaused();
            }
        });

        skipPrevSongBtn.setOnClickListener(view -> {
            if(audioPlayer.player.hasPreviousMediaItem()){
                audioPlayer.player.seekToPrevious();
                playIfPaused();
            }
        });

        playPauseBtn.setOnClickListener(view -> {

            if(audioPlayer.player.isPlaying()){
                audioPlayer.player.pause();
                playPauseBtn.setImageResource(R.drawable.ic_play_circle_filled);
            }else{
                if(audioPlayer.player.getMediaItemCount()>0){
                    audioPlayer.player.play();
                    playPauseBtn.setImageResource(R.drawable.ic_pause_circle_filled);
                }
            }
        });

        //player listener
        playerlistener();
    }

    private void playerlistener() {
        audioPlayer.player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                if(!isPlaying){
                    playPauseBtn.setImageResource(R.drawable.ic_play_circle_filled);
                }
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                // Player.Listener.super.onMediaItemTransition(mediaItem, reason);
                assert mediaItem != null;
                playingSongNameView.setText(mediaItem.mediaMetadata.title);
            }
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                //   Player.Listener.super.onPlaybackStateChanged(playbackState);
                if(playbackState == ExoPlayer.STATE_READY){
                    playingSongNameView.setText(Objects.requireNonNull(audioPlayer.player.getCurrentMediaItem()).mediaMetadata.title);
                    playPauseBtn.setImageResource(R.drawable.ic_pause_circle_filled);

                    //Setting Album art
                    Uri albumArtUri = audioPlayer.player.getCurrentMediaItem().mediaMetadata.artworkUri;
                    //viewHolder.albumArtHolder.setImageResource(R.drawable.d_album_art);
                    if(null != albumArtUri){
                        albumArtHolder.setImageURI(albumArtUri);

                        if(null == albumArtHolder.getDrawable()){
                            albumArtHolder.setImageResource(R.drawable.d_album_art);
                        }
                    }
                    else{
                        albumArtHolder.setImageResource(R.drawable.d_album_art);
                    }

                }
            }
        });
    }

    private void playIfPaused() {
        if(audioPlayer.player.isPlaying()) return;
        audioPlayer.player.play();
        playPauseBtn.setImageResource(R.drawable.ic_pause_circle_filled);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer.player.release();
    }
}