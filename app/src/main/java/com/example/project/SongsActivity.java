package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class SongsActivity extends AppCompatActivity implements Serializable{
    ListView songsListView;
    String[] songs;
    ArrayList<String> options = new ArrayList<String>();
    String chosenSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        Intent in = getIntent();
        Serializable data= in.getSerializableExtra("com.example.INDEX3");
        options = (ArrayList<String>) data;
        //System.out.println("songs activity --> options list " + options);
        songs = new String[options.size()];
        for (int i = 0; i < options.size(); i++) {
            songs[i] = options.get(i);
        }

        songsListView = (ListView) findViewById(R.id.songsListView);
        songsListView.setAdapter(new ArrayAdapter<String>(this, R.layout.my_songs_detail, songs));

    }

    public void onStart() {
        super.onStart();
        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenSong = songs[position];

                Intent player = new Intent(SongsActivity.this, StartPlayerActivity.class);
                player.putExtra("com.example.INDEX4", chosenSong);
                startActivity(player);
            }
        });

    }
}