package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.Manifest;

import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetWifiState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetWifiState;
import tranquvis.simplesmsremote.CommandManagement.Module;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class ModuleWifi extends Module
{
    private static final CommandSetWifiState COMMAND_SET_WIFI_STATE = new CommandSetWifiState();
    private static final CommandGetWifiState COMMAND_GET_WIFI_STATE = new CommandGetWifiState();

    public ModuleWifi()
    {
        super("wifi");

        this.titleRes = R.string.control_module_title_wifi;
        this.descriptionRes = R.string.control_module_desc_wifi;
        this.iconRes = R.drawable.ic_signal_wifi_2_bar_grey_700_36dp;

        this.requiredPermissions = new String[]{
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_SETTINGS
        };
    }
}
