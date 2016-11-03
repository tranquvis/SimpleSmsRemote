package tranquvis.simplesmsremote.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.Adapters.CommandSyntaxDescListAdapter;
import tranquvis.simplesmsremote.Adapters.GrantedPhonesEditableListAdapter;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.ModuleSettingsData;
import tranquvis.simplesmsremote.Utils.PermissionUtils;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.UI.UIUtils;

public class ConfigureControlModuleActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final int REQUEST_CODE_PERM_MODULE_REQUIREMENTS = 1;

    private Module module;
    private ControlModuleUserData userData;
    protected ModuleSettingsData moduleSettings;
    private List<String> grantedPhones;
    private boolean isModuleEnabled;
    private boolean saveOnStop = true;

    private String[] remainingPermissionRequests;
    private String[] lastPermissionRequests;
    private boolean processPermissionRequestOnResume = false;

    private ListView grantedPhonesListView;
    private GrantedPhonesEditableListAdapter grantedPhonesListAdapter;

    private CoordinatorLayout coordinatorLayout;
    private ViewStub settingsViewStub;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_control_module);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Resources res = getResources();

        String controlModuleId = getIntent().getStringExtra("controlActionId");
        module = Module.getFromId(controlModuleId);
        if(module == null)
        {
            finish();
            return;
        }
        userData = module.getUserData();
        isModuleEnabled = module.isEnabled();

        toolbar.setTitle(R.string.title_activity_configure_control_action);

        if(module.getTitleRes() != -1)
        {
            toolbar.setSubtitle(module.getTitleRes());
        }

        if(module.getDescriptionRes() != -1)
        {
            ((TextView) findViewById(R.id.textView_description)).setText(
                    module.getDescriptionRes());
        }

        ListView commandsListView = (ListView) findViewById(R.id.listView_commands);
        CommandSyntaxDescListAdapter commandsListAdapter = new CommandSyntaxDescListAdapter(this,
                module.getCommands());
        commandsListView.setAdapter(commandsListAdapter);
        UIUtils.SetListViewHeightBasedOnItems(commandsListView);

        if(module.getParamInfoRes() != -1)
        {
            ((TextView)findViewById(R.id.textView_command_parameter_info))
                    .setText(module.getParamInfoRes());
        }
        else
        {
            findViewById(R.id.textView_command_parameter_info_title).setVisibility(View.GONE);
            findViewById(R.id.textView_command_parameter_info).setVisibility(View.GONE);
        }

        TextView compatibilityTextView = (TextView)findViewById(R.id.textView_compatibility_info);
        Button buttonChangeEnabled = (Button)findViewById(R.id.button_change_enabled);

        buttonChangeEnabled.setText(!isModuleEnabled ? R.string.enable_module
                : R.string.disable_module);

        findViewById(R.id.imageButton_command_info).setOnClickListener(this);

        if(module.isCompatible())
        {
            compatibilityTextView.setText(R.string.compatible);
            compatibilityTextView.setTextColor(res.getColor(R.color.colorSuccess));
            buttonChangeEnabled.setOnClickListener(this);
        }
        else
        {
            compatibilityTextView.setText(R.string.incompatible);
            compatibilityTextView.setTextColor(res.getColor(R.color.colorError));
            buttonChangeEnabled.setEnabled(false);
        }

        if(isModuleEnabled)
        {
            findViewById(R.id.card_user_settings).setVisibility(View.VISIBLE);
            findViewById(R.id.textView_user_settings_title).setVisibility(View.VISIBLE);

            grantedPhones = userData.getGrantedPhones();
            if (grantedPhones.isEmpty())
                grantedPhones.add("");

            grantedPhonesListView = (ListView) findViewById(R.id.listView_granted_phones);
            grantedPhonesListAdapter = new GrantedPhonesEditableListAdapter(this, grantedPhones,
                    grantedPhonesListView);
            grantedPhonesListView.setScrollContainer(false);
            grantedPhonesListView.setAdapter(grantedPhonesListAdapter);
            UIUtils.SetListViewHeightBasedOnItems(grantedPhonesListView);

            FloatingActionButton addPhoneFab = (FloatingActionButton) findViewById(R.id.fab_add_phone);
            addPhoneFab.setOnClickListener(this);

            moduleSettings = userData.getSettings();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if(isModuleEnabled && saveOnStop)
                {
                    saveUserData();
                    saveOnStop = false;
                }
                break;
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
            case R.id.imageButton_command_info:
                startActivity(new Intent(this, HelpHowToControlActivity.class));
                break;
        }
    }

    private void enableModule()
    {
        if (!PermissionUtils.AppHasPermissions(this,
                module.getRequiredPermissions(this)))
            requestPermissions(module.getRequiredPermissions(this));
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
                                        module.getId());
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
        PermissionUtils.RequestResult result = PermissionUtils.RequestNextPermissions(this,
                permissions, REQUEST_CODE_PERM_MODULE_REQUIREMENTS);
        remainingPermissionRequests = result.getRemainingPermissions();
        lastPermissionRequests = result.getRequestPermissions();
        if(result.getRequestType() == PermissionUtils.RequestType.INDEPENDENT_ACTIVITY)
            processPermissionRequestOnResume = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_PERM_MODULE_REQUIREMENTS:
                onModuleRequiredPermissionRequestFinished();
                break;
        }
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();

        if(processPermissionRequestOnResume)
        {
            processPermissionRequestOnResume = false;
            onModuleRequiredPermissionRequestFinished();
        }
    }

    private void onModuleRequiredPermissionRequestFinished()
    {
        if(PermissionUtils.AppHasPermissions(this, lastPermissionRequests))
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
            Snackbar.make(coordinatorLayout, R.string.permissions_denied, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop()
    {
        if(isModuleEnabled && saveOnStop)
            saveUserData();
        super.onStop();
    }

    private void saveUserData()
    {
        if(isModuleEnabled) {
            updateGrantedPhones();
            updateModuleSettings();

            DataManager.getUserData().setControlModule(new ControlModuleUserData(
                    module.getId(), grantedPhones, moduleSettings));
        }
        else
            DataManager.getUserData().addControlModule(new ControlModuleUserData(
                    module.getId(), new ArrayList<String>(), moduleSettings));

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

    protected void setSettingsContentLayout(int layoutId)
    {
        findViewById(R.id.card_module_settings).setVisibility(View.VISIBLE);
        findViewById(R.id.textView_module_settings_title).setVisibility(View.VISIBLE);

        ViewStub settingsViewStub = (ViewStub) findViewById(R.id.viewStub_settings_content);
        settingsViewStub.setLayoutResource(layoutId);
        settingsViewStub.inflate();
    }

    private void updateGrantedPhones()
    {
        grantedPhonesListAdapter.updateData();
        List<String> filteredPhones = new ArrayList<>();
        for(String phone : grantedPhones)
        {
            phone = phone.trim();
            if(!phone.isEmpty() && !filteredPhones.contains(phone))
                filteredPhones.add(phone);
        }
        grantedPhones.clear();
        grantedPhones.addAll(filteredPhones);
    }

    protected void updateModuleSettings()
    {

    }

    protected CoordinatorLayout getCoordinatorLayout()
    {
        return coordinatorLayout;
    }

    protected Module getModule()
    {
        return module;
    }

    protected ControlModuleUserData getUserData()
    {
        return userData;
    }
}
