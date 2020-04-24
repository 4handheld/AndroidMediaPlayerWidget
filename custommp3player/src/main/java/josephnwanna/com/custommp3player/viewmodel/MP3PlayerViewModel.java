package josephnwanna.com.custommp3player.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import josephnwanna.com.custommp3player.observer.MP3PlayerDataObserver;
import josephnwanna.com.custommp3player.prefab.BasicMP3PlayerPrefab;
import josephnwanna.com.custommp3player.ui.MP3PlayerView;

/**
* This is a custom ViewModel provided out of the box to assist
* the developer with retaining variables about the current media player
* */
public class MP3PlayerViewModel extends AndroidViewModel {

    private MP3PlayerDataObserver prefab;

    public MP3PlayerViewModel(@NonNull Application application) {
        super(application);
    }

    public void setMP3PlayerView(@NonNull BasicMP3PlayerPrefab mp3PlayerView){
        prefab=mp3PlayerView.observer;
    }

}
