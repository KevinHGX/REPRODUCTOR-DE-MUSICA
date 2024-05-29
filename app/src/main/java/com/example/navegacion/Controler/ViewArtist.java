package com.example.navegacion.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.navegacion.Music;
import com.example.navegacion.MusicAdapter;
import com.example.navegacion.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ViewArtist extends AppCompatActivity {

    private ImageView imageView;
    private ListView listView;
    private TextView artist;

    private ArrayList<Music> musicList;
    Gson gson = new Gson();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_artist);

        imageView = (ImageView) findViewById(R.id.artistImage);
        artist = (TextView) findViewById(R.id.tartist);
        listView = (ListView) findViewById(R.id.listArtist);

        Intent intent = getIntent();
        String fuckingArray = intent.getStringExtra("songsList");
        musicList = gson.fromJson(fuckingArray, new TypeToken<ArrayList<Music>>(){}.getType());

        Music music = musicList.get(0);

        artist.setText(music.getArtist());
        imageView.setImageBitmap(music.getImage());

        MusicAdapter adapter = new MusicAdapter(this,musicList);
        listView.setAdapter(adapter);


    }
}