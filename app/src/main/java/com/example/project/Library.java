package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class Library extends AppCompatActivity implements Serializable{

    String[] songs;
    ListView songsListView;
    String chosenSong;
    TextView msg;
    ArrayList<String> song = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        msg = (TextView) findViewById(R.id.message);
        //Populate songs array-----------------------
        Intent in = getIntent();
        Serializable data = in.getSerializableExtra("DOWNSONGS");
        song = (ArrayList<String>) data;

        if(! (song.isEmpty()) ){

            songs = new String[song.size()];
            for(int i = 0; i < song.size(); i++){
                songs[i] = song.get(i);
            }


            songsListView = (ListView) findViewById(R.id.songsListView);

            songsListView.setAdapter(new ArrayAdapter<String>(this, R.layout.library_details, songs));
        }

    }

    public void onStart() {
        super.onStart();
        if (songsListView == null) {
            msg.setVisibility(View.VISIBLE);
        }else {
            songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    chosenSong = songs[position];
                    Intent player = new Intent(Library.this, OfflinePlayer.class);
                    player.putExtra("PLAYER", chosenSong);
                    player.putExtra("LIST", song);
                    startActivity(player);
                }
            });
        }
    }


}