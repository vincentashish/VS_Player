package com.example.eaudioplayer.view;

import android.content.Context;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;

public class AudioPlayer {
    ExoPlayer player;
    AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .build();


    public void initPlayer(Context context){
        player = new ExoPlayer.Builder(context)
                .setAudioAttributes(audioAttributes,true)
                .setHandleAudioBecomingNoisy(true)
                .build();
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
    }
}
