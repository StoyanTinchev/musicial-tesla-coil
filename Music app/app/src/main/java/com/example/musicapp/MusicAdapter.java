package com.example.musicapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>
{
    private final Context mContext;
    private final ArrayList<MusicFile> mFiles;
    private MyViewHolder prev = null;

    public MusicAdapter(Context mContext, ArrayList<MusicFile> mFiles)
    {
        this.mContext = mContext;
        this.mFiles = mFiles;
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
        holder.file_name.setText(mFiles.get(position).getTitle());
        holder.album_name.setText(mFiles.get(position).getAlbum());
        byte[] image = null;
        if (mFiles.get(position).getPath() != null && isPathValid(mFiles.get(position).getPath()))
        {
            image = getAlbumArt(mFiles.get(position).getPath());
        }
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
            if (prev == null)
                prev = holder;
            else
            {
                prev.file_name.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.black));
                prev.album_name.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.black));
                prev = holder;
            }
            Intent intent = new Intent(mContext, PlayerActivity.class);
            intent.putExtra("position", position);
            mContext.startActivity(intent);
            holder.file_name.setTextColor(Color.parseColor("#3F51B5"));
            holder.album_name.setTextColor(Color.parseColor("#32408f"));
        });
    }

    @Override
    public int getItemCount()
    {
        return mFiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView file_name, album_name;
        ImageView album_art;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_name = itemView.findViewById(R.id.album_file_name);
            album_art = itemView.findViewById(R.id.music_img);
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
