package josephnwanna.com.custommp3player.interfaces;

import josephnwanna.com.custommp3player.prefab.BasicMP3PlayerPrefab;

public  interface MP3ActionInterface {

    public boolean play(BasicMP3PlayerPrefab.MP3PlayerState activeState);

    public boolean pause(BasicMP3PlayerPrefab.MP3PlayerState activeState);

    public boolean stop(BasicMP3PlayerPrefab.MP3PlayerState activeState);

    public boolean next(BasicMP3PlayerPrefab.MP3PlayerState activeState);

    public boolean prev(BasicMP3PlayerPrefab.MP3PlayerState activeState);

    public boolean fastforward(BasicMP3PlayerPrefab.MP3PlayerState activeState);

    public boolean rewind(BasicMP3PlayerPrefab.MP3PlayerState activeState);

}
