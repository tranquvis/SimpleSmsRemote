package tranquvis.simplesmsremote;

import android.content.Context;

import tranquvis.simplesmsremote.Helper.HotspotHelper;
import tranquvis.simplesmsremote.Helper.MobileDataHelper;

/**
 * Created by Andreas Kaltenleitner on 29.08.2016.
 */
public class ControlCommand
{
    public static final ControlCommand WIFI_HOTSPOT_ENABLE = new ControlCommand("enable hotspot");
    public static final ControlCommand WIFI_HOTSPOT_DISABLE = new ControlCommand("disable hotspot");
    public static final ControlCommand MOBILE_DATA_ENABLE = new ControlCommand("enable mobile data");
    public static final ControlCommand MOBILE_DATA_DISABLE = new ControlCommand("disable mobile data");

    public static final ControlCommand[] ALL = {WIFI_HOTSPOT_ENABLE, WIFI_HOTSPOT_DISABLE,
            MOBILE_DATA_ENABLE, MOBILE_DATA_DISABLE};

    public static ControlCommand getFromCommand(String command)
    {
        command = command.trim();
        for (ControlCommand com : ALL)
        {
            if(com.command.equals(command))
                return com;
        }
        return null;
    }

    private String command;

    private ControlCommand(String command)
    {
        this.command = command;
    }


    public boolean execute(Context context)
    {
        if (this.equals(ControlCommand.WIFI_HOTSPOT_ENABLE))
        {
            return HotspotHelper.setHotspotState(context, true);
        }
        if (this.equals(ControlCommand.WIFI_HOTSPOT_DISABLE))
        {
            return HotspotHelper.setHotspotState(context, false);
        }
        if (this.equals(ControlCommand.MOBILE_DATA_ENABLE))
        {
            return MobileDataHelper.setMobileDataState(context, true);
        }
        if (this.equals(ControlCommand.MOBILE_DATA_DISABLE))
        {
            return MobileDataHelper.setMobileDataState(context, false);
        }
        return false;
    }

    @Override
    public String toString()
    {
        return command;
    }
}
