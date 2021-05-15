package com.example.musicapp.Musics;

import android.graphics.Color;

public class MusicFile
{
    private final String album;
    private final String title;
    private final String duration;
    private final String path;
    private final String artist;
    private final String id;
//    public Color color = null;
//
//    public void setColor(Color color)
//    {
//        this.color = color;
//    }

    public MusicFile(String album, String title, String duration, String path,
                     String artist, String id)
    {
        this.album = album;
        this.title = title;
        this.duration = duration;
        this.path = path;
        this.artist = artist;
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getPath()
    {
        return path;
    }

    public String getAlbum()
    {
        return album;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getDuration()
    {
        return duration;
    }
}
