package tranquvis.simplesmsremote.CommandManagement;

import android.content.Context;
import android.support.annotation.StringRes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 29.08.2016.
 */
public abstract class ControlCommand<T extends ControlCommand>
{
    protected static final ControlCommand TAKE_PICTURE_WITH_OPTIONS =
            new CommandTakePictureWithOptions();
    protected static final ControlCommand TAKE_PICTURE = new CommandTakePicture();

    public static final String
            PARAM_AUDIO_TYPE = "audio type",
            PARAM_AUDIO_VOLUME = "volume",
            PARAM_BRIGHTNESS = "brightness",
            PARAM_DISPLAY_OFF_TIMEOUT = "timeout",
            PARAM_TAKE_PICTURE_SETTINGS = "settings";

    static
    {
        WIFI_HOTSPOT_ENABLE = new ControlCommand("enable hotspot");
        WIFI_HOTSPOT_DISABLE = new ControlCommand("disable hotspot");
        WIFI_HOTSPOT_IS_ENABLED = new ControlCommand("is hotspot enabled");

        MOBILE_DATA_ENABLE = new ControlCommand("enable mobile data");
        MOBILE_DATA_DISABLE = new ControlCommand("disable mobile data");
        MOBILE_DATA_IS_ENABLED = new ControlCommand("is mobile data enabled");

        BATTERY_LEVEL_GET = new ControlCommand("get battery level");
        BATTERY_IS_CHARGING = new ControlCommand("is battery charging");

        LOCATION_GET = new ControlCommand("get location");

        WIFI_ENABLE = new ControlCommand("enable wifi");
        WIFI_DISABLE = new ControlCommand("disable wifi");
        WIFI_IS_ENABLED = new ControlCommand("is wifi enabled");

        BLUETOOTH_ENABLE = new ControlCommand("enable bluetooth");
        BLUETOOTH_DISABLE = new ControlCommand("disable bluetooth");
        BLUETOOTH_IS_ENABLED = new ControlCommand("is bluetooth enabled");

        AUDIO_SET_VOLUME = new ControlCommand("set volume [" + PARAM_AUDIO_TYPE + "] to [" +
                PARAM_AUDIO_VOLUME + "]");
        AUDIO_GET_VOLUME = new ControlCommand("get volume for [" + PARAM_AUDIO_TYPE + "]");
        AUDIO_GET_VOLUME_PERCENTAGE = new ControlCommand("get volume percentage for [" +
                PARAM_AUDIO_TYPE + "]");

        DISPLAY_GET_BRIGHTNESS = new ControlCommand("get brightness");
        DISPLAY_SET_BRIGHTNESS = new ControlCommand("set brightness to [" + PARAM_BRIGHTNESS + "]");
        DISPLAY_GET_OFF_TIMEOUT = new ControlCommand("get display off timeout");
        DISPLAY_SET_OFF_TIMEOUT = new ControlCommand("set display off timeout to ["
                + PARAM_DISPLAY_OFF_TIMEOUT + "]");
        DISPLAY_TURN_OFF = new ControlCommand("turn display off");
    }

    @StringRes
    protected int titleRes;
    protected String[] syntaxDescList;
    protected PatternTreeNode patternTree;


    protected ControlCommand() {}

    public int getTitleRes()
    {
        return titleRes;
    }

    public abstract void execute(CommandExec commandExecutor) throws Exception;

    public ControlModule getModule()
    {
        return ControlModule.getFromCommand(this);
    }

    /**
     * Get all Commands by using reflection.
     * @return all defined control commands
     */
    public static List<ControlCommand> GetAllCommands()
    {
        List<ControlCommand> commands = new ArrayList<>();

        for (Field field : ControlCommand.class.getDeclaredFields())
        {

            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && field.getType() == ControlCommand.class)
            {
                try
                {
                    commands.add((ControlCommand) field.get(null));
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return commands;
    }
}

