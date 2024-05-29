package com.example.navegacion;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

public class Search extends Fragment{

    private ArrayList<Music> musicList;
    private ArrayList<Music> trackList = new ArrayList<>();
    private ArrayList<Music> artistList = new ArrayList<>();
    private ArrayList<Music> albumList = new ArrayList<>();

    private LinearLayout viewTra,viewArt,viewAlb,Message;

    private ListView list_Tracks;
    private ListView list_Artist;
    private ListView list_Album;

    private EditText input;

    private MusicAdapter adapterT,adapterA,adapterAl;



    public Search(ArrayList<Music> musicList) {
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

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        this.viewTra = (LinearLayout) view.findViewById(R.id.viewTracks);
        this.viewArt = (LinearLayout) view.findViewById(R.id.viewArtist);
        this.viewAlb = (LinearLayout) view.findViewById(R.id.viewAlbum);
        this.Message = (LinearLayout) view.findViewById(R.id.message);

        input = (EditText) view.findViewById(R.id.inputsearch);
        list_Tracks = (ListView) view.findViewById(R.id.listTrack);
        list_Artist = (ListView) view.findViewById(R.id.listArtist);
        list_Album = (ListView) view.findViewById(R.id.listAlbum);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Este método se llama antes de que el texto sea cambiado
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               Check(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Este método se llama después de que el texto ha sido cambiado
            }
        });

        return view;
    }

    private void Check(String _target){

        if(_target.isEmpty()){
            this.viewTra.setVisibility(View.GONE);
            this.viewArt.setVisibility(View.GONE);
            this.viewAlb.setVisibility(View.GONE);
            return;
        }

        if(trackList.size() != 0){
            this.trackList.clear();
        }
        if(artistList.size() != 0){
            this.artistList.clear();
        }
        if(albumList.size() != 0){
            this.albumList.clear();
        }

        for (Music item:musicList) {
            if(item.getTitle().toLowerCase().contains(_target.toLowerCase())){//track - Album
                trackList.add(item);
                albumList.add(item);
            }
            if(item.getArtist().toLowerCase().contains(_target.toLowerCase())){//artista
                artistList.add(item);
            }
        }

        if(trackList.size() !=0 ) {
            this.adapterT = new MusicAdapter(getContext(), this.trackList);
            list_Tracks.setAdapter(adapterT);
            this.viewTra.setVisibility(View.VISIBLE);
        }else{
            this.viewTra.setVisibility(View.GONE);
        }

        if(artistList.size() != 0) {
            this.adapterA = new MusicAdapter(getContext(), this.artistList);
            list_Artist.setAdapter(adapterA);
            this.viewArt.setVisibility(View.VISIBLE);
        }else{
            this.viewArt.setVisibility(View.GONE);
        }

        if(albumList.size() != 0) {
            this.adapterAl = new MusicAdapter(getContext(), this.albumList);
            list_Album.setAdapter(adapterAl);
            this.viewAlb.setVisibility(View.VISIBLE);
        }else{
            this.viewAlb.setVisibility(View.GONE);
        }

        if(trackList.size() != 0 || artistList.size() != 0 || albumList.size() != 0){
            this.Message.setVisibility(View.GONE);
        }else{
            this.Message.setVisibility(View.VISIBLE);
        }

    }

}