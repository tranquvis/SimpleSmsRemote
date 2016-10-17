package tranquvis.simplesmsremote.Activities.ModuleActivities;

import android.os.Bundle;

import tranquvis.simplesmsremote.Activities.ConfigureControlModuleActivity;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 17.10.2016.
 */

public class CameraModuleActivity extends ConfigureControlModuleActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setSettingsContentLayout(R.layout.content_module_settings_camera);
    }
}
