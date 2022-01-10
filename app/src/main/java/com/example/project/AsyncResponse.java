package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;

import MusicFile.MusicFile;

public interface AsyncResponse {
    void processFinish(HashMap<String, Integer> output);
    void processFinish(ArrayList<String> output);

    void processFinish2(ArrayList<MusicFile> result);
}