package com.example.navegacion.Controler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navegacion.InicioStatic;
import com.example.navegacion.Music;
import com.example.navegacion.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Artist extends Fragment {

    Map<String,ArrayList<Music>> artist_map = new HashMap<>();
    private RecyclerView recyclerView;
    private ArtistAdapter albumAdapter;

    int spanCountA=3; //columns
    int spacingA=10; // px
    boolean includeEdgeA = false;

    public Artist(Map<String, ArrayList<Music>> artist_map) {
        this.artist_map = artist_map;
        //Log.i("Inside Artist:: ", artist_map.toString());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        this.recyclerView = (RecyclerView)view.findViewById(R.id.recyclerV);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),spanCountA));

        new LoadData().execute("");

        return view;
    }

    public class LoadData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            if (getActivity() != null) {
                albumAdapter = new ArtistAdapter(getActivity(), artist_map);
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            recyclerView.setAdapter(albumAdapter);
            if (getActivity() != null) {
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCountA, spacingA, includeEdgeA));
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}