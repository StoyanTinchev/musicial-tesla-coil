package com.example.musicapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activities.MainActivity;
import com.example.musicapp.Musics.AlbumAdapter;
import com.example.musicapp.Musics.MusicFile;
import com.example.musicapp.R;

public class AlbumFragment extends Fragment
{
    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;

    public AlbumFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if (MainActivity.albums.size() > 0)
        {
            albumAdapter = new AlbumAdapter(getContext(), MainActivity.albums);
            recyclerView.setAdapter(albumAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        return view;
    }

    private ItemTouchHelper.Callback createHelperCallback()
    {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
                ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
            {
                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
            }
        };
    }

    public void moveItem(int oldPos, int newPos)
    {
        MusicFile musicFile = MainActivity.musicFiles.get(oldPos);
        MainActivity.musicFiles.remove(oldPos);
        MainActivity.musicFiles.add(newPos, musicFile);
        albumAdapter.notifyItemMoved(oldPos, newPos);
    }
}