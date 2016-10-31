package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.Manifest;
import android.os.Build;

import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetAudioVolume;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetDisplayBrightness;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetAudioVolume;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetBluetoothState;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetDisplayBrightness;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class ModuleDisplay extends Module
{
    public final CommandGetDisplayBrightness getDisplayBrightness =
            new CommandGetDisplayBrightness(this);
    public final CommandSetDisplayBrightness setDisplayBrightness =
            new CommandSetDisplayBrightness(this);

    public ModuleDisplay()
    {
        this.titleRes = R.string.control_module_title_display;
        this.descriptionRes = R.string.control_module_desc_display;
        this.iconRes = R.drawable.ic_settings_brightness_grey_700_36dp;
        this.paramInfoRes = R.string.control_module_param_desc_display;

        this.requiredPermissions = new String[]{
                Manifest.permission.WRITE_SETTINGS
        };
    }
}
