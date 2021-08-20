package tranquvis.simplesmsremote.CommandManagement.Modules;

import tranquvis.simplesmsremote.Activities.ModuleActivities.GrantPhoneRemotelyModuleActivity;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGrantPhoneRemotely;
import tranquvis.simplesmsremote.R;

public class ModuleGrantPhoneRemotely extends Module {
    public final CommandGrantPhoneRemotely commandGrantPhoneRemotely = new CommandGrantPhoneRemotely(this);

    public ModuleGrantPhoneRemotely() {
        this.titleRes = R.string.control_module_title_grant_phone_remotely;
        this.descriptionRes = R.string.control_module_desc_grant_phone_remotely;
        this.iconRes = R.drawable.ic_baseline_lock_36;
        this.paramInfoRes = R.string.control_module_param_desc_grant_phone_remotely;
        this.configurationActivityType = GrantPhoneRemotelyModuleActivity.class;
    }
}
