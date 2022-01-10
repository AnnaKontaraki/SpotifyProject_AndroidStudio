package MusicFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class MusicFile implements Serializable {
    String track;
    String artist;
    String albumInfo;
    String genre;
    byte[] musicFileEctract;
    static final long serialVersionUID = -4490313382166500541L;


    public MusicFile(String track, String artist, String albumInfo, String genre, byte[] musicFileEctract) {
        this.track = track;
        this.artist = artist;
        this.albumInfo = albumInfo;
        this.genre = genre;
        this.musicFileEctract = musicFileEctract;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumInfo() {
        return albumInfo;
    }

    public void setAlbumInfo(String albumInfo) {
        this.albumInfo = albumInfo;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public byte[] getMusicFileEctract() {
        return musicFileEctract;
    }

    @Override
    public String toString() {
        return "MusicFile{" +
                "track='" + track + '\'' +
                ", artist='" + artist + '\'' +
                ", albumInfo='" + albumInfo + '\'' +
                ", genre='" + genre + '\'';
    }

    public void setMusicFileEctract(byte[] musicFileEctract) {
        this.musicFileEctract = musicFileEctract;
    }

}
