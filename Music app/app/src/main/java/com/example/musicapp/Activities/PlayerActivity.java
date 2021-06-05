package com.example.musicapp.Activities;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.musicapp.Fragments.SongsFragment;
import com.example.musicapp.Musics.AlbumDetailsAdapter;
import com.example.musicapp.Musics.MusicAdapter;
import com.example.musicapp.Musics.MusicFile;
import com.example.musicapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener
{
    public static MusicFile prev_song, curr_song;
    public static MediaPlayer mediaPlayer;
    static Uri uri;
    TextView song_name, artist_name, duration_played, duration_total;
    ImageView cover_art, nextBtn, prevBtn, backBtn, shuffleBtn, repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position = -1;
    ArrayList<MusicFile> listSongs = new ArrayList<>();
    private Thread playThread, prevThread, nextThread;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentMethod();
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        if (mediaPlayer == null)
            mediaPlayer.setOnCompletionListener(this);

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
        shuffleBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (MainActivity.shuffleBoolean)
                {
                    MainActivity.shuffleBoolean = false;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle_off);
                }
                else
                {
                    MainActivity.shuffleBoolean = true;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
                }
            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (MainActivity.repeatBoolean)
                {
                    MainActivity.repeatBoolean = false;
                    repeatBtn.setImageResource(R.drawable.ic_repeat_off);
                }
                else
                {
                    MainActivity.repeatBoolean = true;
                    repeatBtn.setImageResource(R.drawable.ic_repeat_on);
                }
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

    private void prevBtnSupp()
    {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (MainActivity.shuffleBoolean && !MainActivity.repeatBoolean)
            position = getRandom(listSongs.size() - 1);
        else if (!MainActivity.shuffleBoolean && !MainActivity.repeatBoolean)
            position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
        // else position will be the same ...


        uri = Uri.parse(listSongs.get(position).getPath());
        prev_song = listSongs.get(position);
        curr_song = prev_song;
        if (MainActivity.musicFiles.contains(curr_song))
        {
            for (MusicFile musicFile : MainActivity.musicFiles)
                if (musicFile == curr_song)
                    musicFile.setColor(0);
                else
                    musicFile.setColor(-1);
            SongsFragment.musicAdapter.updateList(MainActivity.musicFiles);
            if (AlbumDetailsAdapter.albumFiles != null && AlbumDetailsAdapter.albumFiles.contains(curr_song))
                AlbumDetails.albumDetailsAdapter.updateList(AlbumDetailsAdapter.albumFiles);
        }
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
        mediaPlayer.setOnCompletionListener(this);
    }

    private void prevBtnClicked()
    {
        if (mediaPlayer.isPlaying())
        {
            prevBtnSupp();
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else
        {
            prevBtnSupp();
            playPauseBtn.setBackgroundResource(R.drawable.ic_play_arrow);
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

    private void nextBtnSupp()
    {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (MainActivity.shuffleBoolean && !MainActivity.repeatBoolean)
            position = getRandom(listSongs.size() - 1);
        else if (!MainActivity.shuffleBoolean && !MainActivity.repeatBoolean)
            position = ((position + 1) > (listSongs.size() - 1) ? 0 : (position + 1));
        // else position will be the same ...


        uri = Uri.parse(listSongs.get(position).getPath());
        prev_song = listSongs.get(position);
        curr_song = prev_song;
        if (MainActivity.musicFiles.contains(curr_song))
        {
            for (MusicFile musicFile : MainActivity.musicFiles)
                if (musicFile == curr_song)
                    musicFile.setColor(0);
                else
                    musicFile.setColor(-1);
            SongsFragment.musicAdapter.updateList(MainActivity.musicFiles);
            if (AlbumDetailsAdapter.albumFiles != null && AlbumDetailsAdapter.albumFiles.contains(curr_song))
                AlbumDetails.albumDetailsAdapter.updateList(AlbumDetailsAdapter.albumFiles);
        }
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
        mediaPlayer.setOnCompletionListener(this);
    }

    private void nextBtnClicked()
    {
        if (mediaPlayer.isPlaying())
        {
            nextBtnSupp();
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else
        {
            nextBtnSupp();
            playPauseBtn.setBackgroundResource(R.drawable.ic_play_arrow);
        }
    }

    private int getRandom(int num)
    {
        Random random = new Random();
        return random.nextInt(num + 1);
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

    private void getIntentMethod()
    {
        position = getIntent().getIntExtra("position", -1);
        String sender = getIntent().getStringExtra("sender");
        if (sender != null && sender.equals("albumDetails"))
            listSongs = AlbumDetailsAdapter.albumFiles;
        else
            listSongs = MusicAdapter.mFiles;
        curr_song = listSongs != null ? listSongs.get(position) : null;


        if (mediaPlayer != null)
        {
            if (prev_song == curr_song)
            {
                if (mediaPlayer.isPlaying())
                    playPauseBtn.setImageResource(R.drawable.ic_pause);
                else
                    playPauseBtn.setImageResource(R.drawable.ic_play_arrow);

                if (MainActivity.shuffleBoolean)
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
                else
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle_off);

                if (MainActivity.repeatBoolean)
                    repeatBtn.setImageResource(R.drawable.ic_repeat_on);
                else
                    repeatBtn.setImageResource(R.drawable.ic_repeat_off);

                uri = Uri.parse(listSongs.get(position).getPath());
                seekBar.setMax(mediaPlayer.getDuration() / 1000);
                seekBar.setProgress(mediaPlayer.getCurrentPosition() / 1000);
                metaData(uri);
                return;
            }
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (MainActivity.musicFiles != null && MainActivity.musicFiles.contains(curr_song))
        {
            for (MusicFile musicFile : MainActivity.musicFiles)
                if (musicFile == curr_song)
                    musicFile.setColor(0);
                else
                    musicFile.setColor(-1);
            SongsFragment.musicAdapter.updateList(MainActivity.musicFiles);
            if (AlbumDetailsAdapter.albumFiles != null && AlbumDetailsAdapter.albumFiles.contains(curr_song))
                AlbumDetails.albumDetailsAdapter.updateList(AlbumDetailsAdapter.albumFiles);
        }
        prev_song = curr_song;
        if (listSongs != null)
        {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
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

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        nextBtnClicked();
        if (mediaPlayer != null)
        {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}