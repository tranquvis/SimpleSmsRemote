package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.Manifest;
import android.os.Build;

import tranquvis.simplesmsremote.CommandManagement.Commands.CommandPlaySound;
import tranquvis.simplesmsremote.R;

public class ModulePlaySound
        extends Module {
    public final CommandPlaySound commandPlaySound = new CommandPlaySound(this);

    public ModulePlaySound() {
        this.titleRes = R.string.control_module_title_play_sound;
        this.descriptionRes = R.string.control_module_desc_play_sound;
        this.iconRes = R.drawable.baseline_play_circle_outline_grey_700_36dp;
        this.paramInfoRes = R.string.control_module_param_desc_play_sound;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.requiredPermissions = new String[]{
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
            };
        }
    }
}
