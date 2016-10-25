package tranquvis.simplesmsremote.Data;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.Utils.CameraUtils;

/**
 * Created by Andreas Kaltenleitner on 18.10.2016.
 */

public class CameraModuleSettingsData extends ModuleSettingsData
{
    private String defaultCameraId = null;
    private List<CaptureSettings> captureSettingsList = new ArrayList<>();

    public CameraModuleSettingsData()
    {
    }

    public static CameraModuleSettingsData CreateDefaultSettings(
            List<CameraUtils.MyCameraInfo> cameraInfoList)
    {
        CameraModuleSettingsData moduleSettings = new CameraModuleSettingsData();
        for (CameraUtils.MyCameraInfo camera : cameraInfoList)
        {
            moduleSettings.captureSettingsList.add(camera.getDefaultCaptureSettings());
            if(camera.getLensFacing() == CameraUtils.LensFacing.BACK)
                moduleSettings.defaultCameraId = camera.getId();
        }

        if(moduleSettings.defaultCameraId == null && !moduleSettings.captureSettingsList.isEmpty())
        {
            moduleSettings.defaultCameraId = moduleSettings.captureSettingsList.get(0)
                    .getCameraId();
        }

        return moduleSettings;
    }

    public CaptureSettings getCaptureSettingsByCameraId(String id)
    {
        for (CaptureSettings settings : captureSettingsList)
        {
            if(settings.getCameraId().equals(id))
                return settings;
        }

        return null;
    }

    public String getDefaultCameraId()
    {
        return defaultCameraId;
    }

    public void setDefaultCameraId(String defaultCameraId)
    {
        this.defaultCameraId = defaultCameraId;
    }

    public List<CaptureSettings> getCaptureSettingsList()
    {
        return captureSettingsList;
    }
}
