package tranquvis.simplesmsremote.Activities;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.Adapters.GrantedPhonesEditableListAdapter;
import tranquvis.simplesmsremote.ControlModule;
import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Helper.PermissionHelper;
import tranquvis.simplesmsremote.R;

public class ConfigureControlModuleActivity extends AppCompatActivity implements View.OnClickListener
{
    ControlModule controlModule;
    List<String> grantedPhones;
    boolean isModuleEnabled;
    boolean saveOnStop = true;

    String[] remainingPermissionRequests;
    String[] lastPermissionRequests;
    boolean processPermissionRequestOnResume = false;
    int CODE_PERMISSION_REQUEST = 1;

    ListView grantedPhonesListView;
    GrantedPhonesEditableListAdapter grantedPhonesListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_control_module);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Resources res = getResources();

        String controlModuleId = getIntent().getStringExtra("controlActionId");
        controlModule = ControlModule.getFromId(controlModuleId);
        if(controlModule == null)
        {
            finish();
            return;
        }
        ControlModuleUserData userData = controlModule.getUserData();
        isModuleEnabled = userData != null;

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

        Button buttonChangeEnabled = (Button)findViewById(R.id.button_change_enabled);
        buttonChangeEnabled.setText(!isModuleEnabled ? R.string.enable_module
                : R.string.disable_module);
        buttonChangeEnabled.setOnClickListener(this);
        if(!controlModule.isCompatible())
            buttonChangeEnabled.setEnabled(false);

        if(isModuleEnabled)
        {
            grantedPhones = userData.getGrantedPhones();
            if (grantedPhones.isEmpty())
                grantedPhones.add("");

            grantedPhonesListView = (ListView) findViewById(R.id.listView_granted_phones);
            grantedPhonesListAdapter = new GrantedPhonesEditableListAdapter(this, grantedPhones,
                    grantedPhonesListView);
            grantedPhonesListView.setScrollContainer(false);
            grantedPhonesListView.setAdapter(grantedPhonesListAdapter);

            FloatingActionButton addPhoneFab = (FloatingActionButton) findViewById(R.id.fab_add_phone);
            addPhoneFab.setOnClickListener(this);
        }
        else
        {
            findViewById(R.id.layout_user_data).setVisibility(View.GONE);
            findViewById(R.id.textView_user_data_title).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                saveUserData();
                saveOnStop = false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_change_enabled:
                if(isModuleEnabled)
                    disableModule();
                else
                    enableModule();
                break;
            case R.id.fab_add_phone:
                grantedPhonesListAdapter.addPhone("");
                break;
        }
    }

    private void enableModule()
    {
        if (!PermissionHelper.AppHasPermissions(this,
                controlModule.getRequiredPermissions(this)))
            requestPermissions(controlModule.getRequiredPermissions(this));
        else
        {
            saveUserData();
            recreate();
        }
    }

    private void disableModule()
    {
        new AlertDialog.Builder(this)
                .setMessage(R.string.alert_sure_to_disable_module)
                .setNegativeButton(R.string.simple_no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){}
                        })
                .setPositiveButton(R.string.simple_yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                DataManager.getUserData().removeControlModule(
                                        controlModule.getId());
                                isModuleEnabled = false;
                                try
                                {
                                    DataManager.SaveUserData(ConfigureControlModuleActivity.this);
                                    Toast.makeText(ConfigureControlModuleActivity.this,
                                            R.string.control_module_disabled_successful,
                                            Toast.LENGTH_SHORT).show();
                                } catch (IOException e){
                                    Toast.makeText(ConfigureControlModuleActivity.this,
                                            R.string.alert_save_data_failed,
                                            Toast.LENGTH_SHORT).show();
                                }

                                recreate();
                            }
                        })
                .show();
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
                enableModule();
            }
        }
        else
        {
            Toast.makeText(this, R.string.permissions_denied, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop()
    {
        if(isModuleEnabled && saveOnStop) saveUserData();
        super.onStop();
    }

    private void saveUserData()
    {
        if(isModuleEnabled) {
            grantedPhonesListAdapter.updateData();
            List<String> filteredPhones = new ArrayList<>();
            for(String phone : grantedPhones)
            {
                phone = phone.trim();
                if(!phone.isEmpty() && !filteredPhones.contains(phone))
                    filteredPhones.add(phone);
            }
            DataManager.getUserData().setControlModule(new ControlModuleUserData(
                    controlModule.getId(), grantedPhones));
        }
        else
            DataManager.getUserData().addControlModule(new ControlModuleUserData(
                    controlModule.getId(), new ArrayList<String>()));

        try
        {
            DataManager.SaveUserData(this);
            if(!isModuleEnabled)
                Toast.makeText(this, R.string.control_module_enabled_successful, Toast.LENGTH_SHORT)
                    .show();
        } catch (IOException e)
        {
            Toast.makeText(this, R.string.alert_save_data_failed,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
