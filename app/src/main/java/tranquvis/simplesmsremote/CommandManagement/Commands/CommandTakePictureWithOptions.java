package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;

import org.intellij.lang.annotations.Language;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tranquvis.simplesmsremote.CommandManagement.Command;
import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParam;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamOnOff;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamString;
import tranquvis.simplesmsremote.Data.CameraModuleSettingsData;
import tranquvis.simplesmsremote.Data.CaptureSettings;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.CameraUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 25.10.2016.
 */

public class CommandTakePictureWithOptions extends Command
{
    private static final CommandParamString PARAM_OPTIONS = new CommandParamString("options");
    static final CommandParamCamera PARAM_CAMERA = new CommandParamCamera("camera");
    static final CommandParamFlash PARAM_FLASH = new CommandParamFlash("flash");
    static final CommandParamOnOff PARAM_AUTOFOCUS = new CommandParamOnOff("autofocus");

    @Language("RegExp")
    private static final String
            PATTERN_ROOT = "(?i)^\\s*(?:take|capture)\\s+(?:picture|photo)\\s+with\\s+(.*)\\s*$",
            PATTERN_CAMERA = "(?i)^\\s*(\\d+|((back|front|external)(\\s+((cam(era)?)|lens)?))" +
                    "|(((cam(era)?)|lens)?\\s+(back|front|external|\\d+)))\\s*$",
            PATTERN_FLASH = "(?i)^\\s*(flash(light)?(\\s+(enabled|disabled|on|off|auto))?)" +
                    "|(no\\s+flash(light)?)\\s*$",
            PATTERN_AUTOFOCUS = "(?i)^\\s*autofocus(?:\\s+(on|enabled|off|disabled))?" +
                    "|(?:(no)\\s+autofocus)\\s*$";

    public CommandTakePictureWithOptions(@NonNull Module module)
    {
        super(module);

        titleRes = R.string.command_title_take_picture_with_options;
        syntaxDescList = new String[]{
                "take picture with [" + PARAM_OPTIONS.getId() + "]"
        };
        patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.BY_INDEX_STRICT,
                new PatternTreeNode(PARAM_OPTIONS.getId(),
                        PATTERN_MULTI_PARAMS,
                        MatchType.BY_CHILD_PATTERN_STRICT,
                        new PatternTreeNode(PARAM_CAMERA.getId(),
                                PATTERN_CAMERA,
                                MatchType.DO_NOT_MATCH
                        ),
                        new PatternTreeNode(PARAM_FLASH.getId(),
                                PATTERN_FLASH,
                                MatchType.DO_NOT_MATCH
                        ),
                        new PatternTreeNode(PARAM_AUTOFOCUS.getId(),
                                PATTERN_AUTOFOCUS,
                                MatchType.DO_NOT_MATCH
                        )
                )
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance, CommandExecResult result)
            throws Exception
    {
        //region retrieve given capture settings
        String cameraId = null;
        CameraUtils.LensFacing lensFacing = null;

        Object camera = commandInstance.getParam(PARAM_CAMERA);
        if(camera instanceof String)
            cameraId = (String) camera;
        else
            lensFacing = (CameraUtils.LensFacing) camera;

        CaptureSettings.FlashlightMode flashMode = commandInstance.getParam(PARAM_FLASH);
        Boolean autofocus = commandInstance.getParam(PARAM_AUTOFOCUS);
        //endregion

        CameraModuleSettingsData moduleSettings = (CameraModuleSettingsData)
                getModule().getUserData().getSettings();

        //region get corresponding camera
        CameraUtils.MyCameraInfo cameraInfo;

        if(cameraId != null)
        {
            cameraInfo = CameraUtils.GetCamera(context, cameraId, null);
        }
        else if(lensFacing != null)
        {
            cameraInfo = CameraUtils.GetCamera(context, null, lensFacing);
        }
        else if(moduleSettings != null && moduleSettings.getDefaultCameraId() != null)
        {
            cameraInfo = CameraUtils.GetCamera(context,
                    moduleSettings.getDefaultCameraId(), null);
        }
        else
        {
            throw new Exception("Default camera not set.");
        }

        if(cameraInfo == null)
            throw new Exception("Default camera not found on device.");
        //endregion

        //region create capture settings
        CaptureSettings captureSettings = moduleSettings.getCaptureSettingsByCameraId(
                cameraInfo.getId()).clone();
        if(captureSettings == null)
            captureSettings = cameraInfo.getDefaultCaptureSettings();

        if(flashMode != null)
            captureSettings.setFlashlight(flashMode);
        if(autofocus != null)
            captureSettings.setAutofocus(autofocus);
        //endregion

        CameraUtils.TakePicture(context, cameraInfo, captureSettings);
    }

    private static class CommandParamCamera extends CommandParam<Object>
    {
        CommandParamCamera(String id) {
            super(id);
        }

        /**
         * Get parameter value from input.
         * @return either the id as {@code String} or lens facing as {@code CameraUtils.LensFacing}
         */
        @Override
        public Object getValueFromInput(String input) {
            if(input.contains("front"))
                return CameraUtils.LensFacing.FRONT;
            if(input.contains("back"))
                return CameraUtils.LensFacing.FRONT;

            // retrieve camera id
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(input);
            if(!matcher.find() || matcher.groupCount() < 2)
                throw new IllegalArgumentException("unexpected input");
            return matcher.group(1);
        }
    }

    private static class CommandParamFlash extends CommandParam<CaptureSettings.FlashlightMode>
    {
        CommandParamFlash(String id) {
            super(id);
        }

        @Override
        public CaptureSettings.FlashlightMode getValueFromInput(String input) {
            if(input.contains("no") || input.contains("off") || input.contains("disabled"))
                return CaptureSettings.FlashlightMode.OFF;
            if(input.contains("auto"))
                return CaptureSettings.FlashlightMode.AUTO;
            else
                return CaptureSettings.FlashlightMode.ON;
        }
    }
}
