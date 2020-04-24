package josephnwanna.com.custommp3playerwidget;

import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.io.FileDescriptor;

public class MyService extends Service {
    private String TAG=getClass().getSimpleName();
    private MediaPlayer player;
    MediaMetadataRetriever retriever;
    private Uri file_source;
    private IBinder serviceBinder;
    int play_from=0;
    private String title,album,albumArtist,bitRate,cdTrackNumber,genre;
    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"Bound");
        if(serviceBinder == null){
            serviceBinder=new CustomBinder();
        }
        startService(new Intent(this,MyService.class));
        return serviceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"started");
        if(player == null){
            player=MediaPlayer.create(this,R.raw.lesson);
            retriever= new MediaMetadataRetriever();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"destroyed");
        super.onDestroy();
        stop();
        player.release();
        player=null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG,"Unbound");
        return super.onUnbind(intent);
    }

    public void setUri(Uri file_uri) throws Exception{
        player.stop();
        try {
            player.reset();
            player.setDataSource(this, file_uri);
        }catch (Exception e){
            Log.e("setdatasource",e.getMessage()+"");
            throw e;
        }
        retriever.setDataSource(this,file_uri);
        try {
            player.prepare();
        }catch (Exception e){
            Log.e("prepare",e.getMessage()+"");
            throw  e;
        }
        title=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        album=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        albumArtist=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
        bitRate=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        cdTrackNumber=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
        genre=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        Log.i("TITLE",title);
        Log.i("ALBUM",album);
        Log.i("ALBUM ARTIST",albumArtist);
        Log.i("BITRATE",bitRate);
        Log.i("CD TRACK NUMBER",cdTrackNumber);
        Log.i("GENRE",genre);
        file_source=file_uri;
    }

    public void play(){
        player.seekTo(play_from);
        player.start();
    }

    public void pause(){
        player.pause();
        play_from=player.getCurrentPosition();
    }

    public void stop(){
        play_from=0;
        player.seekTo(play_from);
        player.pause();
    }

    public MediaPlayer getMediaPlayer(){
        return player;
    }

    public class CustomBinder extends Binder {
        public MyService bindToService(){
            return MyService.this;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getBitRate() {
        return bitRate;
    }

    public void setBitRate(String bitRate) {
        this.bitRate = bitRate;
    }

    public String getCdTrackNumber() {
        return cdTrackNumber;
    }

    public void setCdTrackNumber(String cdTrackNumber) {
        this.cdTrackNumber = cdTrackNumber;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
