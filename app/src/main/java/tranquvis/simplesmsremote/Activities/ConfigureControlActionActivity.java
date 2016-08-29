package tranquvis.simplesmsremote.Activities;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import tranquvis.simplesmsremote.ControlModule;
import tranquvis.simplesmsremote.Helper.PermissionHelper;
import tranquvis.simplesmsremote.R;

public class ConfigureControlActionActivity extends AppCompatActivity implements View.OnClickListener
{
    ControlModule controlModule;

    String[] remainingPermissionRequests;
    String[] lastPermissionRequests;
    boolean processPermissionRequestOnResume = false;
    int CODE_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_control_module);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Resources res = getResources();

        String controlActionId = getIntent().getStringExtra("controlActionId");
        controlModule = ControlModule.getFromId(controlActionId);
        if(controlModule == null)
        {
            finish();
            return;
        }

        toolbar.setTitle(R.string.title_activity_configure_control_action);
        toolbar.setSubtitle(controlModule.getTitleRes());

        ((TextView)findViewById(R.id.textView_description)).setText(
                controlModule.getDescriptionRes());
        ((TextView)findViewById(R.id.textView_commands)).setText(controlModule.getCommandsString());

        TextView compatibilityTextView = (TextView)findViewById(R.id.textView_compatibility_info);
        if(controlModule.isCompatible())
        {
            compatibilityTextView.setText(R.string.compatible);
            compatibilityTextView.setTextColor(res.getColor(R.color.colorSuccess));
        }
        else
        {
            compatibilityTextView.setText(R.string.incompatible);
            compatibilityTextView.setTextColor(res.getColor(R.color.colorError));
        }

        Button buttonSubmit = (Button)findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(this);
        if(controlModule.getUserData() != null)
        {
            buttonSubmit.setText(R.string.save);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_submit:
                if(!PermissionHelper.AppHasPermissions(this,
                        controlModule.getRequiredPermissions(this)))
                    requestPermissions(controlModule.getRequiredPermissions(this));
                else
                {
                    finish();
                }
                break;
        }
    }

    private void requestPermissions(String[] permissions)
    {
        PermissionHelper.RequestResult result = PermissionHelper.RequestNextPermissions(this,
                permissions, CODE_PERMISSION_REQUEST);
        remainingPermissionRequests = result.getRemainingPermissions();
        lastPermissionRequests = result.getRequestPermissions();
        if(result.getRequestType() == PermissionHelper.RequestType.INDEPENDENT_ACTIVITY)
            processPermissionRequestOnResume = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        onPermissionRequestFinished();
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();

        if(processPermissionRequestOnResume)
        {
            processPermissionRequestOnResume = false;
            onPermissionRequestFinished();
        }
    }

    private void onPermissionRequestFinished()
    {
        if(PermissionHelper.AppHasPermissions(this, lastPermissionRequests))
        {
            if(remainingPermissionRequests != null && remainingPermissionRequests.length > 0)
                requestPermissions(remainingPermissionRequests);
            else
            {
                //all permissions granted
                Toast.makeText(this, R.string.control_module_enabled_successful, Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        }
        else
        {
            Toast.makeText(this, R.string.permissions_denied, Toast.LENGTH_SHORT).show();
        }
    }
}
