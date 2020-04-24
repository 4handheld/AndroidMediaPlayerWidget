package josephnwanna.com.custommp3player;

public class MP3PlayerSettings {

    public int PLAY_IN_BACKGROUND=VALUE.BACKGROUND.ALLOW;

    public int PAUSE_FOR_OTHER_AUDIO=1;

    public int VOLUME_AUDIO_MIN=1;

    public int VOLUME_AUDIO_MAX=1;

    public int VOLUME_INCREASE_QUANTITY=3;

    public int VOLUME_DECREASE_QUANTITY=5;

    public int getPLAY_IN_BACKGROUND() {
        return PLAY_IN_BACKGROUND;
    }

    public void setPLAY_IN_BACKGROUND(int PLAY_IN_BACKGROUND) {
        this.PLAY_IN_BACKGROUND = PLAY_IN_BACKGROUND;
    }

    public int getPAUSE_FOR_OTHER_AUDIO() {
        return PAUSE_FOR_OTHER_AUDIO;
    }

    public void setPAUSE_FOR_OTHER_AUDIO(int PAUSE_FOR_OTHER_AUDIO) {
        this.PAUSE_FOR_OTHER_AUDIO = PAUSE_FOR_OTHER_AUDIO;
    }

    public int getVOLUME_AUDIO_MIN() {
        return VOLUME_AUDIO_MIN;
    }

    public void setVOLUME_AUDIO_MIN(int VOLUME_AUDIO_MIN) {
        this.VOLUME_AUDIO_MIN = VOLUME_AUDIO_MIN;
    }

    public int getVOLUME_AUDIO_MAX() {
        return VOLUME_AUDIO_MAX;
    }

    public void setVOLUME_AUDIO_MAX(int VOLUME_AUDIO_MAX) {
        this.VOLUME_AUDIO_MAX = VOLUME_AUDIO_MAX;
    }

    public int getVOLUME_INCREASE_QUANTITY() {
        return VOLUME_INCREASE_QUANTITY;
    }

    public void setVOLUME_INCREASE_QUANTITY(int VOLUME_INCREASE_QUANTITY) {
        this.VOLUME_INCREASE_QUANTITY = VOLUME_INCREASE_QUANTITY;
    }

    public int getVOLUME_DECREASE_QUANTITY() {
        return VOLUME_DECREASE_QUANTITY;
    }

    public void setVOLUME_DECREASE_QUANTITY(int VOLUME_DECREASE_QUANTITY) {
        this.VOLUME_DECREASE_QUANTITY = VOLUME_DECREASE_QUANTITY;
    }

    public static final class VALUE{
        public static final class BACKGROUND{
            public static final int ALLOW=0;
            public static final int REJECT=1;

        }
    }
}
