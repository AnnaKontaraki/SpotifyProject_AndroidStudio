package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import MusicFile.MusicFile;



public class MainActivity extends AppCompatActivity implements Serializable, AsyncResponse {

    private static final String TAG = "Permission" ;
    private Button showArtists;
    public HashMap<String,Integer> hash = new HashMap<>();

    MainTask main = new MainTask();

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                System.out.println("PERM");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                System.out.println("PERM");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            System.out.println("PERM");
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showArtists =(Button) findViewById(R.id.ShowArtistsButton);

        Boolean granted = isStoragePermissionGranted();
        System.out.println(granted);
        main.delegate = this;
        main.execute(hash);

    }

    public void onStart() {
        super.onStart();

        showArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, SecondActivity.class);
               // System.out.println("Main Activity send hash " + hash);
                startIntent.putExtra("com.example.INDEX", hash); //pass the list of artist to second activity
                startActivity(startIntent);
            }
        });

    }

    @Override
    public void processFinish(HashMap<String,Integer> output) {
        hash = output;
       // System.out.println("Hash" + hash);
        // System.out.println("Output" + output);
    }

    @Override
    public void processFinish(ArrayList<String> output) {

    }

    @Override
    public void processFinish2(ArrayList<MusicFile> result) {

    }


    private class MainTask extends AsyncTask<HashMap<String,Integer>, String, HashMap<String, Integer>> implements Serializable {

        AsyncResponse delegate = null;

        //-------first connection with broker here!!------------------
        @Override
        protected final HashMap<String, Integer> doInBackground(HashMap<String,Integer>... params) {
            try {
                Socket client = null;
                ObjectInputStream in = null;
                ObjectOutputStream out = null;
                try {
                    while(hash.isEmpty()) {
                        client = new Socket("192.168.56.1", 9070);

                        in = new ObjectInputStream(client.getInputStream());
                        out = new ObjectOutputStream(client.getOutputStream());

                        out.writeObject("@");
                        out.flush();
                        Object hashtable = in.readObject();

                        Hashtable<String, Integer> temp = (Hashtable<String, Integer>) hashtable;
                        //System.out.println("Main Activity take hashTable from broker " + temp);

                        for (Map.Entry<String, Integer> entry : temp.entrySet()) {
                            String key = entry.getKey();
                            Integer value = entry.getValue();
                            hash.put(key, value);
                        }
                    }
                } catch (UnknownHostException unknownHost) {
                    System.err.println("You are trying to connect to an unknown host!");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } finally {
                    try {
                        in.close();
                        out.close();
                        client.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return hash;
        }

        @Override
        protected void onPostExecute(HashMap<String, Integer> result) {
            delegate.processFinish(result);

        }

    }
}