package com.example.musicapp.NotificationPlaying;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.musicapp.Activities.PlayerActivity;
import com.example.musicapp.Musics.MusicFile;

import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<MusicFile> musicFiles = new ArrayList<>();
    Uri uri;
    int position = -1;
    ActionPlaying actionPlaying;

    @Override
    public void onCreate() {
        super.onCreate();
        musicFiles = PlayerActivity.listSongs;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind", "Method");
        return mBinder;
    }

    public void pause() {
        mediaPlayer.pause();
    }


    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("servicePosition", -1);
        String actionName = intent.getStringExtra("ActionName");
        int sameSong = intent.getIntExtra("sameSong", 0);
        if (myPosition != -1 && sameSong == 0)
            playMedia(myPosition);
        if (actionName != null) {
            switch (actionName) {
                case "playPause":
                    Toast.makeText(this,
                            "Pause", Toast.LENGTH_LONG).show();
                    if (actionPlaying != null) {
                        Log.e("Inside", "ActionPlayPause");
                        actionPlaying.playPauseBtnClicked();
                    }
                    break;
                case "next":
                    Toast.makeText(this,
                            "Next", Toast.LENGTH_LONG).show();
                    if (actionPlaying != null) {
                        Log.e("Inside", "ActionNext");
                        actionPlaying.nextBtnClicked();
                    }
                    break;
                case "previous":
                    Toast.makeText(this,
                            "Previous", Toast.LENGTH_LONG).show();
                    if (actionPlaying != null) {
                        Log.e("Inside", "ActionPrevious");
                        actionPlaying.prevBtnClicked();
                    }
                    break;

            }
        }
        return START_STICKY;
    }

    private void playMedia(int StartPosition) {
        musicFiles = PlayerActivity.listSongs;
        position = StartPosition;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musicFiles != null) {
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }

    public void start() {
        mediaPlayer.start();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void release() {
        mediaPlayer.release();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void createMediaPlayer(int position) {
        uri = musicFiles.get(position).getUri();
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }

    public void OnCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (actionPlaying != null)
            actionPlaying.nextBtnClicked();
        createMediaPlayer(position);
        start();
        OnCompleted();
    }

    public void setCallBack(ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;
    }
}
