package com.example.musicapp.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.example.musicapp.Fragments.SongsFragment;
import com.example.musicapp.Musics.AlbumDetailsAdapter;
import com.example.musicapp.Musics.MusicAdapter;
import com.example.musicapp.Musics.MusicFile;
import com.example.musicapp.NotificationPlaying.ActionPlaying;
import com.example.musicapp.NotificationPlaying.ApplicationClass;
import com.example.musicapp.NotificationPlaying.MusicService;
import com.example.musicapp.NotificationPlaying.NotificationReceiver;
import com.example.musicapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity implements
        ServiceConnection, ActionPlaying {
    public static MusicFile prev_song, curr_song;
    //    public static MediaPlayer mediaPlayer;
    static Uri uri;
    TextView song_name, artist_name, duration_played, duration_total;
    ImageView cover_art, nextBtn, prevBtn, backBtn, shuffleBtn, repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position = -1;
    public static ArrayList<MusicFile> listSongs = new ArrayList<>();
    private Thread playThread, prevThread, nextThread;
    MusicService musicService;
    MediaSessionCompat mediaSessionCompat;

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
        setContentView(R.layout.activity_player);
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(), "My Audio");
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
        PlayerActivity.this.runOnUiThread(new Runnable() {
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
        shuffleBtn.setOnClickListener(v -> {
            if (MainActivity.shuffleBoolean) {
                MainActivity.shuffleBoolean = false;
                shuffleBtn.setImageResource(R.drawable.ic_shuffle_off);
            } else {
                MainActivity.shuffleBoolean = true;
                shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
            }
        });
        repeatBtn.setOnClickListener(v -> {
            if (MainActivity.repeatBoolean) {
                MainActivity.repeatBoolean = false;
                repeatBtn.setImageResource(R.drawable.ic_repeat_off);
            } else {
                MainActivity.repeatBoolean = true;
                repeatBtn.setImageResource(R.drawable.ic_repeat_on);
            }
        });
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
        prevThread = new Thread() {
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
            showNotification(R.drawable.ic_pause);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            musicService.start();
        } else {
            prevBtnSupp();
            showNotification(R.drawable.ic_play_arrow);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play_arrow);
        }
    }

    private void nextThreadBtn() {
        nextThread = new Thread() {
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
            showNotification(R.drawable.ic_pause);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            musicService.start();
        } else {
            nextBtnSupp();
            showNotification(R.drawable.ic_play_arrow);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play_arrow);
        }
    }

    private int getRandom(int num) {
        Random random = new Random();
        return random.nextInt(num + 1);
    }

    private void playThreadBtn() {
        playThread = new Thread() {
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
            showNotification(R.drawable.ic_play_arrow);
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
            showNotification(R.drawable.ic_pause);
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
//        curr_song = listSongs != null ? listSongs.get(position) : null;
//
//        if (prev_song == curr_song) {
////            TODO: fix already playing in same song
//            Intent intent = new Intent(this, MusicService.class);
//            intent.putExtra("servicePosition", position);
//            intent.putExtra("sameSong", true);
//            startService(intent);
////            TODO:
//            if (musicService.isPlaying()) {
////                showNotification(R.drawable.ic_pause);
//                playPauseBtn.setImageResource(R.drawable.ic_pause);
//            }
//            else {
////                showNotification(R.drawable.ic_play_arrow);
//                playPauseBtn.setImageResource(R.drawable.ic_play_arrow);
//            }
//
//            if (MainActivity.shuffleBoolean)
//                shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
//            else
//                shuffleBtn.setImageResource(R.drawable.ic_shuffle_off);
//
//            if (MainActivity.repeatBoolean)
//                repeatBtn.setImageResource(R.drawable.ic_repeat_on);
//            else
//                repeatBtn.setImageResource(R.drawable.ic_repeat_off);
//
//            uri = Uri.parse(listSongs.get(position).getPath());
//            seekBar.setMax(musicService.getDuration() / 1000);
//            seekBar.setProgress(musicService.getCurrentPosition() / 1000);
//            metaData(uri);
//            return;
//        }
//        if (MainActivity.musicFiles != null && MainActivity.musicFiles.contains(curr_song)) {
//            for (MusicFile musicFile : MainActivity.musicFiles)
//                if (musicFile == curr_song)
//                    musicFile.setColor(0);
//                else
//                    musicFile.setColor(-1);
//            SongsFragment.musicAdapter.updateList(MainActivity.musicFiles);
//            if (AlbumDetailsAdapter.albumFiles != null && AlbumDetailsAdapter.albumFiles.contains(curr_song))
//                AlbumDetails.albumDetailsAdapter.updateList(AlbumDetailsAdapter.albumFiles);
//        }
//        prev_song = curr_song;
        if (listSongs != null) {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        showNotification(R.drawable.ic_pause);

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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.getService();
        Toast.makeText(this, "Connected" + musicService, Toast.LENGTH_SHORT).show();
        seekBar.setMax(musicService.getDuration() / 1000);
        metaData(uri);
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        musicService.OnCompleted();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }

    void showNotification(int playPauseBtn) {
        Intent intent = new Intent(this, PlayerActivity.class);
        PendingIntent contentIntent = PendingIntent
                .getActivity(this, 0, intent, 0);

        Intent prevIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ApplicationClass.ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent
                .getBroadcast(this, 0, prevIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, PlayerActivity.class)
                .setAction(ApplicationClass.ACTION_PLAY);
        PendingIntent pausePending = PendingIntent
                .getBroadcast(this, 0, pauseIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ApplicationClass.ACTION_PREVIOUS);
        PendingIntent nextPending = PendingIntent
                .getBroadcast(this, 0, nextIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

//        TODO: listSongs or musicFiles
        byte[] picture = MusicAdapter.getAlbumArt(listSongs.get(position).getPath());
        Bitmap thumb;
        if (picture != null) {
            thumb = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        } else {
            thumb = BitmapFactory.decodeResource(getResources(), R.drawable.musicimage);
        }
        Notification notification = new NotificationCompat.Builder(this,
                ApplicationClass.CHANNEL_ID_2)
                .setSmallIcon(playPauseBtn)
                .setLargeIcon(thumb)
                .setContentTitle(listSongs.get(position).getTitle())
                .setContentText(listSongs.get(position).getArtist())
                .addAction(R.drawable.ic_skip_previous, "Previous", prevPending)
                .addAction(playPauseBtn, "Pause", pausePending)
                .addAction(R.drawable.ic_skip_next, "Next", nextPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}