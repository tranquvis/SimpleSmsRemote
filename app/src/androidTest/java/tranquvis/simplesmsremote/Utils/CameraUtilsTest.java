package tranquvis.simplesmsremote.Utils;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;

import static org.junit.Assert.*;

/**
 * Created by Andi on 15.10.2016.
 */
public class CameraUtilsTest extends AppContextTest {
    @Test
    public void takePhoto() throws Exception {
        CameraUtils.MyCameraInfo cameraInfo = CameraUtils.GetBackCamera(appContext);
        CameraUtils.TakePhoto(appContext, cameraInfo, cameraInfo.getDefaultCaptureSettings());
    }
}