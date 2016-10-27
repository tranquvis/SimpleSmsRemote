package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.Manifest;
import android.os.Build;

import tranquvis.simplesmsremote.CommandManagement.Command;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetMobileDataState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetWifiState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetMobileDataState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetWifiState;
import tranquvis.simplesmsremote.CommandManagement.Module;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class ModuleMobileData extends Module
{
    public final CommandSetMobileDataState commandSetMobileDataState =
            new CommandSetMobileDataState(this);
    public final CommandGetMobileDataState commandGetMobileDataState =
            new CommandGetMobileDataState(this);

    ModuleMobileData()
    {
        this.sdkMax = Build.VERSION_CODES.LOLLIPOP;
        this.requiredPermissions = new String[]{
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        this.titleRes = R.string.control_module_title_mobile_data;
        this.descriptionRes = R.string.control_module_desc_mobile_data;
        this.iconRes = R.drawable.ic_network_cell_grey_700_36dp;
    }
}
