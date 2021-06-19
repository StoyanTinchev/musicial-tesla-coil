package com.example.musicapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.musicapp.Fragments.AlbumFragment;
import com.example.musicapp.Fragments.SongsFragment;
import com.example.musicapp.Musics.MusicFile;
import com.example.musicapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
{
    public static final int REQUEST_CODE = 1;
    public static ArrayList<MusicFile> musicFiles;
    public static boolean shuffleBoolean = false, repeatBoolean = false;
    public static ArrayList<MusicFile> albums = new ArrayList<>();
    static ArrayList<String> duplicate = new ArrayList<>();

    public static ArrayList<MusicFile> getAllAudio(Context context)
    {
        ArrayList<MusicFile> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection =
                {
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media._ID
                };

        Cursor cursor = context.getContentResolver().query(uri, projection,
                MediaStore.Audio.Media.IS_MUSIC + "=1", null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                String id = cursor.getString(5);

                MusicFile musicFile = new MusicFile(album, title, duration, path, artist, id);

                Log.e("Path: " + path, " Album: " + album);

                tempAudioList.add(musicFile);
                if (!duplicate.contains(album))
                {
                    albums.add(musicFile);
                    duplicate.add(album);
                }
            }
            cursor.close();
        }
        return tempAudioList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
    }

    private void permission()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , REQUEST_CODE);
        }
        else
        {
            musicFiles = getAllAudio(this);
            initViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                musicFiles = getAllAudio(this);
                initViewPager();
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , REQUEST_CODE);
            }
    }

    private void initViewPager()
    {
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new SongsFragment(), "Songs");
        viewPagerAdapter.addFragments(new AlbumFragment(), "Albums");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_option);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        String userInput = newText.toLowerCase();
        ArrayList<MusicFile> myFiles = new ArrayList<>();
        for (MusicFile songs : musicFiles)
            if (songs.getTitle().toLowerCase().contains(userInput))
                myFiles.add(songs);

        SongsFragment.musicAdapter.updateList(myFiles);
        return true;
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final ArrayList<Fragment> fragments;
        private final ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm)
        {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        void addFragments(Fragment fragment, String title)
        {
            this.fragments.add(fragment);
            this.titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position)
        {
            return this.fragments.get(position);
        }

        @Override
        public int getCount()
        {
            return this.fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position)
        {
            return this.titles.get(position);
        }
    }
}