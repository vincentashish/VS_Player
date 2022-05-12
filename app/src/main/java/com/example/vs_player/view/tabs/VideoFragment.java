package com.example.vs_player.view.tabs;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vs_player.R;
import com.example.vs_player.adapter.VideoItemAdapter;
import com.example.vs_player.model.videoItem;
import com.google.android.exoplayer2.ExoPlayer;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {

    RecyclerView recyclerView;
    VideoItemAdapter videoItemAdapter;
    LinearLayoutManager layoutManager;
    ExoPlayer player;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(ContextCompat.checkSelfPermission(requireContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            fetchVideoList();
        }
    }

    private void fetchVideoList(){
        List<videoItem> videoItemList = new ArrayList<>();
        Uri videoLibrary;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            videoLibrary = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }
        else{
            videoLibrary = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.MIME_TYPE
        };

        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";

        try(Cursor cursor = requireContext().getContentResolver().query(videoLibrary,projection,null,null,sortOrder)){

            int idCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int mimeTypeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE);

            while(cursor.moveToNext()){
                long id = cursor.getLong(idCol);
                String name = cursor.getString(nameCol);
                int duration = cursor.getInt(durationCol);
                int size = cursor.getInt(sizeCol);
                String mimeType = cursor.getString(mimeTypeCol);

                Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,id);

                name = name.substring(0,name.lastIndexOf("."));

                videoItem vItem = new videoItem(
                        id,
                        uri,
                        name,
                        duration,
                        size,
                        mimeType
                );

                Log.d("Checkkk",
                        " id: "+id+" name: "+name+" duration: "+duration+" size: "+size+" MIME Type: "+mimeType);

                videoItemList.add(vItem);

            }

            showVideoItems(videoItemList);

        }

    }

    private void showVideoItems(List<videoItem> videoItemList) {
        videoItemAdapter = new VideoItemAdapter(videoItemList,player,getContext());
        recyclerView = requireView().findViewById(R.id.videoRecyclerView);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(videoItemAdapter);
    }
}

