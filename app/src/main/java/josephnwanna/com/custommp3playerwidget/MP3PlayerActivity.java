package josephnwanna.com.custommp3playerwidget;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import josephnwanna.com.custommp3player.interfaces.MP3ActionInterface;
import josephnwanna.com.custommp3player.interfaces.MP3StateInterface;
import josephnwanna.com.custommp3player.interfaces.MP3UIUpdateInterface;
import josephnwanna.com.custommp3player.observer.MP3PlayerDataObserver;
import josephnwanna.com.custommp3player.prefab.BasicMP3PlayerPrefab;
import josephnwanna.com.custommp3player.service.Backgroundservice;
import josephnwanna.com.custommp3player.ui.MP3PlayerView;
import josephnwanna.com.custommp3player.viewmodel.MP3PlayerViewModel;
import josephnwanna.com.custommp3playerwidget.databinding.ActivityMp3PlayerBinding;

public class MP3PlayerActivity extends AppCompatActivity{

    //androidx mediaplayer
    MP3PlayerViewModel viewModel;
    BasicMP3PlayerPrefab prefab;
    ActivityMp3PlayerBinding activityMp3PlayerBinding;
    MediaPlayer mediaPlayer;
    MyService playerService;


    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.CustomBinder binder=(MyService.CustomBinder) service;
            playerService=binder.bindToService();
            mediaPlayer=playerService.getMediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    prefab.setReady(true);
                    prefab.setTitle(getString(R.string.strint_title)+" : "+playerService.getTitle());
                    prefab.setAlbumTitle(getString(R.string.string_album_title)+" : "+playerService.getAlbum()+"");
                    prefab.setArtistName(getString(R.string.string_artist)+" : "+playerService.getAlbumArtist());
                    prefab.setAudioTitle(getString(R.string.string_genre)+" : "+playerService.getGenre());
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    prefab.stop();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    prefab.setTitle("Error Loading File");
                    return false;
                }
            });

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mediaPlayer=null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel=(new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).create(MP3PlayerViewModel.class);
        activityMp3PlayerBinding= DataBindingUtil.setContentView(this,R.layout.activity_mp3_player);
        prefab=activityMp3PlayerBinding.myMp3Prefab;

        setUpActionInterface();
        setUpStateInterface();
        setUpUIUpdateInterface();
        prefab.setTitle("App Demo");
        viewModel.setMP3PlayerView(prefab);
        activityMp3PlayerBinding.fileSelectButton.setOnClickListener(e->requestForFile());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(this,MyService.class);
        this.bindService(intent,connection,Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        prefab.destroy();
        prefab=null;
        this.unbindService(connection);
        playerService=null;
        mediaPlayer=null;
    }

    public void setUpActionInterface(){
        prefab.setActionsInterface(new MP3ActionInterface() {
            @Override
            public boolean play(BasicMP3PlayerPrefab.MP3PlayerState activeState) {
                if(activeState.equals(BasicMP3PlayerPrefab.MP3PlayerState.PLAYING)){
                    return false;
                }
                playerService.play();
                return true;
            }

            @Override
            public boolean pause(BasicMP3PlayerPrefab.MP3PlayerState activeState) {
                playerService.pause();
                return true;
            }

            @Override
            public boolean stop(BasicMP3PlayerPrefab.MP3PlayerState activeState) {
                if( !(activeState.equals(BasicMP3PlayerPrefab.MP3PlayerState.PLAYING)
                        || activeState.equals(BasicMP3PlayerPrefab.MP3PlayerState.PAUSED))){
                    return false;
                }
                playerService.stop();
                return true;
            }

            @Override
            public boolean next(BasicMP3PlayerPrefab.MP3PlayerState activeState) {
                return false;
            }

            @Override
            public boolean prev(BasicMP3PlayerPrefab.MP3PlayerState activeState) {
                return false;
            }

            @Override
            public boolean fastforward(BasicMP3PlayerPrefab.MP3PlayerState activeState) {
                return true;
            }

            @Override
            public boolean rewind(BasicMP3PlayerPrefab.MP3PlayerState activeState) {
                return true;
            }
        });
    }

    public void setUpStateInterface(){
        prefab.setStateUpdateInterface(new MP3StateInterface() {
            @Override
            public void onState(BasicMP3PlayerPrefab.MP3PlayerState state) {
                Toast.makeText(MP3PlayerActivity.this,state.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUpUIUpdateInterface(){

        prefab.setMp3UIUpdateInterface(new MP3UIUpdateInterface() {
            @Override
            public void updateUI(MP3PlayerDataObserver observer) {
                if(mediaPlayer == null){
                    return;
                }
                try {
                    observer.setTotalTime(observer.parseTime(mediaPlayer.getDuration()));
                    observer.setTimeElapsed(observer.parseTime(mediaPlayer.getCurrentPosition()));
                }catch (Exception e){
                    Log.wtf("8)8)8)",e.getMessage());
                }
            }
        },1000);
    }


    //Internal Methods
    String[] permissions={"",""};
    static final int REQUEST_CODE_MP3=1;
    //request for file
    public void requestForFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/mpeg");
        startActivityForResult(intent, REQUEST_CODE_MP3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("onActivityResult",requestCode+"");
        Log.i("onActivityResult",resultCode+"");
//        Log.i("onActivityResult",data.getData().toString());

        if (requestCode == REQUEST_CODE_MP3 && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                setUri(uri);
                // Perform operations on the document using its URI.
            }
        }
    }

    public void setUri(Uri uri){
        try {
            prefab.setReady(false);
            playerService.setUri(uri);
            Log.i("setUri","OK");
        }catch (Exception e){
            Log.i("setUri",e.getMessage()+"");
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(String message){

    }
}
