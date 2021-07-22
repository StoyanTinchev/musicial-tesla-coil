package com.example.musicapp.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.bumptech.glide.Glide;
import com.example.musicapp.Fragments.SongsFragment;
import com.example.musicapp.Musics.AlbumDetailsAdapter;
import com.example.musicapp.Musics.MusicAdapter;
import com.example.musicapp.Musics.MusicFile;
import com.example.musicapp.NotificationPlaying.ActionPlaying;
import com.example.musicapp.NotificationPlaying.MusicService;
import com.example.musicapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity implements
        ServiceConnection, ActionPlaying {
    public static MusicFile prev_song, curr_song;
    public static ArrayList<MusicFile> listSongs = new ArrayList<>();
    static Uri uri;
    TextView song_name, artist_name, duration_played, duration_total;
    ImageView cover_art, nextBtn, prevBtn, backBtn, shuffleBtn, repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position = -1;
    MusicService musicService;

    public static String formattedTime(int mCurrentPosition) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_player);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initViews();
        getIntentMethod();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService != null && fromUser)
                    musicService.seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_played.setText(formattedTime(mCurrentPosition));
                }
                new Handler(Looper.getMainLooper()).postDelayed(this, 10);
            }
        });
        shuffleBtn.setOnClickListener(v ->
        {
            if (MainActivity.shuffleBoolean) {
                MainActivity.shuffleBoolean = false;
                shuffleBtn.setImageResource(R.drawable.ic_shuffle_off);
            } else {
                MainActivity.shuffleBoolean = true;
                shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
            }
        });
        repeatBtn.setOnClickListener(v ->
        {
            if (MainActivity.repeatBoolean) {
                MainActivity.repeatBoolean = false;
                repeatBtn.setImageResource(R.drawable.ic_repeat_off);
            } else {
                MainActivity.repeatBoolean = true;
                repeatBtn.setImageResource(R.drawable.ic_repeat_on);
            }
        });
        backBtn.setOnClickListener(v -> finish());
    }

    @SuppressWarnings("DEPRECATION")
    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private void prevThreadBtn() {
        Thread prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(v -> prevBtnClicked());
            }
        };
        prevThread.start();
    }

    private void prevBtnSupp() {
        musicService.stop();
        musicService.release();
        if (MainActivity.shuffleBoolean && !MainActivity.repeatBoolean)
            position = getRandom(listSongs.size() - 1);
        else if (!MainActivity.shuffleBoolean && !MainActivity.repeatBoolean)
            position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
        // else position will be the same ...


        uri = Uri.parse(listSongs.get(position).getPath());
        prev_song = listSongs.get(position);
        curr_song = prev_song;
        if (MainActivity.musicFiles.contains(curr_song)) {
            for (MusicFile musicFile : MainActivity.musicFiles)
                if (musicFile == curr_song)
                    musicFile.setColor(0);
                else
                    musicFile.setColor(-1);
            SongsFragment.musicAdapter.updateList(MainActivity.musicFiles);
            if (AlbumDetailsAdapter.albumFiles != null && AlbumDetailsAdapter.albumFiles.contains(curr_song))
                AlbumDetails.albumDetailsAdapter.updateList(AlbumDetailsAdapter.albumFiles);
        }
        musicService.createMediaPlayer(position);
        metaData(uri);
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        seekBar.setMax(musicService.getDuration() / 1000);
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                new Handler(Looper.getMainLooper()).postDelayed(this, 10);
            }
        });
        musicService.OnCompleted();
    }

    @Override
    public void prevBtnClicked() {
        if (musicService.isPlaying()) {
            prevBtnSupp();
            musicService.showNotification(R.drawable.ic_pause);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            musicService.start();
        } else {
            prevBtnSupp();
            musicService.showNotification(R.drawable.ic_play_arrow);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play_arrow);
        }
    }

    private void nextThreadBtn() {
        Thread nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(v -> nextBtnClicked());
            }
        };
        nextThread.start();
    }

    private void nextBtnSupp() {
        musicService.stop();
        musicService.release();
        if (MainActivity.shuffleBoolean && !MainActivity.repeatBoolean)
            position = getRandom(listSongs.size() - 1);
        else if (!MainActivity.shuffleBoolean && !MainActivity.repeatBoolean)
            position = ((position + 1) > (listSongs.size() - 1) ? 0 : (position + 1));
        // else position will be the same ...


        uri = Uri.parse(listSongs.get(position).getPath());
        prev_song = listSongs.get(position);
        curr_song = prev_song;
        if (MainActivity.musicFiles.contains(curr_song)) {
            for (MusicFile musicFile : MainActivity.musicFiles)
                if (musicFile == curr_song)
                    musicFile.setColor(0);
                else
                    musicFile.setColor(-1);
            SongsFragment.musicAdapter.updateList(MainActivity.musicFiles);
            if (AlbumDetailsAdapter.albumFiles != null && AlbumDetailsAdapter.albumFiles.contains(curr_song))
                AlbumDetails.albumDetailsAdapter.updateList(AlbumDetailsAdapter.albumFiles);
        }
        musicService.createMediaPlayer(position);
        metaData(uri);
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        seekBar.setMax(musicService.getDuration() / 1000);
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                new Handler(Looper.getMainLooper()).postDelayed(this, 10);
            }
        });
        musicService.OnCompleted();
    }

    @Override
    public void nextBtnClicked() {
        if (musicService.isPlaying()) {
            nextBtnSupp();
            musicService.showNotification(R.drawable.ic_pause);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            musicService.start();
        } else {
            nextBtnSupp();
            musicService.showNotification(R.drawable.ic_play_arrow);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play_arrow);
        }
    }

    private int getRandom(int num) {
        Random random = new Random();
        return random.nextInt(num + 1);
    }

    private void playThreadBtn() {
        Thread playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(v -> playPauseBtnClicked());
            }
        };
        playThread.start();
    }

    @Override
    public void playPauseBtnClicked() {
        if (musicService.isPlaying()) {
            playPauseBtn.setImageResource(R.drawable.ic_play_arrow);
            musicService.showNotification(R.drawable.ic_play_arrow);
            musicService.pause();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(this, 10);
                }
            });
        } else {
            musicService.showNotification(R.drawable.ic_pause);
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            musicService.start();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    new Handler(Looper.getMainLooper()).postDelayed(this, 10);
                }
            });
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        String sender = getIntent().getStringExtra("sender");
        if (sender != null && sender.equals("albumDetails"))
            listSongs = AlbumDetailsAdapter.albumFiles;
        else
            listSongs = MusicAdapter.mFiles;
        curr_song = listSongs != null ? listSongs.get(position) : null;

        if (MusicService.mediaPlayer != null) {
            if (prev_song == curr_song) {
                if (MusicService.mediaPlayer.isPlaying())
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
                seekBar.setMax(MusicService.mediaPlayer.getDuration() / 1000);
                seekBar.setProgress(MusicService.mediaPlayer.getCurrentPosition() / 1000);
                metaData(uri);
                return;
            }
        }
        if (MainActivity.musicFiles != null && MainActivity.musicFiles.contains(curr_song)) {
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
        if (listSongs != null) {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("servicePosition", position);
        startService(intent);
    }


    private void initViews() {
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

    private void metaData(Uri uri) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            if (musicService != null) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(uri.toString());
                int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
                duration_total.setText(formattedTime(durationTotal));
                byte[] art = retriever.getEmbeddedPicture();
                if (art != null) {
                    Glide.with(this)
                            .asBitmap()
                            .load(art)
                            .into(cover_art);
                } else {
                    Glide.with(this)
                            .asBitmap()
                            .load(R.drawable.musicimage)
                            .into(cover_art);
                }
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        musicService.setCallBack(this);
        seekBar.setMax(musicService.getDuration() / 1000);
        metaData(uri);
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        musicService.OnCompleted();
        musicService.showNotification(R.drawable.ic_pause);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }
}