package com.example.musicapp.Musics;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Activities.PlayerActivity;
import com.example.musicapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.example.musicapp.Activities.MainActivity.musicFiles;
import static com.example.musicapp.Activities.PlayerActivity.mediaPlayer;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>
{
    private final Context mContext;
    private int row_index = -1;

    public MusicAdapter(Context mContext)
    {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new MyViewHolder(view);
    }

    public static boolean isPathValid(String path)
    {
        try
        {
            Paths.get(path);
        }
        catch (InvalidPathException ex)
        {
            return false;
        }

        return true;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        String file_name_text = musicFiles.get(position).getTitle();
        String album_name_text = musicFiles.get(position).getAlbum();
        if (file_name_text.length() > 26)
            file_name_text = file_name_text.substring(0, 26) + "...";

        if (album_name_text.length() > 20)
            album_name_text = album_name_text.substring(0, 20) + "...";

        holder.file_name.setText(file_name_text);
        holder.album_name.setText(album_name_text);
        byte[] image = null;
        if (musicFiles.get(position).getPath() != null && isPathValid(musicFiles.get(position).getPath()))
            image = getAlbumArt(musicFiles.get(position).getPath());
        if (image != null)
        {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.album_art);
        }
        else
        {
            Glide.with(mContext)
                    .load(R.drawable.musicimage)
                    .into(holder.album_art);
        }
        holder.itemView.setOnClickListener(v ->
        {
            row_index = position;
            notifyDataSetChanged();
            Intent intent = new Intent(mContext, PlayerActivity.class);
            intent.putExtra("position", position);
            mContext.startActivity(intent);
        });
        if (row_index == position)
        {
            holder.file_name.setTextColor(Color.parseColor("#008B8B"));
            holder.album_name.setTextColor(Color.parseColor("#008B8B"));
        }
        else
        {
            holder.file_name.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.black));
            holder.album_name.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.black));
        }
        holder.menuMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if (item.getItemId() == R.id.delete)
                            deleteFile(position, v);

                        return true;
                    }
                });
            }
        });
    }

    public void deleteFile(int position, View view)
    {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(musicFiles.get(position).getId()));
        File file = new File(musicFiles.get(position).getPath());
        boolean deleted = file.delete(); // delete file
        if (deleted)
        {
            mContext.getContentResolver().delete(contentUri, null, null);
            musicFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, musicFiles.size());
            Snackbar.make(view, "File deleted", Snackbar.LENGTH_LONG).show();
        }
        // else is when the file is in the sd card
        else
            Snackbar.make(view, "Can't be deleted!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount()
    {
        return musicFiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView file_name, album_name;
        ImageView album_art, menuMore;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_name = itemView.findViewById(R.id.album_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            menuMore =  itemView.findViewById(R.id.menuMore);
        }
    }

    private byte[] getAlbumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
