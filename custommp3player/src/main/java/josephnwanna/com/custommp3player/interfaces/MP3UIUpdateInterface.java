package josephnwanna.com.custommp3player.interfaces;

import josephnwanna.com.custommp3player.observer.MP3PlayerDataObserver;
import josephnwanna.com.custommp3player.ui.MP3PlayerView;
import josephnwanna.com.custommp3player.viewmodel.MP3PlayerViewModel;

public interface MP3UIUpdateInterface {

    public void updateUI(MP3PlayerDataObserver mp3playerViewmodel);

}
