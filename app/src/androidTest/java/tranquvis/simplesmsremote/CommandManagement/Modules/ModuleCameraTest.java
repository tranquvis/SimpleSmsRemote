package tranquvis.simplesmsremote.CommandManagement.Modules;

import org.junit.Test;

import tranquvis.simplesmsremote.CommandManagement.Commands.Command;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePicture;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePictureTest;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePictureWithOptions;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePictureWithOptionsTest;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTest;

import static org.junit.Assert.*;

/**
 * Created by Kaltenleitner Andreas on 29.10.2016.
 */
public class ModuleCameraTest extends ModuleTest {

    @Test
    public void testTakePhoto() throws Exception
    {
        CommandTakePictureTest unitTest = getUnitTestFrom(getCommand(CommandTakePicture.class));
        unitTest.testExecution();
    }

    @Test
    public void testTakePhotoWithOptions() throws Exception
    {
        CommandTakePictureWithOptionsTest unitTest =
                getUnitTestFrom(getCommand(CommandTakePictureWithOptions.class));
        unitTest.testExecutionWithCustomOptions("back lens and flash and autofocus");
    }
}