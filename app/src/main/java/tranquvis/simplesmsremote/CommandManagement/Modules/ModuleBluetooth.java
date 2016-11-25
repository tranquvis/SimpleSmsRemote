package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.Manifest;

import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetBluetoothState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetBluetoothState;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class ModuleBluetooth extends Module {
    public final CommandSetBluetoothState commandSetBluetoothState =
            new CommandSetBluetoothState(this);
    public final CommandGetBluetoothState commandGetBluetoothState =
            new CommandGetBluetoothState(this);

    public ModuleBluetooth() {
        this.titleRes = R.string.control_module_title_bluetooth;
        this.descriptionRes = R.string.control_module_desc_bluetooth;
        this.iconRes = R.drawable.ic_bluetooth_grey_700_36dp;

        this.requiredPermissions = new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        };
    }
}
