package com.example.project;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import MusicFile.MusicFile;

public  class  SocketHandler implements Serializable {

        private static Socket socket;
        private static ObjectInputStream in;
        private static ObjectOutputStream out;

        public static synchronized Socket getSocket(){
            return socket;
        }

        public static synchronized void createSocket(String ip, int port) throws IOException {
            socket = new Socket(ip, port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        }

        public static synchronized void closeSocket() throws IOException {
            socket.close();
            in.close();
            out.close();
        }

        public static synchronized Object write(String msg) throws IOException, ClassNotFoundException {
            out.writeObject(msg);
            out.flush();
            return in.readObject();
        }

        public static  synchronized ArrayList<MusicFile> writeSong(String song) throws IOException, ClassNotFoundException {
            Object o;
            MusicFile chunk;
            ArrayList<MusicFile> chunks = new ArrayList<MusicFile>();

            out.writeObject(song);
            out.flush();
            while (true) {
                o = in.readObject();
                chunk = (MusicFile) o;
                if (chunk != null) {
                    chunks.add(chunk);
                } else {
                    break;
                }
            }
            if(chunks.isEmpty()){
                System.out.println("Not enough info for this song");
            }

            return chunks;
        }

}
