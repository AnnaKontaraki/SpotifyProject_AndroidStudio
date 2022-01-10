package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SecondActivity extends AppCompatActivity implements Serializable, ComponentCallbacks2 {
    ListView ArtistListView;
    String[] artists;
    ArrayList<String> artist;
    HashMap<String,Integer> hashTable;
    HashMap<String,Integer> info = new HashMap<String, Integer>();

    String chosen;
    int portNumber;

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
        setContentView(R.layout.activity_second);
        //--------- Initialize list based on list given by the communication with broker
        Intent secondIntent = getIntent();
        Serializable data= secondIntent.getSerializableExtra("com.example.INDEX");
        hashTable = (HashMap<String, Integer>) data;
       // System.out.println("Second Activity take hashTable from main activity " + hashTable);
        artist = new ArrayList<String>(hashTable.keySet());
        artists = new String[artist.size()];
        for(int i= 0; i< artist.size(); i++){
            artists[i] = artist.get(i);
        }
       // System.out.println("List of artists " + artist);


        //-----end of initialization-----------
        ArtistListView = (ListView) findViewById(R.id.ArtistListView);
        ArtistListView.setAdapter(new ArrayAdapter<String>(this, R.layout.my_artist_detail, artists));
    }
    public  void onStart(){
        super.onStart();
        ArtistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                chosen = artists[position];
                portNumber = hashTable.get(chosen);
                info.put(chosen, portNumber);

                Intent showSongsActivity = new Intent(SecondActivity.this, SongsListActivity.class);
                showSongsActivity.putExtra("com.example.INDEX2", info);//passes the position of the chosen artist to the next activity
                startActivity(showSongsActivity);
            }
        });

    }

}