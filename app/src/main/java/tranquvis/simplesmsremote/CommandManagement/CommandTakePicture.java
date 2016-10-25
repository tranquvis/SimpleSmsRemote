package tranquvis.simplesmsremote.CommandManagement;

import tranquvis.simplesmsremote.Data.CameraModuleSettingsData;
import tranquvis.simplesmsremote.Data.CaptureSettings;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.CameraUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by kalte on 25.10.2016.
 */

public class CommandTakePicture extends ControlCommand
{
    CommandTakePicture() {
        super();
        titleRes = R.string.command_title_take_picture;
        syntaxDescList = new String[]{
                "take picture"
        };
        patternTree = new PatternTreeNode("root",
                "take\\s+(picture|photo)",
                MatchType.DO_NOT_MATCH
        );
    }

    @Override
    public void execute(CommandExec commandExecutor) throws Exception
    {
        CameraModuleSettingsData moduleSettings = (CameraModuleSettingsData)
                getModule().getUserData().getSettings();
        if(moduleSettings == null || moduleSettings.getDefaultCameraId() == null)
        {
            throw new Exception("Default camera not set.");
        }

        CameraUtils.MyCameraInfo cameraInfo = CameraUtils.GetCamera(commandExecutor.getContext(),
                moduleSettings.getDefaultCameraId(), null);
        if(cameraInfo == null)
            throw new Exception("Default camera not found on device.");

        CaptureSettings captureSettings
                = moduleSettings.getCaptureSettingsByCameraId(cameraInfo.getId());
        if(captureSettings == null)
            captureSettings = cameraInfo.getDefaultCaptureSettings();

        CameraUtils.TakePicture(commandExecutor.getContext(), cameraInfo, captureSettings);
    }
}
