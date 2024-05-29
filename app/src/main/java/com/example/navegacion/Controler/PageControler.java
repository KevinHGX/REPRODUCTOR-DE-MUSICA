package com.example.navegacion.Controler;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.navegacion.InicioStatic;
import com.example.navegacion.Music;
import com.example.navegacion.Storage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PageControler extends FragmentPagerAdapter {

    int count;
    ArrayList<Music> musicList;
    Map<String,ArrayList<Music>> artist_map = new HashMap<>();
    Set<String> set = new HashSet<>();
    public PageControler(@NonNull FragmentManager fm, int behavior,ArrayList<Music> _musicL) {
        super(fm, behavior);
        this.count = behavior;
        this.musicList = _musicL;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new Tracks(musicList);
            case 1:
                artist_map = InicioStatic.artist;
                Log.i("Artists=::>>",artist_map.toString());
                return new Artist(artist_map);
            case 2:
                return new Album(musicList);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }

}
