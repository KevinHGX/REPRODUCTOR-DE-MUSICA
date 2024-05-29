package com.example.navegacion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.navegacion.Controler.PlayerActivity;
import com.google.gson.Gson;

import java.util.ArrayList;


public class MusicAdapter extends ArrayAdapter<Music> {
    private Context mContext;
    private ArrayList<Music> mMusicList;
    private int selectPosition = -1;
    private Handler handler = new Handler();
    private LinearLayout itemMusic;
    private ImageView option;
    private TextView textViewArtist,textViewTitle;

    public MusicAdapter(Context context, ArrayList<Music> musicList) {
        super(context, 0, musicList);
        mContext = context;
        mMusicList = musicList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.music_item, parent, false);
        }

        Music currentMusic = mMusicList.get(position);

        ImageView imageView = listItem.findViewById(R.id.imageViewMusic);
        imageView.setImageBitmap(currentMusic.getImage());

        textViewArtist = listItem.findViewById(R.id.textViewArtist);
        textViewTitle = listItem.findViewById(R.id.textViewTitle);
        itemMusic = listItem.findViewById(R.id.itemMusic);
        option = listItem.findViewById(R.id.options);

        if(position == selectPosition){
            textViewTitle.setTextColor(mContext.getResources().getColor(R.color.white));
            textViewArtist.setTextColor(mContext.getResources().getColor(R.color.white));
            itemMusic.setBackgroundColor(mContext.getResources().getColor(R.color.secondary));
            option.setColorFilter(ContextCompat.getColor(mContext, R.color.white), PorterDuff.Mode.SRC_ATOP);
        }else{
            textViewTitle.setTextColor(mContext.getResources().getColor(R.color.secondary_no_select));
            textViewArtist.setTextColor(mContext.getResources().getColor(R.color.secondary_no_select));
            itemMusic.setBackgroundColor(mContext.getResources().getColor(R.color.black));
            option.setColorFilter(ContextCompat.getColor(mContext, R.color.secondary_no_select), PorterDuff.Mode.SRC_ATOP);
        }

        textViewArtist.setText(currentMusic.getArtist());
        textViewTitle.setSelected(true);
        textViewTitle.setText(currentMusic.getTitle());

        listItem.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                selectPosition = position;
                notifyDataSetChanged();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        String musicListGson = gson.toJson(mMusicList);
                        mContext.startActivity(new Intent(mContext, PlayerActivity.class)
                                .putExtra("songsList", musicListGson)
                                .putExtra("position", position));
                    }
                },100);
            }
        });

        return listItem;
    }

}

