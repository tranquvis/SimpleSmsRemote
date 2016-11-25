package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Before;
import org.junit.Test;

import tranquvis.simplesmsremote.Data.CaptureSettings;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Utils.Device.CameraUtils;

import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePicture.PARAM_OPTIONS_WRAPPER;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePicture.PARAM_OPTION_AUTOFOCUS;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePicture.PARAM_OPTION_CAMERA;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePicture.PARAM_OPTION_FLASH;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */
public class CommandTakePictureTest extends CommandTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        DataManager.LoadUserData(appContext);
    }

    @Override
    @Test
    public void testPattern() throws Exception {
        // test with single option and whitespace
        assertThat("\n take Picture  with front cam \r").matches()
                .has(PARAM_OPTION_CAMERA, CameraUtils.LensFacing.FRONT);

        // test simple take picture
        assertThat("take picture").matches()
                .hasNot(PARAM_OPTIONS_WRAPPER);

        // test with multiple options
        assertThat("take photo with lens 1 and flash and autofocus").matches()
                .has(PARAM_OPTION_CAMERA, "1")
                .has(PARAM_OPTION_FLASH, CaptureSettings.FlashlightMode.ON)
                .has(PARAM_OPTION_AUTOFOCUS, true);

        // test with multiple options of the same type
        assertThat("capture photo with front and cam 1").matches()
                .has(PARAM_OPTION_CAMERA, "1");
        assertThat("capture photo with front, cam 1").matches()
                .has(PARAM_OPTION_CAMERA, "1");

        // test various option syntax for flash
        assertThat("\n take picture  with Flashlight auto \r").matches()
                .has(PARAM_OPTION_FLASH, CaptureSettings.FlashlightMode.AUTO);
        assertThat("take picture with flash").matches()
                .has(PARAM_OPTION_FLASH, CaptureSettings.FlashlightMode.ON);
        assertThat("take picture with flash enabled").matches()
                .has(PARAM_OPTION_FLASH, CaptureSettings.FlashlightMode.ON);
        assertThat("take picture with flash on").matches()
                .has(PARAM_OPTION_FLASH, CaptureSettings.FlashlightMode.ON);
        assertThat("take picture with no flash").matches()
                .has(PARAM_OPTION_FLASH, CaptureSettings.FlashlightMode.OFF);
        assertThat("take picture with flash disabled").matches()
                .has(PARAM_OPTION_FLASH, CaptureSettings.FlashlightMode.OFF);
        assertThat("take picture with flash off").matches()
                .has(PARAM_OPTION_FLASH, CaptureSettings.FlashlightMode.OFF);

        // test various option syntax for camera
        assertThat("\n take picture  with Camera 1 \r").matches()
                .has(PARAM_OPTION_CAMERA, "1");
        assertThat("take picture with 0").matches()
                .has(PARAM_OPTION_CAMERA, "0");
        assertThat("take picture with back lens").matches()
                .has(PARAM_OPTION_CAMERA, CameraUtils.LensFacing.BACK);
        assertThat("take picture with camera external").matches()
                .has(PARAM_OPTION_CAMERA, CameraUtils.LensFacing.EXTERNAL);

        // test various option syntax for autofocus
        assertThat("\n take picture  with Autofocus \r").matches()
                .has(PARAM_OPTION_AUTOFOCUS, true);
        assertThat("take picture with autofocus enabled").matches()
                .has(PARAM_OPTION_AUTOFOCUS, true);
        assertThat("take picture with autofocus on").matches()
                .has(PARAM_OPTION_AUTOFOCUS, true);
        assertThat("take picture with no autofocus").matches()
                .has(PARAM_OPTION_AUTOFOCUS, false);
        assertThat("take picture with autofocus disabled").matches()
                .has(PARAM_OPTION_AUTOFOCUS, false);
        assertThat("take picture with autofocus off").matches()
                .has(PARAM_OPTION_AUTOFOCUS, false);
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("take picture").matches().executes();
        assertThat("take picture with camera 1, autofocus, flash").matches().executes();
        assertThat("take picture with front, no autofocus, no flash").matches().executes();
    }

    public void testCustomExecutionWith(String options) throws Exception {
        String input = "take picture";
        if (options != null)
            input += " with " + options;

        assertThat(input).matches().executes();
    }
}