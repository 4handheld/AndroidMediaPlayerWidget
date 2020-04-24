package josephnwanna.com.custommp3player;

import org.junit.Test;
import static org.junit.Assert.*;
import org.hamcrest.*;

import josephnwanna.com.custommp3player.observer.MP3PlayerDataObserver;

public class MP3PlayerDataObserverTest {

    @Test
    public void sample_addition(){
        assertEquals(4,2*2);
    }

    @Test
    public void Observer_Converts0Milliseconds_toTimeElaspedString(){
        MP3PlayerDataObserver obs=new MP3PlayerDataObserver();
        String convert_0=obs.parseTime(0);
        assertEquals("00:00:00",convert_0);
    }

    @Test
    public void Observer_Converts10Milliseconds_toTimeElaspedString(){
        MP3PlayerDataObserver obs=new MP3PlayerDataObserver();
        String convert_10=obs.parseTime(10000);
        assertEquals("00:00:10",convert_10);
    }

    @Test
    public void Observer_Converts100Milliseconds_toTimeElaspedString(){
        MP3PlayerDataObserver obs=new MP3PlayerDataObserver();
        String convert_100=obs.parseTime(100000);
        assertEquals("00:01:40",convert_100);
    }

}
