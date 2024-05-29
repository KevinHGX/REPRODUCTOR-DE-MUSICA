package com.example.navegacion.Controler;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.example.navegacion.InicioStatic;
import com.example.navegacion.Music;
import com.example.navegacion.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {
    ImageView prev,play,next,imageTrack,repeat,shuffle;
    int position,sizeMusic;
    SeekBar mSeekBarTime;
    TextView songName,begin,end,artistName,content;
    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean stopThread,flag_shuffle = false,flag_repeat = false;
    Gson gson = new Gson();
    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    Thread updateSeek;
    ArrayList<Music> musicList;
    LinearLayout backplayer;

    Context context = this;

    public void onDestroy() {
        super.onDestroy();
        stopThread = true;
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // casting views
        prev = findViewById(R.id.prev);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        begin = findViewById(R.id.Bstart);
        end = findViewById(R.id.Eend);
        mSeekBarTime = findViewById(R.id.barSong);
        songName = findViewById(R.id.textsong);
        artistName = findViewById(R.id.textArtist);
        imageTrack = findViewById(R.id.imagen);
        repeat = findViewById(R.id.repeat);
        shuffle = findViewById(R.id.shuffle);
        content = findViewById(R.id.textcontent);
        backplayer = findViewById(R.id.backplayer);

        changeColor();//seekbar

        Intent intent = getIntent();

        String fuckingArray = intent.getStringExtra("songsList");
        musicList = gson.fromJson(fuckingArray, new TypeToken<ArrayList<Music>>(){}.getType());
        this.sizeMusic = musicList.size()-1;

        int intExtra = intent.getIntExtra("position", 0);
        position = intExtra;

        if(musicList == null){
            Log.i("Status>>","NULL");
        }else {

            Music target = musicList.get(position);

            update(target);

            Uri songUri = ContentUris.withAppendedId(uri,Long.parseLong(target.getId()));
            MediaPlayer create = MediaPlayer.create(this, songUri);
            mediaPlayer = create;
            create.start();
            artistName.setText(target.getArtist());
            songName.setText(target.getTitle());
            songName.setSelected(true);
            imageTrack.setImageBitmap(target.getImage());
            changeColorBackgroundLayoutPlayer(target.getImage());
            mSeekBarTime.setMax(this.mediaPlayer.getDuration());
            stopThread = false;
            content.setText((position+1)+"/"+(sizeMusic+1));
            play.setImageResource(R.drawable.baseline_pause_circle_24);
        }

        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Log.d("duration", mediaPlayer.getCurrentPosition() + " and " + mediaPlayer.getDuration());
                if (mediaPlayer.getCurrentPosition() >= mediaPlayer.getDuration() - 200) {
                    if(flag_repeat){
                        repeatSong();
                    }else {
                        next.callOnClick();
                    }
                }
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeek = new Thread() {
            public void run() {
                while (!stopThread) {
                    try {
                        if (mediaPlayer != null) {
                            long currentPosition = mediaPlayer.getCurrentPosition();
                            final String format = String.format("%02d:%02d", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(currentPosition)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(currentPosition) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition))));
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    begin.setText(format);
                                }
                            });
                            mSeekBarTime.setProgress(mediaPlayer.getCurrentPosition());
                            sleep(200);
                            Log.d("threadCode", "Updating Success");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("threadCode", "Updating Failed");
                    }
                }
            }
        };
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    play.setImageResource(R.drawable.baseline_play_circle_filled_24);
                    mediaPlayer.pause();
                }
                else {
                    play.setImageResource(R.drawable.baseline_pause_circle_24);
                    mediaPlayer.start();
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(flag_shuffle){
                    position = setPosition(position);
                }else {
                    if (position != 0) {
                        position--;
                    } else {
                        position = musicList.size() - 1;
                    }
                }
                Music item_prev = musicList.get(position);

                update(item_prev);

                imageTrack.setImageBitmap(item_prev.getImage());
                changeColorBackgroundLayoutPlayer(item_prev.getImage());
                Uri parse = ContentUris.withAppendedId(uri,Long.parseLong(item_prev.getId()));
                mediaPlayer = MediaPlayer.create(getApplicationContext(), parse);
                mediaPlayer.start();
                play.setImageResource(R.drawable.baseline_pause_circle_24);
                mSeekBarTime.setMax(mediaPlayer.getDuration());
                artistName.setText(item_prev.getArtist());
                songName.setText(musicList.get(position).getTitle());
                long duration = mediaPlayer.getDuration();
                content.setText((position+1)+"/"+(sizeMusic+1));
                end.setText(String.format("%02d:%02d", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(duration)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(flag_shuffle){
                    position=setPosition(position);
                }else{
                    if (position != musicList.size() - 1) {
                        position++;
                    } else {
                        position = 0;
                    }
                }
                Music item_next = musicList.get(position);

                update(item_next);

                imageTrack.setImageBitmap(item_next.getImage());
                changeColorBackgroundLayoutPlayer(item_next.getImage());
                Uri parse = ContentUris.withAppendedId(uri,Long.parseLong(item_next.getId()));
                mediaPlayer = MediaPlayer.create(getApplicationContext(), parse);
                mediaPlayer.start();
                play.setImageResource(R.drawable.baseline_pause_circle_24);
                mSeekBarTime.setMax(mediaPlayer.getDuration());
                artistName.setText(item_next.getArtist());
                songName.setText(musicList.get(position).getTitle());
                long duration = mediaPlayer.getDuration();
                content.setText((position+1)+"/"+(sizeMusic+1));
                end.setText(String.format("%02d:%02d", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(duration)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))));
            }
        });
        long duration = mediaPlayer.getDuration();
        end.setText(String.format("%02d:%02d", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(duration)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))));
        updateSeek.start();

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag_repeat){
                    repeat.setImageResource(R.drawable.baseline_repeat_24);
                    repeat.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.secondary), PorterDuff.Mode.SRC_IN);
                    flag_repeat=!flag_repeat;
                }else{
                    repeat.setImageResource(R.drawable.baseline_repeat_one_24);
                    repeat.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white), PorterDuff.Mode.SRC_IN);
                    flag_repeat=!flag_repeat;
                }
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag_shuffle){
                    shuffle.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.secondary), PorterDuff.Mode.SRC_IN);
                    flag_shuffle=!flag_shuffle;
                }else{
                    shuffle.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white), PorterDuff.Mode.SRC_IN);
                    flag_shuffle=!flag_shuffle;
                }
            }
        });
    }

    private int setPosition(int currentPosition){
        Random random = new Random();
        int res = random.nextInt(sizeMusic)+1;
        while(res == currentPosition){
            res = random.nextInt(sizeMusic)+1;
        }

        return res;
    }

    private void repeatSong(){
        mediaPlayer.stop();
        mediaPlayer.release();
        Music item_next = musicList.get(position);
        Uri parse = ContentUris.withAppendedId(uri,Long.parseLong(item_next.getId()));
        mediaPlayer = MediaPlayer.create(getApplicationContext(), parse);
        mediaPlayer.start();
        content.setText((position+1)+"/"+(sizeMusic+1));
    }

    private void changeColor(){
        mSeekBarTime.setProgressTintList(ColorStateList.valueOf(Color.WHITE));
    }
    public void changeColorBackgroundLayoutPlayer(Bitmap _picture){
        Palette.from(_picture).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {

                Drawable progressDrawable = mSeekBarTime.getProgressDrawable();
                //ColorFilter colorFilter = progressDrawable.getColorFilter();
                PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;

                int color = palette.getDominantColor(ContextCompat.getColor(context,R.color.black));
                backplayer.setBackgroundColor(color);
                int color1 = palette.getVibrantColor(ContextCompat.getColor(context,R.color.black));
                play.setColorFilter(color1);
                prev.setColorFilter(color1);
                next.setColorFilter(color1);

                ColorFilter newColorFilter = new PorterDuffColorFilter(color1,mode);

                progressDrawable.setColorFilter(newColorFilter);

                Drawable thumb = mSeekBarTime.getThumb();
                if(thumb != null){
                    thumb.setColorFilter(newColorFilter);
                }

                mSeekBarTime.setProgressDrawable(progressDrawable);
            }
        });

    }

    public void update(Music target){
        //---------------------------------------------
        int auxA = InicioStatic.musicArtist.get(target.getArtist());
        int auxT = InicioStatic.musicTrack.get(target.getId());
        auxA+=1;
        auxT+=1;
        InicioStatic.musicArtist.put(target.getArtist(),auxA);
        InicioStatic.musicTrack.put(target.getId(),auxT);
        //---------------------------------------------
    }
}