package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;



public class OfflinePlayer extends AppCompatActivity {

    MediaPlayer mp = new MediaPlayer();
    ImageButton play;
    ImageButton pause;
    ImageButton prev;
    ImageButton next;
    TextView start;
    TextView end;
    TextView SongTextView;
    SeekBar bar;
    String song;
    ArrayList<String> downloaded;
    String path = Environment.getExternalStorageDirectory()+"/Download/";
    int position;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_player);
        prev = (ImageButton) findViewById(R.id.prevButton);
        next = (ImageButton) findViewById(R.id.nextButton);
        play = (ImageButton) findViewById(R.id.playBtn);
        pause = (ImageButton) findViewById(R.id.pauseBtn);
        start = (TextView) findViewById(R.id.startSong);
        end = (TextView)findViewById(R.id.endSong);
        SongTextView = (TextView)  findViewById(R.id.songName);

        bar = (SeekBar) findViewById(R.id.seekBar);
        totalTime = mp.getDuration();
        bar.setMax(totalTime);
        bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mp.seekTo(progress);
                        bar.setProgress(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        Intent in = getIntent();
        song = in.getStringExtra("PLAYER");
        SongTextView.setText(song);
        Serializable data = in.getSerializableExtra("LIST");
        downloaded = (ArrayList<String>) data;
        position = downloaded.indexOf(song);



               CreatePlayer(song);

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mp.isPlaying()) {
                            mp.start();
                            Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mp.isPlaying()) {
                            mp.pause();
                            Toast.makeText(getApplicationContext(),"Stop",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position > 0){
                            position--;
                        }
                        else{
                            position = downloaded.size()-1;
                        }
                        song = downloaded.get(position);
                        mp.reset();
                        CreatePlayer(song);
                    }
                });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < downloaded.size()-1){
                    position++;
                }
                else{
                    position = 0;
                }
                song = downloaded.get(position);
                mp.reset();
                CreatePlayer(song);
            }
        });


    }



    public void CreatePlayer(String song){
        try {
            SongTextView.setText(song);
            mp.setDataSource(path+song+".mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


