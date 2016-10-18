package tranquvis.simplesmsremote.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andreas Kaltenleitner on 18.10.2016.
 */

public class CameraModuleSettingsData extends ModuleSettingsData
{
    private List<String> cameraIdList;
    private List<CaptureSettings> captureSettingsList;

    public CameraModuleSettingsData(List<String> cameraIdList, List<CaptureSettings> captureSettingsList)
    {
        this.cameraIdList = cameraIdList;
        this.captureSettingsList = captureSettingsList;
    }

    public CaptureSettings getCaptureSettingsByCameraId(String id)
    {
        int i = 0;
        for (String cameraId : cameraIdList)
        {
            if(cameraId.equals(id))
                return captureSettingsList.get(i);
            i++;
        }

        return null;
    }
}
