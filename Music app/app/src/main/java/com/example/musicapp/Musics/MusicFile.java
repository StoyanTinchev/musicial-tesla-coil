package com.example.musicapp.Musics;

import android.net.Uri;

import java.io.File;
import java.util.Objects;

public class MusicFile
{
    private final String album;
    private final String title;
    private final String duration;
    private final String path;
    private final String artist;
    private final String id;
    public int color = -1;

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

    public Uri getUri()
    {
        File file = new File(path);
        return Uri.fromFile(file);
    }

    public void setColor(int color)
    {
        this.color = color;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicFile musicFile = (MusicFile) o;
        return color == musicFile.color &&
                album.equals(musicFile.album) &&
                title.equals(musicFile.title) &&
                duration.equals(musicFile.duration) &&
                path.equals(musicFile.path) &&
                artist.equals(musicFile.artist) &&
                id.equals(musicFile.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(album, title, duration, path, artist, id, color);
    }
}
