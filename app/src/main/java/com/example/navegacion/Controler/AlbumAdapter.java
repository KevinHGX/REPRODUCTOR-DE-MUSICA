package com.example.navegacion.Controler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navegacion.Music;
import com.example.navegacion.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AH> {

    private Context context;
    private ArrayList<Music> musicList;

    private boolean flag=false;
    public AlbumAdapter(Context context, ArrayList<Music> musicList,boolean _f) {
        this.context = context;
        this.musicList = musicList;
        this.flag = _f;
    }

    @NonNull
    @Override
    public AH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AH(LayoutInflater.from(parent.getContext()).inflate(R.layout.album_grid,parent,false),flag);
    }

    @Override
    public void onBindViewHolder(@NonNull AH holder, int position) {
        if(musicList != null){
            Music music = musicList.get(position);

            String title = music.getTitle();
            String artist = music.getArtist();
            title = chechString(title,9);

            holder.titleSong.setText(title);
            artist = chechString(artist,10);

            holder.artistSong.setText(artist);
            holder.image.setImageBitmap(music.getImage());
        }
    }

    public String chechString(String _target,int _limit){
        String substring;

        if(_target.length() > _limit){
            substring = _target.substring(0,6);
            substring += "...";
        }else{
            substring = _target;
        }

        return substring;
    }

    @Override
    public int getItemCount() {
        return musicList != null ? musicList.size() : 0;
    }

    public class AH extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView image;
        private TextView titleSong,artistSong;
        public AH(@NonNull View itemView,boolean status) {
            super(itemView);

            image = (ImageView)itemView.findViewById(R.id.imageView);
            titleSong = (TextView)itemView.findViewById(R.id.titlesong);
            artistSong = (TextView)itemView.findViewById(R.id.artistsong);

            if(status){

                titleSong.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                artistSong.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                image.setLayoutParams(new LinearLayout.LayoutParams(0,175));
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(context, musicList.get(getAdapterPosition()).toString(), Toast.LENGTH_SHORT).show();
            Gson gson = new Gson();
            String musicListGson = gson.toJson(musicList);
            context.startActivity(new Intent(context, PlayerActivity.class)
                    .putExtra("songsList", musicListGson)
                    .putExtra("position", getAdapterPosition()));
        }
    }

}
