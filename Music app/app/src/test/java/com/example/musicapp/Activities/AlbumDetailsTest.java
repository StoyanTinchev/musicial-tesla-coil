package com.example.musicapp.Activities;

import com.example.musicapp.Musics.MusicFile;

import junit.framework.TestCase;

import java.util.ArrayList;

public class AlbumDetailsTest extends TestCase
{
    private AlbumDetails albumDetails = new AlbumDetails();
    public void setUp() throws Exception
    {
        super.setUp();
        MainActivity.musicFiles = new ArrayList<MusicFile>()
        {
            {
                add(new MusicFile("Faktor", "1", null, null, null, null));
                add(new MusicFile("Faktor", "2", null, null, null, null));
                add(new MusicFile("Faktor", "3", null, null, null, null));
                add(new MusicFile("Faktor", "4", null, null, null, null));
                add(new MusicFile("Faktor", "5", null, null, null, null));
                add(new MusicFile("Faktor", "6", null, null, null, null));
            }
        };
    }

    public void testMoveItem()
    {
        MusicFile musicFile = MainActivity.musicFiles.get(1);
        albumDetails.moveItem(1, 4);
        assertEquals(musicFile, MainActivity.musicFiles.get(4));
    }
}