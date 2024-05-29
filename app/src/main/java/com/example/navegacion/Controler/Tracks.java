package com.example.navegacion.Controler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.navegacion.Music;
import com.example.navegacion.MusicAdapter;
import com.example.navegacion.R;
import com.example.navegacion.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.widget.Toast;

public class Tracks extends Fragment {

    private ArrayList<Music> musicList;
    private ListView listView;

    private Storage permission;

    /*public Tracks(Storage permission) {
        this.permission = permission;
    }*/

    public Tracks(ArrayList<Music> musicList) {
        this.musicList = musicList;
    }

    /* public Tracks(ArrayList<Music> musicList) {
        this.musicList = musicList;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);
        MusicAdapter adapter = new MusicAdapter(getContext(),musicList);

        listView = view.findViewById(R.id.listView);

        listView.setSelector(android.R.color.darker_gray);
        listView.setAdapter(adapter);

        return view;
    }
}