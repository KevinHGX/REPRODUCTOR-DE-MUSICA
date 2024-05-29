package com.example.navegacion.Controler;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navegacion.Music;
import com.example.navegacion.R;

import java.util.ArrayList;

public class Album extends Fragment {
    private ArrayList<Music> musicList;
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;

    int spanCount=2; //columns
    int spacing=20; // px
    boolean includeEdge = false;

    public Album(ArrayList<Music> musicList) {
        this.musicList = musicList;
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
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        this.recyclerView = (RecyclerView)view.findViewById(R.id.recyclerV);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),spanCount));

        new LoadData().execute("");

        return view;
    }

    public class LoadData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            if(getActivity() != null){
                albumAdapter = new AlbumAdapter(getActivity(),musicList,false);
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String s){
            recyclerView.setAdapter(albumAdapter);
            if(getActivity() != null){
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));
            }
        }


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

    }
}