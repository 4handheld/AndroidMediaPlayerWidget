package josephnwanna.com.custommp3player.observer;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;

import java.util.concurrent.TimeUnit;

import josephnwanna.com.custommp3player.BR;
import josephnwanna.com.custommp3player.prefab.BasicMP3PlayerPrefab;

public class MP3PlayerDataObserver extends BaseObservable {

    private String audioTitle;
    private String timeElapsed;
    private String totalTime;
    private String albumTitle;
    private String artistName;
    private String trackTitle;

    private BasicMP3PlayerPrefab.MP3PlayerState playerState;

    @Bindable
    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
        notifyPropertyChanged(BR.audioTitle);
    }

    @Bindable
    public String getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(String timeElapsed) {
        this.timeElapsed = timeElapsed;
        notifyPropertyChanged(BR.timeElapsed);
    }

    @Bindable
    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
        notifyPropertyChanged(BR.totalTime);
    }

    @Bindable
    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
        notifyPropertyChanged(BR.albumTitle);
    }

    @Bindable
    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
        notifyPropertyChanged(BR.artistName);
    }

    @Bindable
    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
        notifyPropertyChanged(BR.trackTitle);
    }

    @Bindable
    public BasicMP3PlayerPrefab.MP3PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(BasicMP3PlayerPrefab.MP3PlayerState playerState) {
        this.playerState = playerState;
        notifyPropertyChanged(BR.playerState);
    }

    public String parseTime(double millis){
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours((long)millis),
                TimeUnit.MILLISECONDS.toMinutes((long)millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long)millis)),
                TimeUnit.MILLISECONDS.toSeconds((long)millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)millis)));
    }


}
