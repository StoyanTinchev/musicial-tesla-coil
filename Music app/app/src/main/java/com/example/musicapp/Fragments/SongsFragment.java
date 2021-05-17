package com.example.musicapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activities.MainActivity;
import com.example.musicapp.Musics.MusicAdapter;
import com.example.musicapp.Musics.MusicFile;
import com.example.musicapp.R;


public class SongsFragment extends Fragment
{
    RecyclerView recyclerView;
    public static MusicAdapter musicAdapter;

    public SongsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if (MainActivity.musicFiles.size() > 0)
        {
            musicAdapter = new MusicAdapter(getContext(), MainActivity.musicFiles);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        return view;
    }

    private ItemTouchHelper.Callback createHelperCallback()
    {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
                ItemTouchHelper.DOWN, 0)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
            {
                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { }
        };
    }

    public void moveItem(int oldPos, int newPos)
    {
        MusicFile musicFile = MainActivity.musicFiles.get(oldPos);
        MainActivity.musicFiles.remove(oldPos);
        MainActivity.musicFiles.add(newPos, musicFile);
        musicAdapter.notifyItemMoved(oldPos, newPos);
    }


}