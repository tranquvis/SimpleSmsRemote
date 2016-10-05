package tranquvis.simplesmsremote;

import android.content.Context;
import android.location.Location;

import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.Helper.BatteryHelper;
import tranquvis.simplesmsremote.Helper.BluetoothHelper;
import tranquvis.simplesmsremote.Helper.WifiHelper;
import tranquvis.simplesmsremote.Helper.LocationHelper;
import tranquvis.simplesmsremote.Helper.MobileDataHelper;
import tranquvis.simplesmsremote.Services.Sms.MySms;

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

    public static final ControlCommand BATTERY_LEVEL_FETCH = new ControlCommand("fetch battery level");
    public static final ControlCommand BATTERY_IS_CHARGING = new ControlCommand("is battery charging");

    public static final ControlCommand LOCATION_FETCH = new ControlCommand("fetch location");

    public static final ControlCommand WIFI_ENABLE = new ControlCommand("enable wifi");
    public static final ControlCommand WIFI_DISABLE = new ControlCommand("disable wifi");
    public static final ControlCommand WIFI_IS_ENABLED = new ControlCommand("is wifi enabled");

    public static final ControlCommand BLUETOOTH_ENABLE = new ControlCommand("enable bluetooth");
    public static final ControlCommand BLUETOOTH_DISABLE = new ControlCommand("disable bluetooth");
    public static final ControlCommand BLUETOOTH_IS_ENABLED = new ControlCommand("is blueooth enabled");

    private static final ControlCommand[] ALL = {
            WIFI_HOTSPOT_ENABLE, WIFI_HOTSPOT_DISABLE, WIFI_HOTSPOT_IS_ENABLED,
            MOBILE_DATA_ENABLE, MOBILE_DATA_DISABLE, MOBILE_DATA_IS_ENABLED,
            BATTERY_LEVEL_FETCH, BATTERY_IS_CHARGING,
            LOCATION_FETCH,
            WIFI_ENABLE, WIFI_DISABLE, WIFI_IS_ENABLED,
            BLUETOOTH_ENABLE, BLUETOOTH_DISABLE, BLUETOOTH_IS_ENABLED
    };

    public static ControlCommand getFromCommand(String command)
    {
        command = command.trim().toLowerCase();
        for (ControlCommand com : ALL)
        {
            if(com.command.equals(command))
                return com;
        }
        return null;
    }

    private static ExecutionResult lastExec;

    public static ExecutionResult getLastExec()
    {
        return lastExec;
    }


    private String command;

    private ControlCommand(String command)
    {
        this.command = command;
    }

    public ExecutionResult execute(Context context, MySms controlSms)
    {
        lastExec = new ExecutionResult(this);

        if(!isExecutionGranted(controlSms, context))
            lastExec.success = false;
        else
        {
            try
            {
                if (this.equals(ControlCommand.WIFI_HOTSPOT_ENABLE))
                {
                    WifiHelper.SetHotspotState(context, true);
                }
                else if (this.equals(ControlCommand.WIFI_HOTSPOT_DISABLE))
                {
                    WifiHelper.SetHotspotState(context, false);
                }
                else if (this.equals(ControlCommand.WIFI_HOTSPOT_IS_ENABLED))
                {
                    boolean isHotspotEnabled = WifiHelper.IsHotspotEnabled(context);
                    lastExec.customResultMessage = context.getString(
                            isHotspotEnabled ? R.string.result_msg_hotspot_is_enabled_true
                                    : R.string.result_msg_hotspot_is_enabled_false);
                    lastExec.forceSendingResultSmsMessage = true;
                }
                else if (this.equals(ControlCommand.MOBILE_DATA_ENABLE))
                {
                    MobileDataHelper.SetMobileDataState(context, true);
                }
                else if (this.equals(ControlCommand.MOBILE_DATA_DISABLE))
                {
                    MobileDataHelper.SetMobileDataState(context, false);
                }
                else if (this.equals(ControlCommand.MOBILE_DATA_IS_ENABLED))
                {
                    boolean isMobileDataEnabled = MobileDataHelper.IsMobileDataEnabled(context);
                    lastExec.customResultMessage = context.getString(
                            isMobileDataEnabled ? R.string.result_msg_mobile_data_is_enabled_true
                                    : R.string.result_msg_mobile_data_is_enabled_false);
                    lastExec.forceSendingResultSmsMessage = true;
                }
                else if (this.equals(ControlCommand.BATTERY_LEVEL_FETCH))
                {
                    float batteryLevel = BatteryHelper.GetBatteryLevel(context);
                    lastExec.customResultMessage = context.getResources().getString(
                            R.string.result_msg_battery_level, batteryLevel*100);
                    lastExec.forceSendingResultSmsMessage = true;
                }
                else if (this.equals(ControlCommand.BATTERY_IS_CHARGING))
                {
                    boolean isBatteryCharging = BatteryHelper.IsBatteryCharging(context);
                    lastExec.customResultMessage = context.getString(
                            isBatteryCharging ? R.string.result_msg_battery_is_charging_true
                                    : R.string.result_msg_battery_is_charging_false);
                    lastExec.forceSendingResultSmsMessage = true;
                }
                else if (this.equals(ControlCommand.LOCATION_FETCH))
                {
                    Location location = LocationHelper.GetLocation(context, 4000);
                    if(location == null)
                        throw new Exception("Location Request timed out");
                    lastExec.customResultMessage = context.getString(
                            R.string.result_msg_location_coordinates,
                            location.getLatitude(), location.getLongitude());
                    lastExec.forceSendingResultSmsMessage = true;
                }
                else if (this.equals(ControlCommand.WIFI_ENABLE))
                {
                    WifiHelper.SetWifiState(context, true);
                }
                else if (this.equals(ControlCommand.WIFI_DISABLE))
                {
                    WifiHelper.SetWifiState(context, false);
                }
                else if (this.equals(ControlCommand.WIFI_IS_ENABLED))
                {
                    boolean isWifiEnabled = WifiHelper.IsWifiEnabled(context);
                    lastExec.customResultMessage = context.getString(
                            isWifiEnabled ? R.string.result_msg_wifi_is_enabled_true
                                    : R.string.result_msg_wifi_is_enabled_false);
                    lastExec.forceSendingResultSmsMessage = true;
                }
                else if (this.equals(ControlCommand.BLUETOOTH_ENABLE))
                {
                    BluetoothHelper.SetBluetoothState(true);
                }
                else if (this.equals(ControlCommand.BLUETOOTH_DISABLE))
                {
                    BluetoothHelper.SetBluetoothState(false);
                }
                else if (this.equals(ControlCommand.BLUETOOTH_IS_ENABLED))
                {
                    boolean isBluetoothEnabled = BluetoothHelper.IsBluetoothEnabled();
                    lastExec.customResultMessage = context.getString(
                            isBluetoothEnabled ? R.string.result_msg_bluetooth_is_enabled_true
                                    : R.string.result_msg_bluetooth_is_enabled_false);
                    lastExec.forceSendingResultSmsMessage = true;
                }

                DataManager.addLogEntry(LogEntry.Predefined.ComExecSuccess(context, this), context);
                lastExec.success = true;
            } catch (Exception e)
            {
                e.printStackTrace();
                DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedUnexpected(context, this),
                        context);
                lastExec.success = false;
            }
        }

        return lastExec;
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

    private boolean isExecutionGranted(MySms controlSms, Context context)
    {
        ControlModule module = getModule();
        ControlModuleUserData moduleUserData = module.getUserData();

        if(!module.isCompatible())
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPhoneIncompatible(context,
                    this), context);
            return false;
        }
        if(moduleUserData == null)
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedModuleDisabled(context, this),
                    context);
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

    public static class ExecutionResult
    {
        private ControlCommand command;
        private boolean success = true;
        private String customResultMessage = null;
        private boolean forceSendingResultSmsMessage = false;

        ExecutionResult(ControlCommand command)
        {
            this.command = command;
        }

        public boolean isSuccess()
        {
            return success;
        }

        public String getCustomResultMessage()
        {
            return customResultMessage;
        }

        public ControlCommand getCommand()
        {
            return command;
        }

        public boolean isForceSendingResultSmsMessage()
        {
            return forceSendingResultSmsMessage;
        }
    }
}
