package tranquvis.simplesmsremote.CommandManagement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas Kaltenleitner on 29.08.2016.
 */
public class ControlCommand
{
    public static final String
            PARAM_AUDIO_TYPE = "audio type",
            PARAM_AUDIO_VOLUME = "volume",
            PARAM_BRIGHTNESS = "brightness",
            PARAM_DISPLAY_OFF_TIMEOUT = "timeout",
            PARAM_TAKE_PICTURE_SETTINGS = "settings";

    public static final ControlCommand
            WIFI_HOTSPOT_ENABLE, WIFI_HOTSPOT_DISABLE, WIFI_HOTSPOT_IS_ENABLED,
            MOBILE_DATA_ENABLE, MOBILE_DATA_DISABLE, MOBILE_DATA_IS_ENABLED,
            BATTERY_LEVEL_GET, BATTERY_IS_CHARGING,
            LOCATION_GET,
            WIFI_ENABLE, WIFI_DISABLE, WIFI_IS_ENABLED,
            BLUETOOTH_ENABLE, BLUETOOTH_DISABLE, BLUETOOTH_IS_ENABLED,
            AUDIO_SET_VOLUME, AUDIO_GET_VOLUME, AUDIO_GET_VOLUME_PERCENTAGE,
            DISPLAY_GET_BRIGHTNESS, DISPLAY_SET_BRIGHTNESS, DISPLAY_SET_OFF_TIMEOUT,
                DISPLAY_GET_OFF_TIMEOUT, DISPLAY_TURN_OFF,
            CAMERA_TAKE_PICTURE_SIMPLE, CAMERA_TAKE_PICTURE;

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

        AUDIO_SET_VOLUME = new ControlCommand("set volume [%s] to [%s]",
                PARAM_AUDIO_TYPE, PARAM_AUDIO_VOLUME);
        AUDIO_GET_VOLUME = new ControlCommand("get volume for [%s]", PARAM_AUDIO_TYPE);
        AUDIO_GET_VOLUME_PERCENTAGE = new ControlCommand("get volume percentage for [%s]",
                PARAM_AUDIO_TYPE);

        DISPLAY_GET_BRIGHTNESS = new ControlCommand("get brightness");
        DISPLAY_SET_BRIGHTNESS = new ControlCommand("set brightness to [%s]", PARAM_BRIGHTNESS);
        DISPLAY_GET_OFF_TIMEOUT = new ControlCommand("get display off timeout");
        DISPLAY_SET_OFF_TIMEOUT = new ControlCommand("set display off timeout to [%s]",
                PARAM_DISPLAY_OFF_TIMEOUT);
        DISPLAY_TURN_OFF = new ControlCommand("turn display off");

        CAMERA_TAKE_PICTURE_SIMPLE = new ControlCommand("take picture");
        CAMERA_TAKE_PICTURE = new ControlCommand("take picture with [%s]",
                PARAM_TAKE_PICTURE_SETTINGS);
    }

    private String title;
    private String[] paramNames;

    private ControlCommand(String title)
    {
        this.title = title;

        String[] s1 = title.split("\\[");
        paramNames = new String[s1.length];
        for (int i = 1; i < s1.length; i++)
        {
            paramNames[i - 1] = s1[i].split("\\]")[0];
        }
    }

    private ControlCommand(String title, String... paramNames)
    {
        this.title = String.format(title, (Object[]) paramNames);
        this.paramNames = paramNames;
    }

    public String getTitle()
    {
        return title;
    }

    public String[] getParamNames()
    {
        return paramNames;
    }

    @Override
    public String toString()
    {
        return title;
    }

    public ControlModule getModule()
    {
        return ControlModule.getFromCommand(this);
    }

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

