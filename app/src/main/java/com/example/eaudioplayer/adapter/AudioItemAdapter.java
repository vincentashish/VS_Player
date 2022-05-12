package com.example.eaudioplayer.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eaudioplayer.R;
import com.example.eaudioplayer.conversions;
import com.example.eaudioplayer.model.audioItem;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;

import java.util.ArrayList;
import java.util.List;

public class AudioItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<audioItem> audioFiles;
    ExoPlayer player;

    public AudioItemAdapter(List<audioItem> audioFiles, ExoPlayer player) {

        this.audioFiles = audioFiles;
        this.player = player;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.audio_row_item,parent,false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //current song and view holder
        audioItem audioFile = audioFiles.get(position);
        AudioViewHolder viewHolder = (AudioViewHolder) holder;

        // set values to view
        viewHolder.titleHolder.setText(audioFile.getName());
        viewHolder.sizeHolder.setText(conversions.sizeConversion(audioFile.getSize()));
        viewHolder.numberHolder.setText(String.valueOf(position+1));
        viewHolder.durationHolder.setText(conversions.timeConversion(audioFile.getDuration()));

        String artistHolderString;
        String ua = "<Unknown>";
        if(audioFile.getArtist().equalsIgnoreCase(ua)){
            artistHolderString = "Unknown Artist";
        }
        else{
            artistHolderString = audioFile.getArtist();
        }
        viewHolder.artistHolder.setText(artistHolderString);
        viewHolder.mimeTypeHolder.setText(audioFile.getMimeType().substring(6));


        Log.d("Checkkk","---------------Text is Set---------------->"+audioFile.getName()+" "+audioFile.getArtist());

        //onclick item
        viewHolder.rowItemLayout.setOnClickListener(view -> {
            //media item
            //MediaItem mediaItem = getMediaItem(audioFile);
            if(!player.isPlaying()){
                player.setMediaItems(getMediaItems(),position, 0);

            }else {
                player.pause();
                player.seekTo(position, 0);
            }
            player.prepare();
            player.play();
//            Toast.makeText(view.getContext(), "Playing: "+audioFile.getName(), Toast.LENGTH_LONG).show();
        });

    }

    private List<MediaItem> getMediaItems() {
        List<MediaItem> mediaItems = new ArrayList<>();
        for (audioItem audioFile : audioFiles){
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(audioFile.getUri())
                    .setMediaMetadata(getMetadata(audioFile))
                    .build();
            mediaItems.add(mediaItem);
        }
        return mediaItems;
    }

    private MediaItem getMediaItem(audioItem audioFile) {
        return new MediaItem.Builder()
                .setUri(audioFile.getUri())
                .setMediaMetadata(getMetadata(audioFile))
                .build();
    }

    private MediaMetadata getMetadata(audioItem audioFile) {
        return new MediaMetadata.Builder()
                .setTitle(audioFile.getName())
                .setArtworkUri(audioFile.getAlbumArtUri())
                .build();
    }


    @Override
    public int getItemCount() {
        return audioFiles.size();
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout rowItemLayout;
        TextView numberHolder, titleHolder,sizeHolder,durationHolder,artistHolder,mimeTypeHolder;
        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            rowItemLayout = itemView.findViewById(R.id.AudioRowItemLayout);
            titleHolder = itemView.findViewById(R.id.audio_title);
            numberHolder = itemView.findViewById(R.id.audio_number);
            sizeHolder = itemView.findViewById(R.id.audio_size);
            durationHolder = itemView.findViewById(R.id.audio_duration);
            artistHolder = itemView.findViewById(R.id.artist);
            mimeTypeHolder = itemView.findViewById(R.id.audio_mimeType);
        }
    }

}
