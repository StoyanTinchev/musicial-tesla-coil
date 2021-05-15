package com.example.musicapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Musics.AlbumAdapter;
import com.example.musicapp.Musics.MusicAdapter;
import com.example.musicapp.R;

import static com.example.musicapp.Activities.MainActivity.albums;
import static com.example.musicapp.Activities.MainActivity.musicFiles;

public class AlbumFragment extends Fragment
{
    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    public AlbumFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if (albums.size() > 0)
        {
            albumAdapter = new AlbumAdapter(getContext(), albums);
            recyclerView.setAdapter(albumAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        return view;
    }
}