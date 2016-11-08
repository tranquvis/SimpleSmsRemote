package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.Manifest;
import android.os.Build;

import tranquvis.simplesmsremote.Activities.ModuleActivities.CameraModuleActivity;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTakePicture;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class ModuleCamera extends Module
{
    public final CommandTakePicture commandTakePicture =
            new CommandTakePicture(this);

    public ModuleCamera()
    {
        this.configurationActivityType = CameraModuleActivity.class;
        this.titleRes = R.string.control_module_title_camera;
        this.descriptionRes = R.string.control_module_desc_camera;
        this.iconRes = R.drawable.ic_camera_grey_700_36dp;
        this.paramInfoRes = R.string.control_module_param_desc_camera;

        this.requiredPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        this.sdkMin = Build.VERSION_CODES.LOLLIPOP;
    }
}
