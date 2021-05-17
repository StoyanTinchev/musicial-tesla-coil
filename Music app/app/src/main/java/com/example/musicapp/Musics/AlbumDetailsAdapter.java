package com.example.musicapp.Musics;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
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
import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyHolder>
{
    private Context mContext;
    public static ArrayList<MusicFile> albumFiles;
    View view;

    public AlbumDetailsAdapter(Context mContext, ArrayList<MusicFile> albumFiles)
    {
        this.mContext = mContext;
        AlbumDetailsAdapter.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public AlbumDetailsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new AlbumDetailsAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailsAdapter.MyHolder holder, int position)
    {
        if (albumFiles.get(position).color == -1)
            albumFiles.get(position).setColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.black));
        else if (albumFiles.get(position).color == 0)
            albumFiles.get(position).setColor(Color.parseColor("#008B8B"));

        holder.song_title.setTextColor(albumFiles.get(position).color);
        holder.album_name.setTextColor(albumFiles.get(position).color);

        String file_name_text = albumFiles.get(position).getTitle();
        String album_name_text = albumFiles.get(position).getAlbum();
        if (file_name_text.length() > 26)
            file_name_text = file_name_text.substring(0, 26) + "...";

        if (album_name_text.length() > 20)
            album_name_text = album_name_text.substring(0, 20) + "...";

        holder.song_title.setText(file_name_text);
        holder.album_name.setText(album_name_text);
        byte[] image = null;
        if (albumFiles.get(position).getPath() != null && MusicAdapter.isPathValid(albumFiles.get(position).getPath()))
            image = MusicAdapter.getAlbumArt(albumFiles.get(position).getPath());

        if (image != null)
        {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.album_image);
        }
        else
        {
            Glide.with(mContext)
                    .load(R.drawable.musicimage)
                    .into(holder.album_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("sender", "albumDetails");
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
        holder.menu_more.setOnClickListener(new View.OnClickListener()
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

    @Override
    public int getItemCount()
    {
        return albumFiles.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView album_image, menu_more;
        TextView song_title, album_name;

        public MyHolder(@NonNull View itemView)
        {
            super(itemView);
            album_image = itemView.findViewById(R.id.music_img);
            song_title = itemView.findViewById(R.id.music_file_name);
            album_name = itemView.findViewById(R.id.album_file_name);
            menu_more = itemView.findViewById(R.id.menuMore);
        }
    }

    public void deleteFile(int position, View view)
    {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(albumFiles.get(position).getId()));
        File file = new File(albumFiles.get(position).getPath());
        boolean deleted = file.delete(); // delete file
        if (deleted)
        {
            mContext.getContentResolver().delete(contentUri, null, null);
            albumFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, albumFiles.size());
            Snackbar.make(view, "File deleted", Snackbar.LENGTH_LONG).show();
        }
        // else is when the file is in the sd card
        else
            Snackbar.make(view, "Can't be deleted!", Snackbar.LENGTH_LONG).show();
    }

    public void updateList(ArrayList<MusicFile> musicFileArrayList)
    {
        albumFiles = new ArrayList<>();
        albumFiles.addAll(musicFileArrayList);
        notifyDataSetChanged();
    }
}
