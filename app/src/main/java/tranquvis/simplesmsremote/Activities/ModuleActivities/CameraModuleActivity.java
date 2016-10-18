package tranquvis.simplesmsremote.Activities.ModuleActivities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.util.List;

import tranquvis.simplesmsremote.Activities.ConfigureControlModuleActivity;
import tranquvis.simplesmsremote.Adapters.CameraDeviceSpinnerAdapter;
import tranquvis.simplesmsremote.Data.CameraModuleSettingsData;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.CameraUtils;

/**
 * Created by Andreas Kaltenleitner on 17.10.2016.
 */

public class CameraModuleActivity extends ConfigureControlModuleActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(!getControlModule().isEnabled())
            return;

        //load available cameras
        List<CameraUtils.MyCameraInfo> cameras = null;
        try
        {
            cameras = CameraUtils.GetAllCameras(this);
        } catch (Exception e)
        {
            Snackbar.make(getCoordinatorLayout(), R.string.alert_load_cameras_failed,
                    Snackbar.LENGTH_LONG);
        }

        if(cameras != null)
        {
            if(moduleSettings == null)
            {
                List<CameraSet>
                moduleSettings = new CameraModuleSettingsData();
                moduleSettings.
            }
            else
            {

            }

            //show camera settings
            setSettingsContentLayout(R.layout.content_module_settings_camera);

            AppCompatSpinner spinnerCameraDevice =
                    (AppCompatSpinner) findViewById(R.id.spinner_settings_camera_device);
            CameraDeviceSpinnerAdapter cameraDeviceSpinnerAdapter =
                    new CameraDeviceSpinnerAdapter(this, cameras);
            spinnerCameraDevice.setAdapter(cameraDeviceSpinnerAdapter);
        }
    }

    private CameraModuleSettingsData getSettings()
    {
        return (CameraModuleSettingsData) moduleSettings;
    }
}
