package josephnwanna.com.custommp3player.prefab;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;

import java.util.HashMap;

import josephnwanna.com.custommp3player.interfaces.MP3ActionInterface;
import josephnwanna.com.custommp3player.interfaces.MP3UIUpdateInterface;
import josephnwanna.com.custommp3player.ui.MP3PlayerView;
import josephnwanna.com.custommp3player.interfaces.MP3StateInterface;

/**
 * Prefabricated (Out of the Box) Class that extends MP3PlayerView.
 * This demonstrates how easy it is to extend the Base Class and
 * customize it accordingly.
 *
 * use {@link #setActionsInterface(MP3ActionInterface)} to assign UI buttons to actions.
 * Future versions would use the Command pattern.
 *
 * use {@link #setMp3UIUpdateInterface(MP3UIUpdateInterface, int)} to assign UI updates every milliseconds.
 *
 * use {@link #setStateUpdateInterface(MP3StateInterface)} (MP3UIUpdateInterface, int)} to recieve callbacks on
 * updates to the UI's state.
 *
 * */
public class BasicMP3PlayerPrefab extends MP3PlayerView {

    //Inbuilt Defaults
    private HashMap<Integer,Integer> settings=new HashMap<>();
    private Handler handler=new Handler();
    private int MP3UPDATE_TIME=1000;

    //Interfaces
    private MP3ActionInterface actionInterface;
    private MP3StateInterface stateInterface;
    private MP3UIUpdateInterface mp3UIUpdateInterface;
    private MP3PlayerState playerState=MP3PlayerState.UNDEFINED;

    private String TAG=getClass().getSimpleName();

    //UI Update Handler
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            updateUI();
            if(MP3UPDATE_TIME > 0) {
                handler.postDelayed(runnable, MP3UPDATE_TIME);
            }
        }
    };

    public BasicMP3PlayerPrefab(Context context) {
        super(context);
    }

    public BasicMP3PlayerPrefab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public BasicMP3PlayerPrefab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initialize(Context context) {
        super.initialize(context);
        updateUI();

    }

    public void setReady(boolean isReady){
        playerState = isReady ? MP3PlayerState.READY : MP3PlayerState.UNDEFINED;
    }


    @Override
    public void playOrPause(){

        if(this.actionInterface == null){
            Log.d(TAG,"MP3ActionInterface is null");
            return;
        }
        if(playerState.equals(MP3PlayerState.PLAYING)){
            boolean ok=this.actionInterface.pause(playerState);
            if(!ok){
                return;
            }
            this.playerState=MP3PlayerState.PAUSED;
            announceStateUpdate();
        }else{
            boolean ok=this.actionInterface.play(playerState);
            if(!ok){
                return;
            }
            this.playerState=MP3PlayerState.PLAYING;
            announceStateUpdate();
        }
    }

    @Override
    public void stop() {
        if(this.actionInterface == null){
            Log.d(TAG,"MP3ActionInterface is null");
            return;
        }
        boolean ok=this.actionInterface.stop(playerState);
        if(!ok){
            return;
        }
        this.playerState=MP3PlayerState.STOPPED;
        announceStateUpdate();

    }

    @Override
    public void next() {
        if(this.actionInterface == null){
            Log.d(TAG,"MP3ActionInterface is null");
            return;
        }
        boolean ok=this.actionInterface.next(playerState);
        if(!ok){
            return;
        }
        this.playerState=MP3PlayerState.NEXT;
        announceStateUpdate();
    }

    @Override
    public void prev() {
        if(this.actionInterface == null){
            Log.d(TAG,"MP3ActionInterface is null");
            return;
        }
        boolean ok=this.actionInterface.prev(playerState);
        if(!ok){
            return;
        }
        this.playerState=MP3PlayerState.PREV;
        announceStateUpdate();
    }

    @Override
    public void fastforward() {
        if(this.actionInterface == null){
            Log.d(TAG,"MP3ActionInterface is null");
            return;
        }
        boolean ok=this.actionInterface.fastforward(playerState);
        if(!ok){
            return;
        }
        this.playerState=MP3PlayerState.FAST_FORWARD;
        announceStateUpdate();
    }

    @Override
    public void rewind() {
        if(this.actionInterface == null){
            Log.d(TAG,"MP3ActionInterface is null");
            return;
        }
        boolean ok=this.actionInterface.rewind(playerState);
        if(!ok){
            return;
        }
        this.playerState=MP3PlayerState.REWIND;
        announceStateUpdate();
    }

    @Override
    public void updateSettings(int key, int value) {
        settings.put(key,value);
    }

    @Override
    public HashMap<Integer, Integer> getSettings() {
        return settings;
    }


    public void updateControls(){
        observer.setPlayerState(playerState);
    }

    public void announceStateUpdate(){

        updateUI();
        if(this.stateInterface != null || playerState.equals(MP3PlayerState.UNDEFINED)){
           this.stateInterface.onState(playerState);
        }

    }

    public void setStateUpdateInterface(MP3StateInterface stateUpdateInterface){
        this.stateInterface=stateUpdateInterface;
    }

    /**
     *
     * @param actionsInterface An MP3ActionInterface that maps a set of Commands necessary for the View to change it's state
     */
    public void setActionsInterface(MP3ActionInterface actionsInterface){
        this.actionInterface=actionsInterface;
    }

    /**
     *
     * @param uiUpdater An MP3UIUpdateInterface interface
     * @param timeDelay The value in milliseconds that it'll take to refresh the View
     */
    public void setMp3UIUpdateInterface(MP3UIUpdateInterface uiUpdater, int timeDelay){
        MP3UPDATE_TIME = timeDelay;
        if(MP3UPDATE_TIME < 1000){
            MP3UPDATE_TIME = 1000;
        }
        setMp3UIUpdateInterface(uiUpdater);
    }

    /**
     * Sets an Interface that defines the changes to the View
     * @param uiUpdater . An MP3UIUpdateInterface interface
     */
    public void setMp3UIUpdateInterface(MP3UIUpdateInterface uiUpdater){
        mp3UIUpdateInterface = uiUpdater;
        handler.postDelayed(runnable,MP3UPDATE_TIME);
    }


    public void updateUI(){
        if(mp3UIUpdateInterface !=null) {
            mp3UIUpdateInterface.updateUI(this.observer);
        }
        updateControls();
    }

    public void destroy(){
        handler.removeCallbacks(runnable);
        runnable=null;
        handler=null;
        actionInterface=null;
        stateInterface=null;
        mp3UIUpdateInterface=null;
    }

    public enum MP3PlayerState{
        UNDEFINED, READY,PLAYING, PAUSED, STOPPED, NEXT, PREV, FAST_FORWARD, REWIND;
    }

}
