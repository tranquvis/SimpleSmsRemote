package tranquvis.simplesmsremote.CommandManagement.Commands;

import tranquvis.simplesmsremote.CommandManagement.Command;
import tranquvis.simplesmsremote.CommandManagement.CommandTest;
import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;
import tranquvis.simplesmsremote.Data.CaptureSettings;
import tranquvis.simplesmsremote.Utils.Device.CameraUtils;

import static org.junit.Assert.*;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePictureWithOptions.PARAM_AUTOFOCUS;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePictureWithOptions.PARAM_CAMERA;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePictureWithOptions.PARAM_FLASH;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */
public class CommandTakePictureWithOptionsTest extends CommandTest
{

    public CommandTakePictureWithOptionsTest()
    {
        super(Instances.CAMERA.commandTakePictureWithOptions);
    }

    @Override
    protected void testPattern() throws Exception
    {
        // test with single option and whitespace
        assertThat("\n take Picture  with front cam \r").matches()
                .has(PARAM_CAMERA, CameraUtils.LensFacing.FRONT);

        // test with multiple options
        assertThat("take photo with lens 1 and flash and autofocus").matches()
                .has(PARAM_CAMERA, "1")
                .has(PARAM_FLASH, CaptureSettings.FlashlightMode.ON)
                .has(PARAM_AUTOFOCUS, true);

        // test with multiple options of the same type
        assertThat("capture photo with front and cam 1").matches()
                .has(PARAM_CAMERA, "1");
        assertThat("capture photo with front, cam 1").matches()
                .has(PARAM_CAMERA, "1");

        // test various option syntax for flash
        assertThat("\n take picture  with Flashlight auto \r").matches()
                .has(PARAM_FLASH, CaptureSettings.FlashlightMode.AUTO);
        assertThat("take picture with flash")
                .has(PARAM_FLASH, CaptureSettings.FlashlightMode.ON);
        assertThat("take picture with flash enabled")
                .has(PARAM_FLASH, CaptureSettings.FlashlightMode.ON);
        assertThat("take picture with flash on")
                .has(PARAM_FLASH, CaptureSettings.FlashlightMode.ON);
        assertThat("take picture with no flash")
                .has(PARAM_FLASH, CaptureSettings.FlashlightMode.OFF);
        assertThat("take picture with flash disabled")
                .has(PARAM_FLASH, CaptureSettings.FlashlightMode.OFF);
        assertThat("take picture with flash off")
                .has(PARAM_FLASH, CaptureSettings.FlashlightMode.OFF);

        // test various option syntax for camera
        assertThat("\n take picture  with Camera 1 \r").matches()
                .has(PARAM_CAMERA, "1");
        assertThat("take picture with 0").matches()
                .has(PARAM_CAMERA, "0");
        assertThat("take picture with back lens")
                .has(PARAM_CAMERA, CameraUtils.LensFacing.BACK);
        assertThat("take picture with camera external")
                .has(PARAM_CAMERA, CameraUtils.LensFacing.EXTERNAL);

        // test various option syntax for autofocus
        assertThat("\n take picture  with Autofocus \r").matches()
                .has(PARAM_AUTOFOCUS, true);
        assertThat("take picture with autofocus enabled").matches()
                .has(PARAM_AUTOFOCUS, true);
        assertThat("take picture with autofocus on").matches()
                .has(PARAM_AUTOFOCUS, true);
        assertThat("take picture with no autofocus").matches()
                .has(PARAM_AUTOFOCUS, false);
        assertThat("take picture with autofocus disabled").matches()
                .has(PARAM_AUTOFOCUS, true);
        assertThat("take picture with autofocus off").matches()
                .has(PARAM_AUTOFOCUS, false);
    }

    @Override
    protected void testExecution() throws Exception
    {
        assertThat("take picture with camera 1, autofocus, flash").executes();
        assertThat("take picture with front, no autofocus, no flash").executes();
    }
}