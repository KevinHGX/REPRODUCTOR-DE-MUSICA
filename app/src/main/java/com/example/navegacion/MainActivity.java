package com.example.navegacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileDescriptor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 100;
    ArrayList<Music> musicList = new ArrayList<>();
    //Map<String,ArrayList<Music>> artist_map = new HashMap<>();

    //---------------------------------------------------------------
    ArrayList<Music> auxMusicList = new ArrayList<>();
    Map<String,ArrayList<Music>> auxMusicArtist = new HashMap<>();
    Map<String,ArrayList<Music>> artist_map = new HashMap<>();
    //---------------------------------------------------------------

    Home home;
    Library library;
    Ecuadorian ecuadorian;

    Search search;
    private boolean mostArtists=true, mostTracks=false;

    //private Storage permission = new Storage();

    //----------------------------



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.bottom_navegation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectListener);

        initReadExternalStorage();
        //initPermission();

        loadPreferences();
        home = new Home(auxMusicList,auxMusicArtist);
        home.setIntegerData(musicList.size(),artist_map.size());
        loadFragment(home);
    }

    public void loadPreferences(){
        Gson gson = new Gson();
        Type typeMap = new HashMap<String,Double>().getClass();
        //Type typeHash = new HashMap<String,ArrayList<Music>>().getClass();
        SharedPreferences preferences = getSharedPreferences("tendencias",Context.MODE_PRIVATE);
        String dataA = preferences.getString("artist","empty");
        String dataT = preferences.getString("tracks","empty");
        //String dataH = preferences.getString("Array","empty");

        if(dataA.compareTo("empty") != 0 && dataT.compareTo("empty") != 0){
            Map<String,Double> auxA = gson.fromJson(dataA,typeMap);
            Map<String,Double> auxT = gson.fromJson(dataT,typeMap);
            //InicioStatic.artist = gson.fromJson(dataH,typeHash);

            InicioStatic.musicTrack = castMap(auxA);
            InicioStatic.musicTrack = castMap(auxT);

            Log.i("Artists >> ",InicioStatic.musicArtist.toString());
            Log.i("Tracks >> ",InicioStatic.musicTrack.toString());

            mostListenMusic(mostArtists,3,InicioStatic.musicArtist);
            mostListenMusic(mostTracks,3,InicioStatic.musicTrack);
        }
    }

    public Map<String,Integer> castMap( Map<String,Double> _item){
        Map<String,Integer> aux = new HashMap<>();

        for (Map.Entry<String,Double> entra: _item.entrySet()){
            aux.put(entra.getKey(),Double.valueOf(entra.getValue()).intValue());
        }

        return aux;
    }

    public void savePreferences(){
        SharedPreferences preferences = getSharedPreferences("tendencias", Context.MODE_PRIVATE);
        String dataA = arrayListToString(InicioStatic.musicArtist);
        String dataT = arrayListToString(InicioStatic.musicTrack);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("artist",dataA);
        editor.putString("tracks",dataT);

        editor.commit();
    }

    private String arrayListToString(Map<String,Integer> _target){
        Gson gson = new Gson();
        return gson.toJson(_target);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            mostListenMusic(mostArtists,3,InicioStatic.musicArtist);
            mostListenMusic(mostTracks,3,InicioStatic.musicTrack);
            savePreferences();

            switch(item.getItemId()){
                case R.id.firstF:
                    home = new Home(auxMusicList,auxMusicArtist);
                    home.setIntegerData(musicList.size(),artist_map.size());
                    loadFragment(home);
                    return true;
                case R.id.secondF:
                    library = new Library(musicList);
                    loadFragment(library);
                    return true;
                case R.id.thirdF:
                    ecuadorian = new Ecuadorian();
                    loadFragment(ecuadorian);
                    return true;
                case R.id.search:
                    search = new Search(musicList);
                    loadFragment(search);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        //Bundle bundle = new Bundle();
        //bundle.putSerializable("musicList",musicList);
        transition.replace(R.id.frame_container,fragment);
        transition.commit();
    }

    public void initReadExternalStorage(){
        // Verificar permisos de almacenamiento
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permisos
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
        }

        // Obtener la información de la música del almacenamiento del dispositivo
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID
        };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, MediaStore.Audio.Media.TITLE + " ASC");
        //MediaMetadataRetriever retriever = null;

        // Verificar si el cursor está vacío
        if (cursor == null) {
            Log.e("Music", "Cursor is null.");
        } else if (!cursor.moveToFirst()) {
            Log.e("Music", "No music found on device.");
        } else {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                @SuppressLint("Range") String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                @SuppressLint("Range") String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                //Uri songUri = ContentUris.withAppendedId(uri,Long.parseLong(id));

                Bitmap image = getAlbumArt(Long.parseLong(albumId));

                if(image == null){
                    image = BitmapFactory.decodeResource(getResources(),R.drawable.player);
                }

                Music music = new Music();
                music.setMusic(id, artist, title, data, duration, image);
                musicList.add(music);

                //condicion de guardado si no existe
            }
        }

        // Cerrar el cursor
        cursor.close();

        initStaticClass();
        createMapMusicListArtist();
    }

    private void createMapMusicListArtist() {
        for(Music current:musicList){
            ArrayList<Music> aux = new ArrayList<>();
                aux.add(current);
                for (Music sub_current : musicList) {
                    if (sub_current.getArtist().compareTo(current.getArtist()) == 0 && sub_current.getId().compareTo(current.getId()) != 0) {
                        aux.add(sub_current);
                    }
                }
                artist_map.put(current.getArtist(), aux);
        }
        InicioStatic.artist = artist_map;
        //Log.i("->>>>>",artist_map.toString());
        //Log.i("<<<<<-",InicioStatic.artist.toString());
        //savePreferencesA();
    }

    public void savePreferencesA(){
        SharedPreferences preferences = getSharedPreferences("tendencias", Context.MODE_PRIVATE);
        if(preferences.getString("Array","empty").compareTo("empty") == 0){
            String dataAM = arrayListToStringA(InicioStatic.artist);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Array",dataAM);
            editor.commit();
        }
    }

    private String arrayListToStringA(Map<String,ArrayList<Music>> _target){
        Gson gson = new Gson();
        return gson.toJson(_target);
    }


    public void initStaticClass(){
        Set<String> set = new HashSet<>();

        for (Music current:musicList) {
            set.add(current.getArtist());
            //InicioStatic.musicArtist.put(current.getArtist(), 0);
            InicioStatic.musicTrack.put(current.getId(), 0);
        }

        for(String target:set){
            InicioStatic.musicArtist.put(target,0);
        }
    }

    private Bitmap getAlbumArt(long albumId) {
        Bitmap albumArt = null;
        try {
            final Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);

            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "r");
            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                albumArt = BitmapFactory.decodeFileDescriptor(fd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albumArt;
    }

    public void mostListenMusic(boolean _target,int _size,Map<String,Integer> List) {
        //condicion de chequeo preferences
        // Obtener las entradas del mapa como una lista
        //Log.i("Begin",InicioStatic.musicArtist.toString());
        //Log.i("Begin",InicioStatic.musicTrack.toString());
        Log.i("CHANGE",List.toString());
        List<Map.Entry<String, Integer>> lista = new ArrayList<>(List.entrySet());

        // Ordenar la lista utilizando el valor de cada entrada
        Collections.sort(lista, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        // Crear un nuevo mapa ordenado a partir de la lista ordenada
        Map<String, Integer> mapaOrdenado = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entrada : lista) {
            mapaOrdenado.put(entrada.getKey(), entrada.getValue());
        }

        // Obtener las entradas del mapa como una lista
        List<Map.Entry<String, Integer>> newList = new ArrayList<>(mapaOrdenado.entrySet());

        // Obtener los últimos 3 elementos de la lista
        List<Map.Entry<String, Integer>> ultimos3 = newList.subList(Math.max(newList.size() - _size, 0), newList.size());

        if(_target){//Artist
            if(auxMusicArtist != null){
                auxMusicArtist.clear();
            }
            for (Map.Entry<String, Integer> entrada : ultimos3) {
                auxMusicArtist.put(entrada.getKey(),InicioStatic.artist.get(entrada.getKey()));
            }
        }else{//Tracks
            if(auxMusicList != null){
                auxMusicList.clear();
            }
            for (Map.Entry<String, Integer> entrada : ultimos3) {
                auxMusicList.add(getMusicById(entrada.getKey()));
            }
            Log.i("auxMusicList",auxMusicList.toString());
        }
    }

    private Music getMusicById(String _index){
        Music aux = null;
        for (Music current: musicList) {
            //Log.i("GDB: ",current.getArtist()+"<===>"+_index);
            String auxString = current.getId();
            if(auxString.compareTo(_index) == 0){
                //Log.i("GDB: ",current.getArtist()+"<***>"+_index);
                aux = current;
                break;
            }
        }
        return aux;
    }

}