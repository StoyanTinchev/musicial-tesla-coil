package com.example.musicapp;

public class MusicFile
{
    private final String album;
    private final String title;
    private final String duration;
    private final String path;
    private final String artist;

    public MusicFile(String album, String title, String duration, String path, String artist)
    {
        this.album = album;
        this.title = title;
        this.duration = duration;
        this.path = path;
        this.artist = artist;
    }

    public String getTitle()
    {
        return title;
    }

    public String getPath()
    {
        return path;
    }
}
