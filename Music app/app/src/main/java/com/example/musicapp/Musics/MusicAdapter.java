package com.example.musicapp.Musics;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Activities.MainActivity;
import com.example.musicapp.Activities.PlayerActivity;
import com.example.musicapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>
{
    public static ArrayList<MusicFile> mFiles;
    private final Context mContext;

    public MusicAdapter(Context mContext, ArrayList<MusicFile> mFiles)
    {
        this.mContext = mContext;
        MusicAdapter.mFiles = mFiles;
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

    public static byte[] getAlbumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        if (mFiles.get(position).color == -1)
            mFiles.get(position).setColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.black));
        else if (mFiles.get(position).color == 0)
            mFiles.get(position).setColor(Color.parseColor("#008B8B"));
        holder.file_name.setTextColor(mFiles.get(position).color);
        holder.album_name.setTextColor(mFiles.get(position).color);

        String file_name_text = mFiles.get(position).getTitle();
        String album_name_text = mFiles.get(position).getAlbum();
        if (file_name_text.length() > 26)
            file_name_text = file_name_text.substring(0, 26) + "...";

        if (album_name_text.length() > 20)
            album_name_text = album_name_text.substring(0, 20) + "...";

        holder.file_name.setText(file_name_text);
        holder.album_name.setText(album_name_text);
        byte[] image = null;
        if (mFiles.get(position).getPath() != null && isPathValid(mFiles.get(position).getPath()))
            image = getAlbumArt(mFiles.get(position).getPath());

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
            Intent intent = new Intent(mContext, PlayerActivity.class);
            intent.putExtra("position", position);
            mContext.startActivity(intent);
        });
        holder.menuMore.setOnClickListener(view ->
        {
            if (PlayerActivity.curr_song != null && PlayerActivity.curr_song == mFiles.get(position))
            {
                Snackbar.make(view, "Can't be deleted while playing!", Snackbar.LENGTH_LONG).show();
                return;
            }

            Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    Long.parseLong(mFiles.get(position).getId()));
            File file = new File(mFiles.get(position).getPath());
            boolean deleted = file.delete();
            if (deleted)
            {
                mContext.getContentResolver().delete(contentUri, null, null);
                mFiles.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mFiles.size());
                MainActivity.musicFiles.remove(position);
                Snackbar.make(view, "File deleted", Snackbar.LENGTH_LONG).show();
            }
            else
                Snackbar.make(view, "Can't be deleted!", Snackbar.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount()
    {
        return mFiles.size();
    }

    public void updateList(ArrayList<MusicFile> musicFileArrayList)
    {
        mFiles = new ArrayList<>();
        mFiles.addAll(musicFileArrayList);
        notifyDataSetChanged();
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
            menuMore = itemView.findViewById(R.id.menuMore);
        }
    }
}
