package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.Manifest;

import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetHotspotState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetHotspotState;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class ModuleWifiHotspot extends Module
{
    public final CommandSetHotspotState commandSetHotspotState = new CommandSetHotspotState(this);
    public final CommandGetHotspotState commandGetHotspotState = new CommandGetHotspotState(this);

    public ModuleWifiHotspot()
    {
        this.titleRes = R.string.control_module_title_wifi_hotspot;
        this.descriptionRes = R.string.control_module_desc_wifi_hotspot;
        this.iconRes = R.drawable.ic_wifi_tethering_grey_700_36dp;

        this.requiredPermissions = new String[]{
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_SETTINGS
        };
    }
}
