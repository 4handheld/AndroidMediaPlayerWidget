package josephnwanna.com.custommp3player.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.HashMap;

import josephnwanna.com.custommp3player.databinding.Mp3playerLayoutBinding;
import josephnwanna.com.custommp3player.observer.MP3PlayerDataObserver;

public abstract class MP3PlayerView extends LinearLayout {
    protected Context context;
//    protected String timeElasped, totalTime, title; //caching the values for quick lookup or pre processing
    public MP3PlayerDataObserver observer;
    protected HashMap<Integer,Integer> settings;
    protected Mp3playerLayoutBinding binding;

    public MP3PlayerView(Context context) {
        super(context);
        initialize(context);
    }

    public MP3PlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public MP3PlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    protected void initialize(Context context){
            this.context=context;
            binding = Mp3playerLayoutBinding.inflate(LayoutInflater.from(context),this,true);
            observer=new MP3PlayerDataObserver();
            binding.setMp3view(this);
    }


    public String getTitle() {
        return observer.getTrackTitle();
    }

    public void setTitle(String title) {
        observer.setTrackTitle(title);
    }

    public void setAlbumTitle(String title) {
        observer.setAlbumTitle(title);
    }

    public void setArtistName(String title) {
        observer.setArtistName(title);
    }

    public void setAudioTitle(String title) {
        observer.setAudioTitle(title);
    }

    public String getTotalTime() {
        return observer.getTotalTime();
    }

    public void setTotalTime(String totalTime) {
        observer.setTotalTime(totalTime);
//        binding.totalTime.setText(totalTime);
    }

    public String getTimeElasped() {
        return observer.getTimeElapsed();
    }

    public void setTimeElasped(String timeElasped) {
        observer.setTimeElapsed(timeElasped);
//        binding.elapsedTime.setText(timeElasped);
    }

    public abstract void playOrPause();

    public abstract void stop();

    public abstract void next();

    public abstract void prev();

    public abstract void fastforward();

    public abstract void rewind();

    public abstract void updateSettings(int key,int value);

    public abstract HashMap<Integer,Integer> getSettings();

}
