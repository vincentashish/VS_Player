package com.example.eaudioplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eaudioplayer.R;
import com.example.eaudioplayer.conversions;
import com.example.eaudioplayer.model.videoItem;
import com.example.eaudioplayer.view.VideoPlayerActivity;
import com.google.android.exoplayer2.ExoPlayer;

import java.util.ArrayList;
import java.util.List;

public class VideoItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<videoItem> videoItemList;
    private Context context;
    ExoPlayer player;

    public VideoItemAdapter(List<videoItem> videoItemList, ExoPlayer player, Context context) {
        this.videoItemList = videoItemList;
        this.player = player;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_row_item,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        videoItem vItem = videoItemList.get(position);
        VideoViewHolder viewHolder = (VideoViewHolder) holder;

        viewHolder.titleHolder.setText(vItem.getName());
        viewHolder.sizeHolder.setText(conversions.sizeConversion(vItem.getSize()));
        viewHolder.durationHolder.setText(conversions.timeConversion(vItem.getDuration()));
        viewHolder.mimeTypeHolder.setText(vItem.getMimeType());
        viewHolder.numberHolder.setText(String.valueOf(position+1));

        //OnClick ItemView
        viewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("position",position);
            intent.putExtra("video_title",videoItemList.get(position).getName());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("videoArrayList",(ArrayList<? extends Parcelable>) videoItemList);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return videoItemList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout rowItemLayout;
        TextView numberHolder, titleHolder,sizeHolder,durationHolder,mimeTypeHolder;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            rowItemLayout = itemView.findViewById(R.id.VideoRowItemLayout);
            titleHolder = itemView.findViewById(R.id.video_title);
            numberHolder = itemView.findViewById(R.id.video_number);
            sizeHolder = itemView.findViewById(R.id.video_size);
            durationHolder = itemView.findViewById(R.id.video_duration);
            mimeTypeHolder = itemView.findViewById(R.id.video_mimeType);
        }
    }
}
