package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.support.test.filters.LargeTest;

import org.junit.Test;

import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePicture;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePictureTest;

/**
 * Created by Kaltenleitner Andreas on 29.10.2016.
 */
@LargeTest
public class ModuleCameraTest extends ModuleTest {

    @Test
    public void testTakePhoto() throws Exception
    {
        CommandTakePictureTest unitTest =
                getUnitTestFrom(getCommand(CommandTakePicture.class));
        unitTest.testCustomExecutionWith(null);
    }

    @Test
    public void testTakePhotoWithOptions() throws Exception
    {
        CommandTakePictureTest unitTest =
                getUnitTestFrom(getCommand(CommandTakePicture.class));
        unitTest.testCustomExecutionWith("back lens and autofocus");
    }
}