package tranquvis.simplesmsremote.CommandManagement;

import android.content.Context;
import android.location.Location;

import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.BatteryUtils;
import tranquvis.simplesmsremote.Utils.BluetoothUtils;
import tranquvis.simplesmsremote.Utils.LocationUtils;
import tranquvis.simplesmsremote.Utils.MobileDataUtils;
import tranquvis.simplesmsremote.Utils.WifiUtils;
import tranquvis.simplesmsremote.Sms.MyMessage;

/**
 * Created by Andi on 08.10.2016.
 */

public class CommandExec
{
    private CommandInstance commandInstance;

    public CommandExec(CommandInstance commandInstance) {
        this.commandInstance = commandInstance;
    }

    public CommandExecResult execute(Context context, MyMessage controlSms)
    {
        CommandExecResult result = new CommandExecResult(commandInstance);
        ControlCommand command = commandInstance.getCommand();

        if(!isGranted(controlSms, context))
            result.setSuccess(false);
        else
        {
            try
            {
                if (command.equals(ControlCommand.WIFI_HOTSPOT_ENABLE))
                {
                    WifiUtils.SetHotspotState(context, true);
                }
                else if (command.equals(ControlCommand.WIFI_HOTSPOT_DISABLE))
                {
                    WifiUtils.SetHotspotState(context, false);
                }
                else if (command.equals(ControlCommand.WIFI_HOTSPOT_IS_ENABLED))
                {
                    boolean isHotspotEnabled = WifiUtils.IsHotspotEnabled(context);
                    result.setCustomResultMessage(context.getString(
                            isHotspotEnabled ? R.string.result_msg_hotspot_is_enabled_true
                                    : R.string.result_msg_hotspot_is_enabled_false));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command.equals(ControlCommand.MOBILE_DATA_ENABLE))
                {
                    MobileDataUtils.SetMobileDataState(context, true);
                }
                else if (command.equals(ControlCommand.MOBILE_DATA_DISABLE))
                {
                    MobileDataUtils.SetMobileDataState(context, false);
                }
                else if (command.equals(ControlCommand.MOBILE_DATA_IS_ENABLED))
                {
                    boolean isMobileDataEnabled = MobileDataUtils.IsMobileDataEnabled(context);
                    result.setCustomResultMessage(context.getString(
                            isMobileDataEnabled ? R.string.result_msg_mobile_data_is_enabled_true
                                    : R.string.result_msg_mobile_data_is_enabled_false));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command.equals(ControlCommand.BATTERY_LEVEL_GET))
                {
                    float batteryLevel = BatteryUtils.GetBatteryLevel(context);
                    result.setCustomResultMessage(context.getResources().getString(
                            R.string.result_msg_battery_level, batteryLevel*100));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command.equals(ControlCommand.BATTERY_IS_CHARGING))
                {
                    boolean isBatteryCharging = BatteryUtils.IsBatteryCharging(context);
                    result.setCustomResultMessage(context.getString(
                            isBatteryCharging ? R.string.result_msg_battery_is_charging_true
                                    : R.string.result_msg_battery_is_charging_false));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command.equals(ControlCommand.LOCATION_GET))
                {
                    Location location = LocationUtils.GetLocation(context, 4000);
                    if(location == null)
                        throw new Exception("Location Request timed out");
                    result.setCustomResultMessage(context.getString(
                            R.string.result_msg_location_coordinates,
                            location.getLatitude(), location.getLongitude()));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command.equals(ControlCommand.WIFI_ENABLE))
                {
                    WifiUtils.SetWifiState(context, true);
                }
                else if (command.equals(ControlCommand.WIFI_DISABLE))
                {
                    WifiUtils.SetWifiState(context, false);
                }
                else if (command.equals(ControlCommand.WIFI_IS_ENABLED))
                {
                    boolean isWifiEnabled = WifiUtils.IsWifiEnabled(context);
                    result.setCustomResultMessage(context.getString(
                            isWifiEnabled ? R.string.result_msg_wifi_is_enabled_true
                                    : R.string.result_msg_wifi_is_enabled_false));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command.equals(ControlCommand.BLUETOOTH_ENABLE))
                {
                    BluetoothUtils.SetBluetoothState(true);
                }
                else if (command.equals(ControlCommand.BLUETOOTH_DISABLE))
                {
                    BluetoothUtils.SetBluetoothState(false);
                }
                else if (command.equals(ControlCommand.BLUETOOTH_IS_ENABLED))
                {
                    boolean isBluetoothEnabled = BluetoothUtils.IsBluetoothEnabled();
                    result.setCustomResultMessage(context.getString(
                            isBluetoothEnabled ? R.string.result_msg_bluetooth_is_enabled_true
                                    : R.string.result_msg_bluetooth_is_enabled_false));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command.equals(ControlCommand.AUDIO_SET_VOLUME))
                {
                    //TODO
                }

                DataManager.addLogEntry(LogEntry.Predefined.ComExecSuccess(context, command), context);
                result.setSuccess(true);
            } catch (Exception e)
            {
                e.printStackTrace();
                DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedUnexpected(context, command),
                        context);
                result.setSuccess(false);
            }
        }

        return result;
    }


    private boolean isGranted(MyMessage controlSms, Context context)
    {
        ControlModule module = commandInstance.getCommand().getModule();
        ControlModuleUserData moduleUserData = module.getUserData();

        if(!module.isCompatible())
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPhoneIncompatible(context,
                    commandInstance.getCommand()), context);
            return false;
        }
        if(moduleUserData == null)
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedModuleDisabled(context,
                    commandInstance.getCommand()),
                    context);
            return false;
        }
        if(!moduleUserData.isPhoneGranted(controlSms.getPhoneNumber()))
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPhoneNotGranted(context,
                    commandInstance.getCommand(), controlSms.getPhoneNumber()),
                    context);
            return false;
        }
        if(!module.checkPermissions(context))
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPermissionDenied(context,
                    commandInstance.getCommand()), context);
            return false;
        }

        return true;
    }
}