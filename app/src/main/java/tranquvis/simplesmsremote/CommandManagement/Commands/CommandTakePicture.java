package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.Nullable;

import org.intellij.lang.annotations.Language;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.Data.CameraModuleSettingsData;
import tranquvis.simplesmsremote.Data.CaptureSettings;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.CameraUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 25.10.2016.
 */

public class CommandTakePicture extends Command
{
    @Language("RegExp")
    private static final String PATTERN_ROOT = "(?i)^\\s*(take|capture)\\s+(picture|photo)\\s*$";

    public CommandTakePicture(@Nullable Module module)
    {
        super(module);

        titleRes = R.string.command_title_take_picture;
        syntaxDescList = new String[]{
                "take picture"
        };
        patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance, CommandExecResult result)
            throws Exception
    {
        CameraModuleSettingsData moduleSettings = (CameraModuleSettingsData)
                getModule().getUserData().getSettings();
        if(moduleSettings == null || moduleSettings.getDefaultCameraId() == null)
        {
            throw new Exception("Default camera not set.");
        }

        CameraUtils.MyCameraInfo cameraInfo = CameraUtils.GetCamera(context,
                moduleSettings.getDefaultCameraId(), null);
        if(cameraInfo == null)
            throw new Exception("Default camera not found on device.");

        CaptureSettings captureSettings
                = moduleSettings.getCaptureSettingsByCameraId(cameraInfo.getId());
        if(captureSettings == null)
            captureSettings = cameraInfo.getDefaultCaptureSettings();

        CameraUtils.TakePicture(context, cameraInfo, captureSettings);
    }
}
