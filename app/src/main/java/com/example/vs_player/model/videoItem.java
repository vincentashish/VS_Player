package com.example.vs_player.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class videoItem implements Parcelable {

    long id;
    Uri uri;
    String name;
    int duration;
    int size;
    String mimeType;

    public videoItem(long id, Uri uri, String name, int duration, int size, String mimeType) {
        this.id = id;
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.mimeType = mimeType;
    }

    @NonNull
    @Override
    public String toString() {
        return "id: "+String.valueOf(id)+"\nUri: "+String.valueOf(uri)+"\nname:"+name+"\nduration:"+String.valueOf(duration)+"\nsize: "+String.valueOf(size)+"\nMimeType: "+mimeType;
    }

    protected videoItem(Parcel in) {
        id = in.readLong();
        uri = in.readParcelable(Uri.class.getClassLoader());
        name = in.readString();
        duration = in.readInt();
        size = in.readInt();
        mimeType = in.readString();
    }

    public static final Creator<videoItem> CREATOR = new Creator<videoItem>() {
        @Override
        public videoItem createFromParcel(Parcel in) {
            return new videoItem(in);
        }

        @Override
        public videoItem[] newArray(int size) {
            return new videoItem[size];
        }
    };

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

    public String getMimeType() {
        return mimeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeParcelable(uri,i);
        parcel.writeString(name);
        parcel.writeInt(duration);
        parcel.writeInt(size);
        parcel.writeString(mimeType);
    }
}
