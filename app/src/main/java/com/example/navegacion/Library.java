package com.example.navegacion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navegacion.Controler.PageControler;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Library extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    PageControler pageAdapter;

    ArrayList<Music> musicList;
    Storage permission;

    public Library(ArrayList<Music> musicList) {
        this.musicList = musicList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_library,container,false);
        tabLayout = (TabLayout)view.findViewById(R.id.TabTrack);
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);

        pageAdapter = new PageControler(getChildFragmentManager(),tabLayout.getTabCount(),this.musicList);
        viewPager.setAdapter(pageAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    pageAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 1){
                    pageAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 2){
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;
        //return inflater.inflate(R.layout.fragment_second, container, false);
    }
}