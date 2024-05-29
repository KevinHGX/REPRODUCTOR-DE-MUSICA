package com.example.navegacion;

import android.Manifest;
import android.app.Activity;
import android.os.Environment;
import android.widget.ArrayAdapter;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class Storage extends Activity {

    public ArrayAdapter<String> musicArrayAdapter; // Adapter for music list
    public String songs[]; // to storage song names;
    public ArrayList<File> musics;

    public ArrayAdapter<String> getMusicArrayAdapter() {
        return musicArrayAdapter;
    }

    public String getSongs(int index) {
        return songs[index];
    }

    public ArrayList<File> getMusics() {
        return musics;
    }

}
