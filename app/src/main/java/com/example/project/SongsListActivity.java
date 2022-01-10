package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import MusicFile.MusicFile;


public class SongsListActivity extends AppCompatActivity implements Serializable, AsyncResponse, ComponentCallbacks2 {

    SecondTask second = new SecondTask();
    HashMap<String,Integer> info;
    ArrayList<String> songs;
    ArrayList<String> artist;
    private ProgressBar bar;
    int portNumber;
    String theArtist;

    public void onTrimMemory(int level) {

        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:

                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:

                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:

                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */

                break;

            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);

        bar = (ProgressBar) findViewById(R.id.progressBar);
        Intent in = getIntent();
        Serializable data = in.getSerializableExtra("com.example.INDEX2");
        info = (HashMap<String, Integer>) data;
        artist = new ArrayList<String>(info.keySet());
        theArtist = artist.get(0);
        portNumber = info.get(theArtist);
        second.delegate = this;
        second.execute(songs);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent middle = new Intent(SongsListActivity.this, SongsActivity.class);

                //System.out.println("list of songs before activity send it: " + songs);
                middle.putExtra("com.example.INDEX3", songs); //pass the list of songs to songs activity
                startActivity(middle);

            }

        }, 2000);
}




    @Override
    public void processFinish(HashMap<String, Integer> output) {

    }

    @Override
    public void processFinish(ArrayList<String> output) {
        songs = output;
        //System.out.println("songs list activity --> songs" + songs);
        //System.out.println("songs list activity --> output" + output);
    }

    @Override
    public void processFinish2(ArrayList<MusicFile> result) {

    }


    private class SecondTask extends AsyncTask<ArrayList<String>, String, ArrayList<String>> implements Serializable {

        AsyncResponse delegate = null;

        //------- connection with broker here!!------------------
        @Override
        protected final ArrayList<String> doInBackground(ArrayList<String>... params) {

            try {
                ObjectInputStream in = null;
                ObjectOutputStream out = null;
                try {
                    // int portNumber = hashTable.get(artist.get(position));
                    SocketHandler.createSocket("192.168.56.1", portNumber);

                    songs = (ArrayList<String>) SocketHandler.write(theArtist);
                    //System.out.println("list of songs that songs list activity take from broker " + songs);

                } catch (UnknownHostException unknownHost) {
                    System.err.println("You are trying to connect to an unknown host!");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return songs;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            delegate.processFinish(result);
        }

    }

}