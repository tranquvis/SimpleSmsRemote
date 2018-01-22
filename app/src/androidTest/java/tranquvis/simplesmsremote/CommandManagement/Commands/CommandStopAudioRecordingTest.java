package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Test;

/**
 * Created by Andreas Kaltenleitner on 22.1.2018.
 */
public class CommandStopAudioRecordingTest extends CommandTest {

    @Override
    public void testPattern() throws Exception {
        assertThat("stop recording").matches();
        assertThat("stop audio recording").matches();
        assertThat("end recording").matches();
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("stop audio recording").matches().executes();
    }
}