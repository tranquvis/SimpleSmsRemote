package tranquvis.simplesmsremote.CommandManagement;

import android.content.Context;
import android.location.Location;

import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.AudioUtils;
import tranquvis.simplesmsremote.Utils.BatteryUtils;
import tranquvis.simplesmsremote.Utils.BluetoothUtils;
import tranquvis.simplesmsremote.Utils.LocationUtils;
import tranquvis.simplesmsremote.Utils.MobileDataUtils;
import tranquvis.simplesmsremote.Utils.WifiUtils;
import tranquvis.simplesmsremote.Sms.MyMessage;
import static tranquvis.simplesmsremote.CommandManagement.ControlCommand.*;

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
                if (command == WIFI_HOTSPOT_ENABLE)
                {
                    WifiUtils.SetHotspotState(context, true);
                }
                else if (command == WIFI_HOTSPOT_DISABLE)
                {
                    WifiUtils.SetHotspotState(context, false);
                }
                else if (command == WIFI_HOTSPOT_IS_ENABLED)
                {
                    boolean isHotspotEnabled = WifiUtils.IsHotspotEnabled(context);
                    result.setCustomResultMessage(context.getString(
                            isHotspotEnabled ? R.string.result_msg_hotspot_is_enabled_true
                                    : R.string.result_msg_hotspot_is_enabled_false));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command == MOBILE_DATA_ENABLE)
                {
                    MobileDataUtils.SetMobileDataState(context, true);
                }
                else if (command == MOBILE_DATA_DISABLE)
                {
                    MobileDataUtils.SetMobileDataState(context, false);
                }
                else if (command == MOBILE_DATA_IS_ENABLED)
                {
                    boolean isMobileDataEnabled = MobileDataUtils.IsMobileDataEnabled(context);
                    result.setCustomResultMessage(context.getString(
                            isMobileDataEnabled ? R.string.result_msg_mobile_data_is_enabled_true
                                    : R.string.result_msg_mobile_data_is_enabled_false));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command == BATTERY_LEVEL_GET)
                {
                    float batteryLevel = BatteryUtils.GetBatteryLevel(context);
                    result.setCustomResultMessage(context.getResources().getString(
                            R.string.result_msg_battery_level, batteryLevel*100));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command == BATTERY_IS_CHARGING)
                {
                    boolean isBatteryCharging = BatteryUtils.IsBatteryCharging(context);
                    result.setCustomResultMessage(context.getString(
                            isBatteryCharging ? R.string.result_msg_battery_is_charging_true
                                    : R.string.result_msg_battery_is_charging_false));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command == LOCATION_GET)
                {
                    Location location = LocationUtils.GetLocation(context, 4000);
                    if(location == null)
                        throw new Exception("Location Request timed out");
                    result.setCustomResultMessage(context.getString(
                            R.string.result_msg_location_coordinates,
                            location.getLatitude(), location.getLongitude()));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command == WIFI_ENABLE)
                {
                    WifiUtils.SetWifiState(context, true);
                }
                else if (command == WIFI_DISABLE)
                {
                    WifiUtils.SetWifiState(context, false);
                }
                else if (command == WIFI_IS_ENABLED)
                {
                    boolean isWifiEnabled = WifiUtils.IsWifiEnabled(context);
                    result.setCustomResultMessage(context.getString(
                            isWifiEnabled ? R.string.result_msg_wifi_is_enabled_true
                                    : R.string.result_msg_wifi_is_enabled_false));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command == BLUETOOTH_ENABLE)
                {
                    BluetoothUtils.SetBluetoothState(true);
                }
                else if (command == BLUETOOTH_DISABLE)
                {
                    BluetoothUtils.SetBluetoothState(false);
                }
                else if (command == BLUETOOTH_IS_ENABLED)
                {
                    boolean isBluetoothEnabled = BluetoothUtils.IsBluetoothEnabled();
                    result.setCustomResultMessage(context.getString(
                            isBluetoothEnabled ? R.string.result_msg_bluetooth_is_enabled_true
                                    : R.string.result_msg_bluetooth_is_enabled_false));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command == AUDIO_SET_VOLUME)
                {
                    String audioTypeStr = commandInstance.getParam(PARAM_AUDIO_TYPE);
                    String volumeStr = commandInstance.getParam(PARAM_AUDIO_VOLUME);

                    AudioUtils.AudioType audioType = AudioUtils.AudioType.GetFromName(audioTypeStr);

                    if(volumeStr.charAt(volumeStr.length() - 1) == '%')
                    {
                        float volumePercentage =
                                Float.parseFloat(volumeStr.substring(0, volumeStr.length() - 1));
                        AudioUtils.SetVolumePercentage(context, volumePercentage, audioType);
                    }
                    else
                    {
                        int volumeIndex;
                        switch (volumeStr)
                        {
                            case "vibrate":
                                if(audioType != AudioUtils.AudioType.RING)
                                    throw new Exception("vibrate is only possible for type ring");
                                volumeIndex = AudioUtils.VOLUME_INDEX_RING_VIBRATE;
                                break;
                            case "silent":
                                if(audioType != AudioUtils.AudioType.RING)
                                    throw new Exception("vibrate is only possible for type ring");
                                volumeIndex = AudioUtils.VOLUME_INDEX_RING_SILENT;
                                break;
                            default:
                                volumeIndex = Integer.parseInt(volumeStr);
                                break;
                        }

                        AudioUtils.SetVolumeIndex(context, volumeIndex, audioType);
                    }
                }
                else if (command == AUDIO_GET_VOLUME)
                {
                    String audioTypeStr = commandInstance.getParam(PARAM_AUDIO_TYPE);
                    AudioUtils.AudioType audioType = AudioUtils.AudioType.GetFromName(audioTypeStr);

                    int volumeIndex = AudioUtils.GetVolumeIndex(context, audioType);
                    String volumeStr = String.valueOf(volumeIndex);

                    if(volumeIndex == AudioUtils.VOLUME_INDEX_RING_VIBRATE)
                        volumeStr = "vibrate";
                    else if(volumeIndex == AudioUtils.VOLUME_INDEX_RING_SILENT)
                        volumeStr = "silent";

                    result.setCustomResultMessage(context.getString(
                            R.string.result_msg_audio_volume, audioTypeStr, volumeStr));
                    result.setForceSendingResultSmsMessage(true);
                }
                else if (command == AUDIO_GET_VOLUME_PERCENTAGE)
                {
                    String audioTypeStr = commandInstance.getParam(PARAM_AUDIO_TYPE);
                    AudioUtils.AudioType audioType = AudioUtils.AudioType.GetFromName(audioTypeStr);

                    float volumePercentage = AudioUtils.GetVolumePercentage(context, audioType);

                    result.setCustomResultMessage(context.getString(
                            R.string.result_msg_audio_volume_percentage, audioTypeStr,
                            volumePercentage));
                    result.setForceSendingResultSmsMessage(true);
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