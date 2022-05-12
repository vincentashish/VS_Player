package com.example.vs_player.model;

import android.net.Uri;

public class audioItem {
    long id;
    Uri uri;
    String name,artist;
    int duration;
    int size;
    long albumId;
    Uri albumArtUri;
    String mimeType;


    public audioItem(long id, Uri uri, String name,String artist, int duration, int size, long albumId, Uri albumArtUri, String mimeType) {
        this.id = id;
        this.uri = uri;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.size = size;
        this.albumId = albumId;
        this.albumArtUri = albumArtUri;
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getArtist() {
        return artist;
    }

    public long getId() {
        return id;
    }

    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getSize() {
        return size;
    }

    public long getAlbumId() {
        return albumId;
    }

    public Uri getAlbumArtUri() {
        return albumArtUri;
    }
}
