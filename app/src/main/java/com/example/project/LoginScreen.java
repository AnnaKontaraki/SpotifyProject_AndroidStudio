package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import MusicFile.MusicFile;

public class LoginScreen extends AppCompatActivity implements Serializable, AsyncResponse {

    private Switch offlineSwitch;
    private Button continueButton;
    private String path = Environment.getExternalStorageDirectory()+"/Download/";
    ArrayList<String> songslist = new ArrayList<String>();
    LoginTask login = new LoginTask();
    @Override

   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        login.delegate = this;
        login.execute(songslist);

        offlineSwitch = (Switch) findViewById(R.id.offlineSwitch);
        continueButton = (Button) findViewById(R.id.continueButton);

    }

    public void onStart() {
        super.onStart();
        offlineSwitch.setChecked(true);
        offlineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            continueButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent library = new Intent(LoginScreen.this, Library.class);
                                    System.out.println(songslist);
                                    library.putExtra("DOWNSONGS", songslist);
                                    startActivity(library);
                                }
                            });
                        }
                        else{
                            continueButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent main = new Intent(LoginScreen.this, MainActivity.class);
                                    startActivity(main);
                                }
                            });
                        }
                    }
                });





    }

    @Override
    public void processFinish(HashMap<String, Integer> output) {

    }

    @Override
    public void processFinish(ArrayList<String> output) {
        songslist = output;
    }

    @Override
    public void processFinish2(ArrayList<MusicFile> result) {

    }

    private class LoginTask extends AsyncTask<ArrayList<String>, String, ArrayList<String>> implements Serializable {

        AsyncResponse delegate = null;

        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... params) {
            File downloads = new File(path);

            if ( downloads.listFiles() == null)
                return null;
            for (final File fileEntry : Objects.requireNonNull(downloads.listFiles())) {
                if (!fileEntry.isDirectory()){
                    String sub = fileEntry.getName();

                    if(sub.endsWith(".mp3")) {
                        String name = sub.substring(0,fileEntry.getName().length()-4);
                        if(!songslist.contains(name)) {
                            songslist.add(name);
                        }
                    }
                }
            }

            return songslist;
        }
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            delegate.processFinish(result);

        }
    }
}