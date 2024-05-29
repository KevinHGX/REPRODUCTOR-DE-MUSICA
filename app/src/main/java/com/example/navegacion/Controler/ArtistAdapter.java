package com.example.navegacion.Controler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navegacion.Music;
import com.example.navegacion.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.AH> {

    private Context context;
    private int cont = 3;
    private Map<String,ArrayList<Music>> artist_map = new HashMap<>();;//CAMBIAR A MAP

    public ArtistAdapter(Context context, Map<String,ArrayList<Music>> musicList) {
        this.context = context;
        this.artist_map = musicList;
        //Log.i("ArtistAdapter:: ",artist_map.toString());
    }

    @NonNull
    @Override
    public AH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArtistAdapter.AH(LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_grid,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.AH holder, int position) {
            String artist = (String) artist_map.keySet().toArray()[position];
            ArrayList<Music> music = artist_map.get(artist);
            Music sub_music = music.get(0);

            if (artist_map != null) {
                artist = chechString(artist);
                holder.titleSong.setText(artist);
                holder.artistSong.setText(+music.size() + " Tracks");
                holder.image.setImageBitmap(sub_music.getImage());
            }
    }

    public String chechString(String _target){
        String substring;

        if(_target.length() > 9){
            substring = _target.substring(0,6);
            substring += "...";
        }else{
            substring = _target;
        }

        return substring;
    }

    @Override
    public int getItemCount() {
        return artist_map != null ? artist_map.size() : 0;
    }

    public class AH extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView image;
        private TextView titleSong,artistSong;
        public AH(@NonNull View itemView) {
            super(itemView);

            image = (ImageView)itemView.findViewById(R.id.imageView);
            titleSong = (TextView)itemView.findViewById(R.id.titlesong);
            artistSong = (TextView)itemView.findViewById(R.id.artistsong);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(context,artist_map.keySet().toArray()[getAdapterPosition()].toString(), Toast.LENGTH_SHORT).show();
            Gson gson = new Gson();
            String artist = (String) artist_map.keySet().toArray()[getAdapterPosition()];
            ArrayList<Music> music = artist_map.get(artist);

            String musicListGson = gson.toJson(music);
            context.startActivity(new Intent(context, ViewArtist.class)
                    .putExtra("songsList", musicListGson)
                    .putExtra("position", getAdapterPosition()));
        }
    }

}
