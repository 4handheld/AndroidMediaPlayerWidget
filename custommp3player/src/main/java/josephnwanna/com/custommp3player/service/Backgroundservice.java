package josephnwanna.com.custommp3player.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import josephnwanna.com.custommp3player.MP3PlayerSettings;
import josephnwanna.com.custommp3player.interfaces.MP3StateInterface;

public class Backgroundservice extends MediaBrowserServiceCompat{

    private int FILE_STATE=AUDIOSTATE.STATE_NO_FILE;

    private MediaPlayer mediaPlayer;
    private MP3PlayerSettings settings;
    private int last_play_position;
    private MP3StateInterface mp3StateInterface;
    public final static String PARCELABLE_EXTRA_KEY="file_source";
    private AudioManager audioManager;

    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    public class BackgroundServiceBinder extends Binder {
        public Backgroundservice getService(){
            return Backgroundservice.this;
        }
    }

    private final IBinder serviceBinder=new BackgroundServiceBinder();

    public Backgroundservice() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mediaSession = new MediaSessionCompat(getApplicationContext(),
                Backgroundservice.class.getSimpleName(),
                new ComponentName(getApplicationContext(), MediaButtonReceiver.class),null);
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onCommand(String command, Bundle extras, ResultReceiver cb) {
                super.onCommand(command, extras, cb);
            }

            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                return super.onMediaButtonEvent(mediaButtonEvent);
            }

            @Override
            public void onPrepare() {
                super.onPrepare();
            }

            @Override
            public void onPrepareFromMediaId(String mediaId, Bundle extras) {
                super.onPrepareFromMediaId(mediaId, extras);
            }

            @Override
            public void onPrepareFromSearch(String query, Bundle extras) {
                super.onPrepareFromSearch(query, extras);
            }

            @Override
            public void onPrepareFromUri(Uri uri, Bundle extras) {
                super.onPrepareFromUri(uri, extras);
            }

            @Override
            public void onPlay() {
                super.onPlay();
                mediaPlayer.seekTo(last_play_position);
                mediaPlayer.start();
                mediaSession.setActive(true);
//                startForeground(2,);
            }

            @Override
            public void onPlayFromMediaId(String mediaId, Bundle extras) {
                super.onPlayFromMediaId(mediaId, extras);
            }

            @Override
            public void onPlayFromSearch(String query, Bundle extras) {
                super.onPlayFromSearch(query, extras);
            }

            @Override
            public void onPlayFromUri(Uri uri, Bundle extras) {
                super.onPlayFromUri(uri, extras);
            }

            @Override
            public void onSkipToQueueItem(long id) {
                super.onSkipToQueueItem(id);
            }

            @Override
            public void onPause() {
                super.onPause();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
            }

            @Override
            public void onFastForward() {
                super.onFastForward();
            }

            @Override
            public void onRewind() {
                super.onRewind();
            }

            @Override
            public void onStop() {
                super.onStop();
                mediaSession.setActive(false);
                mediaPlayer.stop();
            }

            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
            }

            @Override
            public void onSetRating(RatingCompat rating) {
                super.onSetRating(rating);
            }

            @Override
            public void onSetRating(RatingCompat rating, Bundle extras) {
                super.onSetRating(rating, extras);
            }

            @Override
            public void onSetCaptioningEnabled(boolean enabled) {
                super.onSetCaptioningEnabled(enabled);
            }

            @Override
            public void onSetRepeatMode(int repeatMode) {
                super.onSetRepeatMode(repeatMode);
            }

            @Override
            public void onSetShuffleMode(int shuffleMode) {
                super.onSetShuffleMode(shuffleMode);
            }

            @Override
            public void onCustomAction(String action, Bundle extras) {
                super.onCustomAction(action, extras);
            }

            @Override
            public void onAddQueueItem(MediaDescriptionCompat description) {
                super.onAddQueueItem(description);
            }

            @Override
            public void onAddQueueItem(MediaDescriptionCompat description, int index) {
                super.onAddQueueItem(description, index);
            }

            @Override
            public void onRemoveQueueItem(MediaDescriptionCompat description) {
                super.onRemoveQueueItem(description);
            }

            @Override
            public void onRemoveQueueItemAt(int index) {
                super.onRemoveQueueItemAt(index);
            }
        });

        setSessionToken(mediaSession.getSessionToken());
        PlaybackStateCompat.Builder playbackstateBuilder = new PlaybackStateCompat.Builder();
       Log.i("token","done");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(Backgroundservice.class.getSimpleName().toString(),"Unbind");
        return super.onUnbind(intent);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaButtonReceiver.handleIntent(mediaSession, intent);
        last_play_position = 0;
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }


    public void setMediaPlayer(@NonNull MediaPlayer mp,@NonNull MP3PlayerSettings settings){
        mediaPlayer=mp;
        this.settings=settings;
    }

    public boolean playOrPause(){
        if(mediaPlayer.isPlaying()){

            if(settings.getPAUSE_FOR_OTHER_AUDIO() == MP3PlayerSettings.VALUE.BACKGROUND.ALLOW && audioManager==null){
             audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                 @Override
                 public void onAudioFocusChange(int focusChange) {
                        switch (focusChange){

                        }
                 }
             },AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
            }

            mediaPlayer.pause();
//            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mMediaPlayer.setVolume(1.0f, 1.0f);
            last_play_position = mediaPlayer.getCurrentPosition();
            FILE_STATE=AUDIOSTATE.STATE_PLAYING;
            return false;
        }else{

            mediaPlayer.seekTo(last_play_position);
            mediaPlayer.start();
            FILE_STATE=AUDIOSTATE.STATE_PAUSED;
            return true;
        }
    }

    public void stop(){
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            last_play_position=0;
            mediaPlayer.seekTo(last_play_position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
            stop();
            mediaPlayer.release();
            mediaPlayer=null;
            audioManager=null;
//            audioManager.abandonAudioFocusRequest(null);
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    private static final class AUDIOSTATE{
        public static final int STATE_NO_FILE=0;
        public static final int STATE_LOADING_FILE=1;
        public static final int STATE_FILE_LOADED=2;
        public static final int STATE_FILE_NOT_LOADED=3;
        public static final int STATE_PAUSED=4;
        public static final int STATE_PLAYING=5;
        public static final int STATE_STOPPED=6;
    }


    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String ClientpackageName, int clientUID, @Nullable Bundle rootHints) {
        return new BrowserRoot("contents",null);
    }

    @Override
    public void onLoadChildren(@NonNull String s, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        List<MediaBrowserCompat.MediaItem> files=new ArrayList<MediaBrowserCompat.MediaItem>();
//        Note: MediaItem objects delivered by the MediaBrowserService should not contain icon bitmaps. Use a Uri instead by calling setIconUri() when you build the MediaDescription for each item.
        result.sendResult(files);
    }



}
