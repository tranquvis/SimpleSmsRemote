package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Test;

/**
 * Created by Andreas Kaltenleitner on 22.1.2018.
 */
public class CommandStartAudioRecordingTest extends CommandTest {

    @Override
    public void testPattern() throws Exception {
        assertThat("start recording").matches();
        assertThat("start audio recording").matches();
        assertThat("begin recording").matches();
        assertThat("record audio").matches();
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("start audio recording").matches().executes();
    }
}