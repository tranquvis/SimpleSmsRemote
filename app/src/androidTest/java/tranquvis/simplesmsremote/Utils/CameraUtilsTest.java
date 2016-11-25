package tranquvis.simplesmsremote.Utils;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.Utils.Device.CameraUtils;

import static tranquvis.simplesmsremote.Utils.Device.CameraUtils.LensFacing;
import static tranquvis.simplesmsremote.Utils.Device.CameraUtils.MyCameraInfo;
import static tranquvis.simplesmsremote.Utils.Device.CameraUtils.TakePicture;

/**
 * Created by Andi on 15.10.2016.
 */
public class CameraUtilsTest extends AppContextTest {
    @Test
    public void takePicture() throws Exception {
        MyCameraInfo cameraInfo = CameraUtils.GetCamera(appContext, null, LensFacing.BACK);
        TakePicture(appContext, cameraInfo, cameraInfo.getDefaultCaptureSettings());
    }
}