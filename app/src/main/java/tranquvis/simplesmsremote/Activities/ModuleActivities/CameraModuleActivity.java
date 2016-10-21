package tranquvis.simplesmsremote.Activities.ModuleActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tranquvis.directorypicker.Dialogs.LocalFolderBrowserDialog;
import tranquvis.directorypicker.Interfaces.LocalFolderBrowserDialogListener;

import tranquvis.simplesmsremote.Activities.ConfigureControlModuleActivity;
import tranquvis.simplesmsremote.Adapters.CameraDeviceSpinnerAdapter;
import tranquvis.simplesmsremote.Data.CameraModuleSettingsData;
import tranquvis.simplesmsremote.Data.CaptureSettings;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.CameraUtils;

/**
 * Created by Andreas Kaltenleitner on 17.10.2016.
 */

public class CameraModuleActivity extends ConfigureControlModuleActivity
{
    private static final int REQUEST_CODE_OUTPUT_PATH_DIR_CHOOSER = 0;
    private CaptureSettings selectedCaptureSettings = null;

    private ViewGroup layoutCameraSettingsContent;
    private SwitchCompat switchDefaultCamera;
    private AppCompatSpinner spinnerFlash;
    private TextView textViewImageOutputPath;

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

        if(cameras == null)
        {
            Snackbar.make(getCoordinatorLayout(), R.string.alert_no_cameras_found,
                    Snackbar.LENGTH_INDEFINITE);
        }
        else
        {
            if(moduleSettings == null)
            {
                //create default settings if not available
                moduleSettings = CameraModuleSettingsData.CreateDefaultSettings(cameras);
            }
            else
            {
                //if settings available, add missing cameras and remove unavailable
                List<CaptureSettings> captureSettingsList = new ArrayList<>();
                List<String> cameraIdList = new ArrayList<>();
                for (CameraUtils.MyCameraInfo camera : cameras)
                {
                    cameraIdList.add(camera.getId());
                    if(getSettings().getCaptureSettingsByCameraId(camera.getId()) == null)
                    {
                        captureSettingsList.add(camera.getDefaultCaptureSettings());
                    }
                }
                if(cameras.size() != captureSettingsList.size())
                {
                    for (CaptureSettings settings : getSettings().getCaptureSettingsList())
                    {
                        if(cameraIdList.contains(settings.getCameraId()))
                        {
                            captureSettingsList.add(settings);
                        }
                    }
                }

                getSettings().getCaptureSettingsList().clear();
                getSettings().getCaptureSettingsList().addAll(captureSettingsList);
            }

            //show camera settings
            setSettingsContentLayout(R.layout.content_module_settings_camera);

            AppCompatSpinner spinnerCameraDevice =
                    (AppCompatSpinner) findViewById(R.id.spinner_settings_camera_device);
            CameraDeviceSpinnerAdapter cameraDeviceSpinnerAdapter =
                    new CameraDeviceSpinnerAdapter(this, cameras);
            spinnerCameraDevice.setAdapter(cameraDeviceSpinnerAdapter);
            spinnerCameraDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                {
                    selectedCaptureSettings = getSettings().getCaptureSettingsList().get(i);
                    loadSelectedCameraSettings();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView)
                {
                    selectedCaptureSettings = null;
                    loadSelectedCameraSettings();
                }
            });

            switchDefaultCamera = (SwitchCompat) findViewById(R.id.switch_settings_is_default);
            switchDefaultCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
                {
                    if(checked)
                    {
                        getSettings().setDefaultCameraId(selectedCaptureSettings.getCameraId());
                    }
                    else if(getSettings().getDefaultCameraId() != null && getSettings()
                            .getDefaultCameraId().equals(selectedCaptureSettings.getCameraId()))
                    {
                        getSettings().setDefaultCameraId(null);
                    }
                }
            });

            layoutCameraSettingsContent =
                    (ViewGroup) findViewById(R.id.layout_camera_settings_content);

            spinnerFlash = (AppCompatSpinner) findViewById(R.id.spinner_settings_camera_flash);
            spinnerFlash.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                {
                    // make sure that enum and string array res have same order
                    selectedCaptureSettings.setFlashlight(
                            CaptureSettings.FlashlightMode.values()[i]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView)
                {

                }
            });

            textViewImageOutputPath =
                    (TextView) findViewById(R.id.textView_settings_capture_output_path);
            findViewById(R.id.layout_settings_capture_output_path).setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    File folder = selectedCaptureSettings.getOutputPath() != null
                            ? new File(selectedCaptureSettings.getOutputPath()) : null;
                    LocalFolderBrowserDialog dialog = new LocalFolderBrowserDialog(
                            CameraModuleActivity.this, folder);
                    dialog.setFolderBrowserDialogListener(new LocalFolderBrowserDialogListener() {
                        @Override
                        public void onFolderSelected(File folder) {
                            if(selectedCaptureSettings == null)
                                return;
                            selectedCaptureSettings.setOutputPath(folder.getAbsolutePath());
                            textViewImageOutputPath.setText(selectedCaptureSettings.getOutputPath());
                        }
                    });
                    dialog.show();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_OUTPUT_PATH_DIR_CHOOSER)
        {

        }
    }

    private void loadSelectedCameraSettings()
    {
        if(selectedCaptureSettings == null)
        {
            layoutCameraSettingsContent.setVisibility(View.INVISIBLE);
            return;
        }

        layoutCameraSettingsContent.setVisibility(View.VISIBLE);

        switchDefaultCamera.setChecked(getSettings().getDefaultCameraId() != null
                && selectedCaptureSettings.getCameraId().equals(getSettings().getDefaultCameraId()));

        spinnerFlash.setSelection(selectedCaptureSettings.getFlashlight().ordinal());

        textViewImageOutputPath.setText(selectedCaptureSettings.getOutputPath());
    }

    private CameraModuleSettingsData getSettings()
    {
        return (CameraModuleSettingsData) moduleSettings;
    }
}
