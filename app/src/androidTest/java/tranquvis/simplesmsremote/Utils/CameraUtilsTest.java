package tranquvis.simplesmsremote.Utils;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import static tranquvis.simplesmsremote.Utils.CameraUtils.*;

import static org.junit.Assert.*;

/**
 * Created by Andi on 15.10.2016.
 */
public class CameraUtilsTest extends AppContextTest {
    @Test
    public void takePhoto() throws Exception {
        MyCameraInfo cameraInfo = CameraUtils.GetCamera(appContext, null,
                MyCameraInfo.LensFacing.BACK);
        TakePhoto(appContext, cameraInfo, cameraInfo.getDefaultCaptureSettings());
    }
}