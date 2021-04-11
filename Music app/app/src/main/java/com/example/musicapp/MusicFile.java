package com.example.musicapp;

public class MusicFile
{
    private String album;
    private String title;
    private String duration;
    private String path;
    private String artist;

    public MusicFile(String album, String title, String duration, String path, String artist)
    {
        this.album = album;
        this.title = title;
        this.duration = duration;
        this.path = path;
        this.artist = artist;
    }
}
