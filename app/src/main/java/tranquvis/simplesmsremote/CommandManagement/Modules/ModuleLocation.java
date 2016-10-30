package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.Manifest;

import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetBluetoothState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetLocationCoordinates;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetBluetoothState;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class ModuleLocation extends Module
{
    public final CommandGetLocationCoordinates commandGetLocationCoordinates =
            new CommandGetLocationCoordinates(this);

    public ModuleLocation()
    {
        this.titleRes = R.string.control_module_title_location;
        this.descriptionRes = R.string.control_module_desc_location;
        this.iconRes = R.drawable.ic_location_on_grey_700_36dp;

        this.requiredPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        };
    }
}
