package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.Manifest;
import android.os.Build;

import tranquvis.simplesmsremote.Activities.ModuleActivities.AuthenticationModuleActivity;
import tranquvis.simplesmsremote.Activities.ModuleActivities.CameraModuleActivity;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandAuth;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGetAudioVolume;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetAudioVolume;
import tranquvis.simplesmsremote.R;

public class ModuleAuthentication extends Module {
    public final CommandAuth commandAuth = new CommandAuth(this);

    public ModuleAuthentication() {
        this.titleRes = R.string.control_module_title_authentication;
        this.descriptionRes = R.string.control_module_desc_authentication;
        this.iconRes = R.drawable.ic_baseline_lock_36;
        this.paramInfoRes = R.string.control_module_param_desc_authentication;
        this.configurationActivityType = AuthenticationModuleActivity.class;
    }
}
