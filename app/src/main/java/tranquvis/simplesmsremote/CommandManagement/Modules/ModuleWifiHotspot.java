package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import tranquvis.simplesmsremote.Activities.ModuleActivities.CameraModuleActivity;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetHotspotState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetWifiState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetHotspotState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetWifiState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePicture;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePictureWithOptions;
import tranquvis.simplesmsremote.CommandManagement.Module;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class ModuleWifiHotspot extends Module
{
    private static final CommandSetHotspotState COMMAND_SET_HOTSPOT_STATE =
            new CommandSetHotspotState();
    private static final CommandGetHotspotState COMMAND_GET_HOTSPOT_STATE =
            new CommandGetHotspotState();

    public ModuleWifiHotspot()
    {
        super("wifi_hotspot");

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
