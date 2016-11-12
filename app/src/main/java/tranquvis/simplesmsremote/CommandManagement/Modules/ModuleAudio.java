package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.Manifest;
import android.os.Build;

import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetAudioVolume;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetAudioVolume;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class ModuleAudio extends Module
{
    public final CommandSetAudioVolume commandSetAudioVolume = new CommandSetAudioVolume(this);
    public final CommandGetAudioVolume commandGetAudioVolume = new CommandGetAudioVolume(this);

    public ModuleAudio()
    {
        this.titleRes = R.string.control_module_title_audio;
        this.descriptionRes = R.string.control_module_desc_audio;
        this.iconRes = R.drawable.ic_volume_up_grey_700_36dp;
        this.paramInfoRes = R.string.control_module_param_desc_audio;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.requiredPermissions = new String[]{
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
            };
        }
    }
}
