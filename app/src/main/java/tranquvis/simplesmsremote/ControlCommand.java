package tranquvis.simplesmsremote;

import android.content.Context;

import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.Helper.HotspotHelper;
import tranquvis.simplesmsremote.Helper.MobileDataHelper;
import tranquvis.simplesmsremote.Services.Sms.MySms;

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


    public boolean execute(Context context, MySms controlSms)
    {
        if(!isExecutionGranted(controlSms, context))
            return false;

        try
        {
            if (this.equals(ControlCommand.WIFI_HOTSPOT_ENABLE)) {
                HotspotHelper.setHotspotState(context, true);
            }
            else if (this.equals(ControlCommand.WIFI_HOTSPOT_DISABLE)) {
                HotspotHelper.setHotspotState(context, false);
            }
            else if (this.equals(ControlCommand.MOBILE_DATA_ENABLE)) {
                MobileDataHelper.setMobileDataState(context, true);
            }
            else if (this.equals(ControlCommand.MOBILE_DATA_DISABLE)) {
                MobileDataHelper.setMobileDataState(context, false);
            }

            DataManager.addLogEntry(LogEntry.Predefined.ComExecSuccess(context, this), context);
            return true;
        }
        catch (Exception e)
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedUnexpected(context, this),
                    context);
            return false;
        }
    }

    @Override
    public String toString()
    {
        return command;
    }

    public ControlModule getModule()
    {
        return ControlModule.getFromCommand(this);
    }

    public boolean isExecutionGranted(MySms controlSms, Context context)
    {
        ControlModule module = getModule();
        ControlModuleUserData moduleUserData = module.getUserData();
        if(moduleUserData == null)
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedModuleDisabled(context, this),
                    context);
            return false;
        }
        if(!module.isCompatible())
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPhoneIncompatible(context,
                    this), context);
            return false;
        }
        if(!moduleUserData.isPhoneGranted(controlSms.getPhoneNumber()))
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPhoneNotGranted(context, this,
                    controlSms.getPhoneNumber()), context);
            return false;
        }
        if(!module.checkPermissions(context))
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPermissionDenied(context,
                    this), context);
            return false;
        }

        return true;
    }
}
