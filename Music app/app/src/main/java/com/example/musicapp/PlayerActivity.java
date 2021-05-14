package com.example.musicapp;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.musicapp.MainActivity.musicFiles;

public class PlayerActivity extends AppCompatActivity
{
    TextView song_name, artist_name, duration_played, duration_total;
    ImageView cover_art, nextBtn, prevBtn, backBtn, shuffleBtn, repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position = -1;
    ArrayList<MusicFile> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Thread playThread, prevThread, nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentMethod();
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (mediaPlayer != null && fromUser)
                    mediaPlayer.seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (mediaPlayer != null)
                {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_played.setText(formattedTime(mCurrentPosition));
                }
                new Handler(Looper.getMainLooper()).postDelayed(this, 10);
            }
        });
    }

    @Override
    protected void onResume()
    {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn()
    {
        prevThread = new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                prevBtn.setOnClickListener(v -> prevBtnClicked());
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (mediaPlayer != null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(this, 10);
                }
            });
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (mediaPlayer != null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(this, 10);
                }
            });
            playPauseBtn.setImageResource(R.drawable.ic_play_arrow);
        }
    }

    private void nextThreadBtn()
    {
        nextThread = new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                nextBtn.setOnClickListener(v -> nextBtnClicked());
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) > (listSongs.size() - 1) ? 0 : (position + 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (mediaPlayer != null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(this, 10);
                }
            });
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) > (listSongs.size() - 1) ? 0 : (position + 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (mediaPlayer != null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(this, 10);
                }
            });
            playPauseBtn.setImageResource(R.drawable.ic_play_arrow);
        }
    }

    private void playThreadBtn()
    {
        playThread = new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                playPauseBtn.setOnClickListener(v -> playPauseBtnClicked());
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked()
    {
        if (mediaPlayer.isPlaying())
        {
            playPauseBtn.setImageResource(R.drawable.ic_play_arrow);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (mediaPlayer != null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(this, 10);
                }
            });
        }
        else
        {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (mediaPlayer != null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(this, 10);
                }
            });
        }
    }

    public static String formattedTime(int mCurrentPosition)
    {
        String totalOut;
        String totalNew;
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;

        if (seconds.length() == 1)
            return totalNew;
        else
            return totalOut;
    }

    private void getIntentMethod()
    {
        position = getIntent().getIntExtra("position", -1);
        listSongs = musicFiles;
        if (listSongs != null)
        {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);
    }

    private void initViews()
    {
        song_name = findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.durationPlayed);
        duration_total = findViewById(R.id.duration);
        cover_art = findViewById(R.id.cover_art);
        nextBtn = findViewById(R.id.id_next);
        prevBtn = findViewById(R.id.id_prev);
        backBtn = findViewById(R.id.back_btn);
        shuffleBtn = findViewById(R.id.id_shuffle);
        repeatBtn = findViewById(R.id.id_repeat);
        playPauseBtn = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.seekBard);
    }

    private void metaData(Uri uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
        duration_total.setText(formattedTime(durationTotal));
        byte[] art = retriever.getEmbeddedPicture();
        if (art != null)
        {
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(cover_art);
        }
        else
        {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.musicimage)
                    .into(cover_art);
        }
    }
}