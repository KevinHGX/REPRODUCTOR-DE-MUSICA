package com.example.navegacion;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Music implements Serializable {
    private String id;
    private String artist;
    private String title;
    private String data;

    private String duration;
    private Bitmap image;
    //private String songP;

    public Music() {}

    public void setMusic(String id, String artist, String title, String data, String duration, Bitmap image) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.data = data;
        this.duration = duration;
        this.image = image;
    }

    protected Music(Parcel in) {
        id = in.readString();
        artist = in.readString();
        title = in.readString();
        data = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public String getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getData() {
        return data;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Artist: " + artist + ", Title: " + title + ", Data: " + data;
    }

}
