package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import MusicFile.MusicFile;

public class StartPlayerActivity extends AppCompatActivity implements Serializable, AsyncResponse, ComponentCallbacks2 {
    File download = null;
    String song;
    ArrayList<MusicFile> chunks = new ArrayList<>();
    private ImageButton play;
    private ImageButton stop;
    private TextView SongTextView;
    PlayerTask player = new PlayerTask();
    MediaPlayer mp;
    String path;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_player);

        stop = (ImageButton) findViewById(R.id.StopButton);
        play = (ImageButton) findViewById(R.id.PlayButton);
        SongTextView = (TextView)  findViewById(R.id.SongTextView);

        Intent in = getIntent();
        song = in.getStringExtra("com.example.INDEX4");

        //download = new File(Environment.getExternalStorageDirectory()+"/Download/" + song+ ".mp3");
        //path = download.getPath();
       // System.out.println("song" + song);
        player.delegate = this;
        player.execute(chunks);
        SongTextView.setText(song);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mp = new MediaPlayer();
                try {
                    mp.setDataSource(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mp.isPlaying()) {
                            mp.start();
                            Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mp.isPlaying()) {
                            mp.pause();
                            Toast.makeText(getApplicationContext(),"Stop",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }, 3000);

    }

    @Override
    public void processFinish(HashMap<String, Integer> output) {

    }

    @Override
    public void processFinish(ArrayList<String> output) {

    }

    @Override
    public void processFinish2(ArrayList<MusicFile> result) {
        chunks = result;
        //System.out.println("chunks" + chunks);
    }


    private class PlayerTask extends AsyncTask<ArrayList<MusicFile>, String, ArrayList<MusicFile>> implements Serializable {

        AsyncResponse delegate = null;

        //------- connection with broker here!!------------------
        @Override
        protected final ArrayList<MusicFile> doInBackground(ArrayList<MusicFile>... params) {

            try {

                try {
                    //System.out.println(song);
                    chunks = SocketHandler.writeSong(song);
                   // System.out.println(chunks);

                    if (chunks.isEmpty()) {
                        System.out.println("Not enough info for this song");
                    } else {
                        download = new File(Environment.getExternalStorageDirectory()+"/Download/" + song+ ".mp3");
                        path = download.getPath();
                        mergeFiles(chunks, download);
                        System.out.println("Downloaded");
                    }

                    //System.out.println("chunks from broker" + chunks);

                } catch (UnknownHostException unknownHost) {
                    System.err.println("You are trying to connect to an unknown host!");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return chunks;
        }

        @Override
        protected void onPostExecute(ArrayList<MusicFile> result) {
            delegate.processFinish2(result);
        }

    }
    public static void mergeFiles(ArrayList<MusicFile> musicfiles, File into) throws IOException {
        ArrayList<byte[]> mp3files = new ArrayList<>();
        for(int i = 0; i < musicfiles.size(); i++){
            MusicFile f = musicfiles.get(i);
            mp3files.add(f.getMusicFileEctract());
        }

        try (FileOutputStream fos = new FileOutputStream(into)) {
            for (int i = 0; i < mp3files.size(); i++) {
                fos.write(mp3files.get(i));
            }
        }
    }
}