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
import com.example.vs_player.adapter.AudioItemAdapter;
import com.example.vs_player.model.audioItem;
import com.google.android.exoplayer2.ExoPlayer;

import java.util.ArrayList;
import java.util.List;

public class AudioFragment extends Fragment {

    RecyclerView recyclerView;
    AudioItemAdapter audioItemAdapter;
    LinearLayoutManager layoutManager;
    ExoPlayer player;

    public AudioFragment(ExoPlayer player){
        this.player = player;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_audio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(ContextCompat.checkSelfPermission(requireContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            fetchAudioList();
        }



    }

    private void fetchAudioList() {
        List<audioItem> audioItemList = new ArrayList<>();
        Uri audioLibrary;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            audioLibrary = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }
        else{
            audioLibrary = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        //Projecting
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.MIME_TYPE
        };

        String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";

        Log.d("Checkkk", "Search Path: " + String.valueOf(audioLibrary));

        //Querying
        try(Cursor cursor = requireContext().getContentResolver().query(audioLibrary,projection,null,null,sortOrder)){

            Log.d("Checkkk", "Getting columns from: " + String.valueOf(audioLibrary));

            //cache cursor indices
            int idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int albumIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
            int artistCol = cursor. getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int mimeTypeCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE);

            //getting values
            while (cursor.moveToNext()) {

                long id = cursor.getLong(idCol);
                String name = cursor.getString(nameCol);
                int duration = cursor.getInt(durationCol);
                int size = cursor.getInt(sizeCol);
                long albumId = cursor.getLong(albumIdCol);
                String artist = cursor.getString(artistCol);
                String mimeType = cursor.getString(mimeTypeCol);

                //Audio uri
                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);

                //album art uri
                Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),albumId);
                //Uri albumArtUri = null;
                //filter the name without extensions
                name = name.substring(0,name.lastIndexOf("."));

                //Set AudioItem
                audioItem aItem = new audioItem(id,
                        uri,
                        name,
                        artist,
                        duration,
                        size,
                        albumId,
                        albumArtUri,
                        mimeType);

                //add AudioItem to Items list
                Log.d("Checkkk",
                        " id: "+id+" name: "+name+" duration: "+duration+" size: "+size+" albumId: "+albumId+" artist: "+artist+" MIME Type: "+mimeType);
                audioItemList.add(aItem);

            }

            //show AudioItems on RecycleView

            showAudioItems(audioItemList);
        }

    }

    private void showAudioItems(List<audioItem> audioItems) {

        audioItemAdapter = new AudioItemAdapter(audioItems, player);
        recyclerView = requireView().findViewById(R.id.audioRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(audioItemAdapter);
    }











}