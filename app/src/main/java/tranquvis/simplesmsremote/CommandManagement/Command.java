package tranquvis.simplesmsremote.CommandManagement;

import android.content.Context;
import android.support.annotation.StringRes;

import org.intellij.lang.annotations.Language;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetWifiState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePicture;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePictureWithOptions;
import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.Sms.MyMessage;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 29.08.2016.
 */
public abstract class Command
{

    @Language("RegExp")
    protected static final String PATTERN_MULTI_PARAMS = "(?!$)(?:\\s*?(.*?)\\s*?(?:and|,|$))\\s*";

    /*
    public static final String
            PARAM_AUDIO_TYPE = "audio type",
            PARAM_AUDIO_VOLUME = "volume",
            PARAM_BRIGHTNESS = "brightness",
            PARAM_DISPLAY_OFF_TIMEOUT = "timeout",
            PARAM_TAKE_PICTURE_SETTINGS = "settings";

    static
    {
        WIFI_HOTSPOT_ENABLE = new Command("enable hotspot");
        WIFI_HOTSPOT_DISABLE = new Command("disable hotspot");
        WIFI_HOTSPOT_IS_ENABLED = new Command("is hotspot enabled");

        MOBILE_DATA_ENABLE = new Command("enable mobile data");
        MOBILE_DATA_DISABLE = new Command("disable mobile data");
        MOBILE_DATA_IS_ENABLED = new Command("is mobile data enabled");

        BATTERY_LEVEL_GET = new Command("get battery level");
        BATTERY_IS_CHARGING = new Command("is battery charging");

        LOCATION_GET = new Command("get location");

        WIFI_ENABLE = new Command("enable wifi");
        WIFI_DISABLE = new Command("disable wifi");
        WIFI_IS_ENABLED = new Command("is wifi enabled");

        BLUETOOTH_ENABLE = new Command("enable bluetooth");
        BLUETOOTH_DISABLE = new Command("disable bluetooth");
        BLUETOOTH_IS_ENABLED = new Command("is bluetooth enabled");

        AUDIO_SET_VOLUME = new Command("set volume [" + PARAM_AUDIO_TYPE + "] to [" +
                PARAM_AUDIO_VOLUME + "]");
        AUDIO_GET_VOLUME = new Command("get volume for [" + PARAM_AUDIO_TYPE + "]");
        AUDIO_GET_VOLUME_PERCENTAGE = new Command("get volume percentage for [" +
                PARAM_AUDIO_TYPE + "]");

        DISPLAY_GET_BRIGHTNESS = new Command("get brightness");
        DISPLAY_SET_BRIGHTNESS = new Command("set brightness to [" + PARAM_BRIGHTNESS + "]");
        DISPLAY_GET_OFF_TIMEOUT = new Command("get display off timeout");
        DISPLAY_SET_OFF_TIMEOUT = new Command("set display off timeout to ["
                + PARAM_DISPLAY_OFF_TIMEOUT + "]");
        DISPLAY_TURN_OFF = new Command("turn display off");
    }
*/

    @StringRes
    protected int titleRes;
    protected String[] syntaxDescList;
    protected PatternTreeNode patternTree;


    protected Command() {}

    public int getTitleRes()
    {
        return titleRes;
    }

    protected abstract void execute(Context context, CommandInstance commandInstance,
                                    CommandExecResult result)
            throws Exception;

    public ControlModule getModule()
    {
        return ControlModule.getFromCommand(this);
    }
}

