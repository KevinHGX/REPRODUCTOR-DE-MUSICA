package com.example.navegacion;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.navegacion.Controler.AlbumAdapter;
import com.example.navegacion.Controler.Artist;
import com.example.navegacion.Controler.ArtistAdapter;
import com.example.navegacion.Controler.GridSpacingItemDecoration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends Fragment {

    ArrayList<Music> auxMusicList = new ArrayList<>();
    Map<String,ArrayList<Music>> auxMusicArtist = new HashMap<>();

    private RecyclerView recyclerA,recyclerT;
    private TextView tracks,artits;

    int spacingA=10; // px
    int spacingT=20; // px
    boolean includeEdgeA = false;

    private ArtistAdapter artistsAdapter;
    private AlbumAdapter albumAdapter;

    private int numTracks,numArtist;

    public Home(ArrayList<Music> auxMusicList, Map<String, ArrayList<Music>> auxMusicArtist) {
        this.auxMusicList = auxMusicList;
        this.auxMusicArtist = auxMusicArtist;
    }

    public void setIntegerData(int _a,int _b){
        this.numTracks = _a;
        this.numArtist = _b;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.tracks = (TextView) view.findViewById(R.id.numtrack);
        this.artits = (TextView) view.findViewById(R.id.numArtists);

        tracks.setText(""+numTracks);
        artits.setText(""+numArtist);

        this.recyclerA = (RecyclerView)view.findViewById(R.id.recyclerA);
        this.recyclerA.setLayoutManager(new GridLayoutManager(getActivity(),3));
        new LoadDataA().execute("");

        this.recyclerT = (RecyclerView)view.findViewById(R.id.recyclerT);
        this.recyclerT.setLayoutManager(new GridLayoutManager(getActivity(),3));
        new LoadDataT().execute("");

        return view;
    }

    public class LoadDataA extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            if (getActivity() != null) {
                artistsAdapter = new ArtistAdapter(getActivity(), auxMusicArtist);
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            recyclerA.setAdapter(artistsAdapter);
            if (getActivity() != null) {
                recyclerA.addItemDecoration(new GridSpacingItemDecoration(3, spacingA, includeEdgeA));
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public class LoadDataT extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            if(getActivity() != null){
                albumAdapter = new AlbumAdapter(getActivity(),auxMusicList,true);
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String s){
            recyclerT.setAdapter(albumAdapter);
            if(getActivity() != null){
                recyclerT.addItemDecoration(new GridSpacingItemDecoration(3,spacingT,includeEdgeA));
            }
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

    }


}