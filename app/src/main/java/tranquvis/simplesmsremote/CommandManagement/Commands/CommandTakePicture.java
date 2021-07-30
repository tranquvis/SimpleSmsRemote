package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParam;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamEmpty;
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

public class CommandTakePicture extends Command {
    static final CommandParamString PARAM_OPTIONS = new CommandParamString("options");
    static final CommandParamEmpty PARAM_OPTIONS_WRAPPER =
            new CommandParamEmpty("options_wrapper");
    static final CommandParamCamera PARAM_OPTION_CAMERA = new CommandParamCamera("camera");
    static final CommandParamFlash PARAM_OPTION_FLASH = new CommandParamFlash("flash");
    static final CommandParamOnOff PARAM_OPTION_AUTOFOCUS = new CommandParamOnOff("autofocus");


    private static final String
            PATTERN_ROOT = AdaptSimplePattern("(?:take|capture) (?:picture|photo)((?: with (.*?))?)");

    private static final String
            PATTERN_OPTIONS_WRAPPER = AdaptSimplePattern(" with (.*?)");

    private static final String PATTERN_CAMERA = AdaptSimplePattern(
            "(\\d+|back|front|external)( (cam(era)?|lens))?" +
                    "|(cam(era)?|lens) (back|front|external|\\d+)");

    private static final String PATTERN_FLASH = AdaptSimplePattern(
            "flash(light)?( (enabled|disabled|on|off|auto))?|no flash(light)?");

    private static final String PATTERN_AUTOFOCUS = AdaptSimplePattern(
            "autofocus( (on|enabled|off|disabled))?|(no) autofocus");

    public CommandTakePicture(@NonNull Module module) {
        super(module);

        titleRes = R.string.command_title_take_picture;
        syntaxDescList = new String[]{
                "take picture",
                "take picture with [" + PARAM_OPTIONS.getId() + "...]"
        };
        patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.BY_INDEX_IF_NOT_EMPTY,
                new PatternTreeNode(PARAM_OPTIONS_WRAPPER.getId(),
                        PATTERN_OPTIONS_WRAPPER,
                        MatchType.BY_INDEX_STRICT,
                        new PatternTreeNode(PARAM_OPTIONS.getId(),
                                PATTERN_MULTI_PARAMS,
                                MatchType.BY_CHILD_PATTERN_STRICT,
                                new PatternTreeNode(PARAM_OPTION_CAMERA.getId(),
                                        PATTERN_CAMERA,
                                        MatchType.DO_NOT_MATCH
                                ),
                                new PatternTreeNode(PARAM_OPTION_FLASH.getId(),
                                        PATTERN_FLASH,
                                        MatchType.DO_NOT_MATCH
                                ),
                                new PatternTreeNode(PARAM_OPTION_AUTOFOCUS.getId(),
                                        PATTERN_AUTOFOCUS,
                                        MatchType.DO_NOT_MATCH
                                )
                        )
                )
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance, CommandExecResult result)
            throws Exception {
        //region retrieve given capture settings
        String cameraId = null;
        CameraUtils.LensFacing lensFacing = null;
        CaptureSettings.FlashlightMode flashMode = null;
        Boolean autofocus = null;

        if (commandInstance.isParamAssigned(PARAM_OPTIONS_WRAPPER)) {
            Object camera = commandInstance.getParam(PARAM_OPTION_CAMERA);
            if (camera instanceof String)
                cameraId = (String) camera;
            else
                lensFacing = (CameraUtils.LensFacing) camera;

            flashMode = commandInstance.getParam(PARAM_OPTION_FLASH);
            autofocus = commandInstance.getParam(PARAM_OPTION_AUTOFOCUS);
        }
        //endregion

        CameraModuleSettingsData moduleSettings = (CameraModuleSettingsData)
                getModule().getUserData().getSettings();

        //region get corresponding camera
        CameraUtils.MyCameraInfo cameraInfo;

        if (cameraId != null) {
            cameraInfo = CameraUtils.GetCamera(context, cameraId, null);
        } else if (lensFacing != null) {
            cameraInfo = CameraUtils.GetCamera(context, null, lensFacing);
        } else if (moduleSettings != null && moduleSettings.getDefaultCameraId() != null) {
            cameraInfo = CameraUtils.GetCamera(context,
                    moduleSettings.getDefaultCameraId(), null);
        } else {
            throw new Exception("Default camera not set.");
        }

        if (cameraInfo == null)
            throw new Exception("Default camera not found on device.");
        //endregion

        //region create capture settings
        CaptureSettings captureSettings = moduleSettings.getCaptureSettingsByCameraId(
                cameraInfo.getId()).clone();
        if (captureSettings == null)
            captureSettings = cameraInfo.getDefaultCaptureSettings();

        if (flashMode != null)
            captureSettings.setFlashlight(flashMode);
        if (autofocus != null)
            captureSettings.setAutofocus(autofocus);
        //endregion

        CameraUtils.TakePicture(context, cameraInfo, captureSettings);
    }

    private static class CommandParamCamera extends CommandParam<Object> {
        CommandParamCamera(String id) {
            super(id);
        }

        /**
         * Get parameter value from input.
         *
         * @return either the id as {@code String} or lens facing as {@code CameraUtils.LensFacing}
         */
        @Override
        public Object getValueFromInput(String input) {
            if (input.matches("(?i).*?front.*?"))
                return CameraUtils.LensFacing.FRONT;
            if (input.matches("(?i).*?back.*?"))
                return CameraUtils.LensFacing.BACK;
            if (input.matches("(?i).*?ext(ernal)?.*?"))
                return CameraUtils.LensFacing.EXTERNAL;

            // retrieve camera id
            Pattern pattern = Pattern.compile(".*?(\\d+).*?");
            Matcher matcher = pattern.matcher(input);
            if (!matcher.find() || matcher.groupCount() < 1)
                throw new IllegalArgumentException("unexpected input: '" + input + "'");
            return matcher.group(1);
        }
    }

    private static class CommandParamFlash extends CommandParam<CaptureSettings.FlashlightMode> {
        CommandParamFlash(String id) {
            super(id);
        }

        @Override
        public CaptureSettings.FlashlightMode getValueFromInput(String input) {
            if (input.matches("(?i).*?(no|off|disabled).*?"))
                return CaptureSettings.FlashlightMode.OFF;
            if (input.matches("(?i).*?auto.*?"))
                return CaptureSettings.FlashlightMode.AUTO;
            else
                return CaptureSettings.FlashlightMode.ON;
        }
    }
}
