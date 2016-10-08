package tranquvis.simplesmsremote.CommandManagement;

/**
 * Created by Andreas Kaltenleitner on 29.08.2016.
 */
public class ControlCommand
{
    public static final ControlCommand WIFI_HOTSPOT_ENABLE = new ControlCommand("enable hotspot");
    public static final ControlCommand WIFI_HOTSPOT_DISABLE = new ControlCommand("disable hotspot");
    public static final ControlCommand WIFI_HOTSPOT_IS_ENABLED = new ControlCommand("is hotspot enabled");

    public static final ControlCommand MOBILE_DATA_ENABLE = new ControlCommand("enable mobile data");
    public static final ControlCommand MOBILE_DATA_DISABLE = new ControlCommand("disable mobile data");
    public static final ControlCommand MOBILE_DATA_IS_ENABLED = new ControlCommand("is mobile data enabled");

    public static final ControlCommand BATTERY_LEVEL_GET = new ControlCommand("get battery level");
    public static final ControlCommand BATTERY_IS_CHARGING = new ControlCommand("is battery charging");

    public static final ControlCommand LOCATION_GET = new ControlCommand("get location");

    public static final ControlCommand WIFI_ENABLE = new ControlCommand("enable wifi");
    public static final ControlCommand WIFI_DISABLE = new ControlCommand("disable wifi");
    public static final ControlCommand WIFI_IS_ENABLED = new ControlCommand("is wifi enabled");

    public static final ControlCommand BLUETOOTH_ENABLE = new ControlCommand("enable bluetooth");
    public static final ControlCommand BLUETOOTH_DISABLE = new ControlCommand("disable bluetooth");
    public static final ControlCommand BLUETOOTH_IS_ENABLED = new ControlCommand("is bluetooth enabled");

    public static final ControlCommand AUDIO_SET_VOLUME = new ControlCommand("set volume [audio type] to [volume]");
    public static final ControlCommand AUDIO_GET_VOLUME = new ControlCommand("get volume for [audio type]");
    public static final ControlCommand AUDIO_GET_VOLUME_PERCENTAGE = new ControlCommand("get volume percentage for [audio type]");

    public static final ControlCommand[] ALL = {
            WIFI_HOTSPOT_ENABLE, WIFI_HOTSPOT_DISABLE, WIFI_HOTSPOT_IS_ENABLED,
            MOBILE_DATA_ENABLE, MOBILE_DATA_DISABLE, MOBILE_DATA_IS_ENABLED,
            BATTERY_LEVEL_GET, BATTERY_IS_CHARGING,
            LOCATION_GET,
            WIFI_ENABLE, WIFI_DISABLE, WIFI_IS_ENABLED,
            BLUETOOTH_ENABLE, BLUETOOTH_DISABLE, BLUETOOTH_IS_ENABLED,
            AUDIO_SET_VOLUME, AUDIO_GET_VOLUME, AUDIO_GET_VOLUME_PERCENTAGE
    };

    public static ControlCommand getFromCommand(String command)
    {
        command = command.trim().toLowerCase();
        for (ControlCommand com : ALL)
        {

            if(com.commandTemplate.equals(command))
                return com;
        }
        return null;
    }

    private String commandTemplate;
    private String[] paramNames;

    private ControlCommand(String commandTemplate)
    {
        this.commandTemplate = commandTemplate;

        String[] s1 = commandTemplate.split("\\[");
        paramNames = new String[s1.length];
        for (int i = 1; i < s1.length; i++) {
            paramNames[i-1] = s1[i].split("\\]")[0];
        }
    }

    public String getCommandTemplate() {
        return commandTemplate;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    @Override
    public String toString()
    {
        return commandTemplate;
    }

    public ControlModule getModule()
    {
        return ControlModule.getFromCommand(this);
    }
}
