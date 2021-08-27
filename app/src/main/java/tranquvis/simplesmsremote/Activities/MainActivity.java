package tranquvis.simplesmsremote.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import tranquvis.simplesmsremote.Adapters.ManageControlModulesListAdapter;
import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.Data.AppDataManager;
import tranquvis.simplesmsremote.Data.UserSettings;
import tranquvis.simplesmsremote.Helper.HelpOverlay;
import tranquvis.simplesmsremote.Helper.MyNotificationManager;
import tranquvis.simplesmsremote.Listeners.OnSwipeTouchListener;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Services.SMSReceiverService;
import tranquvis.simplesmsremote.Utils.PermissionUtils;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final int CODE_PERM_REQUEST_SMS = 1;
    private final String TAG = getClass().getName();

    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private ListView listView;
    private ManageControlModulesListAdapter listAdapter;

    private boolean startReceiverAfterPermRequest;
    private Thread receiverStatusUpdateThread;

    private FloatingActionButton receiverChangeStateFab;
    private TextView receiverLifeInfoTextView;

    private View helpOverlayView;
    private boolean showHelpOverlay = false;
    private HelpOverlay helpOverlay;
    private int helpViewPos = -1;

    private TextView helpInfoTitleTextView;
    private TextView helpInfoDescTextView;
    private Button helpNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyNotificationManager.getInstance(this).initChannels();
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //load user data
        try {
            AppDataManager.getDefault().LoadUserData(this);
        } catch (IOException e) {
            Toast.makeText(this, R.string.alert_load_data_failed, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //save user data
        try {
            AppDataManager.getDefault().SaveUserData(this);
        } catch (IOException e) {
            Toast.makeText(this, R.string.alert_load_data_failed, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //region init list view
        List<Module> moduleList = Instances.GetAll(Module.GetDefaultComparator(this,
                AppDataManager.getDefault()));
        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ManageControlModulesListAdapter(this, moduleList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        //endregion

        //region init receiver status views
        receiverChangeStateFab = (FloatingActionButton) findViewById(R.id.fab_receiver_change_state);
        receiverChangeStateFab.setOnClickListener(this);
        receiverLifeInfoTextView = (TextView) findViewById(R.id.textView_receiver_life_info);
        //endregion

        startUpdatingReceiverStatusAsync();

        //region show help overlay on first start
        if (AppDataManager.getDefault().isFirstStart())
            showHelpOverlay = true;
        if (getIntent().getBooleanExtra("showHelpOverlay", false)) {
            showHelpOverlay = true;
            getIntent().removeExtra("showHelpOverlay");
        }

        if (showHelpOverlay) {
            helpInfoTitleTextView = (TextView) findViewById(R.id.textView_help_info_title);
            helpInfoDescTextView = (TextView) findViewById(R.id.textView_help_info_content);
            helpNextButton = (Button) findViewById(R.id.button_help_next);

            helpOverlayView = findViewById(R.id.layout_help_overlay);
            helpOverlayView.setVisibility(View.VISIBLE);
            helpOverlayView.setOnTouchListener(new OnSwipeTouchListener(this) {
                @Override
                public void onSwipeLeft() {
                    if (helpViewPos < helpOverlay.getHelpViewCount() - 1)
                        showNextHelpView();
                }

                @Override
                public void onSwipeRight() {
                    showPreviousHelpView();
                }
            });
            findViewById(R.id.layout_help_info).setVisibility(View.VISIBLE);
            helpOverlay = HelpOverlay.GetMainActivityOverlay();
            showNextHelpView();
            helpNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showNextHelpView();
                }
            });
            findViewById(R.id.button_help_abort).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recreate();
                }
            });
        } else {
            findViewById(R.id.layout_help_overlay).setVisibility(View.INVISIBLE);
        }
        //endregion
    }

    @Override
    public void onBackPressed() {
        if(showHelpOverlay)
            recreate();
        else
            super.onBackPressed();
    }

    /**
     * goto next step of help overlay
     */
    private void showNextHelpView() {
        helpViewPos++;
        HelpOverlay.View helpView = helpOverlay.getView(helpViewPos);
        if (helpView == null) {
            //goto how to control
            showHelpOverlay = false;
            helpOverlayView.setVisibility(View.GONE);
            startActivity(new Intent(this, HelpHowToControlActivity.class));
            return;
        }

        updateHelpView(helpView);
        if (helpViewPos > 0) {
            HelpOverlay.View previousHelpView = helpOverlay.getView(helpViewPos - 1);
            if (previousHelpView.getHintContainerResId() >= 0)
                findViewById(previousHelpView.getHintContainerResId())
                        .setVisibility(View.INVISIBLE);
        }
    }

    /**
     * goto previous step of help overlay
     */
    private void showPreviousHelpView() {
        if (helpViewPos - 1 < 0)
            return;
        helpViewPos--;
        HelpOverlay.View helpView = helpOverlay.getView(helpViewPos);

        updateHelpView(helpView);
        HelpOverlay.View previousHelpView = helpOverlay.getView(helpViewPos + 1);
        if (previousHelpView != null && previousHelpView.getHintContainerResId() >= 0)
            findViewById(previousHelpView.getHintContainerResId())
                    .setVisibility(View.INVISIBLE);
    }

    /**
     * update visible information of help overlay
     *
     * @param helpView current help view
     */
    private void updateHelpView(HelpOverlay.View helpView) {
        helpInfoTitleTextView.setText(helpView.getTitleRes());
        helpInfoDescTextView.setText(helpView.getDescRes());
        if (helpView.getHintContainerResId() >= 0)
            findViewById(helpView.getHintContainerResId()).setVisibility(View.VISIBLE);
        if (helpViewPos == 0) {
            //first
            helpNextButton.setText(R.string.help_take_a_tour);
        } else {
            if (helpViewPos == helpOverlay.getHelpViewCount() - 1) {
                //last help view
                helpNextButton.setText(R.string.help_how_to_control_title);
            } else
                helpNextButton.setText(R.string.help_next);

            if (helpView.getTitleRes() == R.string.help_other_title) {
                toolbar.showOverflowMenu();
            } else {
                toolbar.hideOverflowMenu();
            }
        }
    }

    /**
     * start updating receiver status in background until activity finishes
     */
    private void startUpdatingReceiverStatusAsync() {
        receiverStatusUpdateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!MainActivity.this.isFinishing()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateReceiverStatus();
                            }
                        });
                        Thread.sleep(2000);
                    }
                } catch (InterruptedException e) {
                }
            }
        });
        receiverStatusUpdateThread.start();
    }

    /**
     * Update all views, which show information about the receiver's status
     */
    private void updateReceiverStatus() {
        if (SMSReceiverService.isRunning(this)) {
            receiverChangeStateFab.setImageResource(R.drawable.ic_stop_white_24dp);

            //region get time string
            long elapsedTime = System.currentTimeMillis() -
                    SMSReceiverService.getStartTime(this).getTime();
            long days = TimeUnit.MILLISECONDS.toDays(elapsedTime);
            long hours = TimeUnit.MILLISECONDS.toHours(elapsedTime) - TimeUnit.DAYS.toHours(days);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) -
                    TimeUnit.DAYS.toMinutes(days) - TimeUnit.HOURS.toMinutes(hours);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) -
                    TimeUnit.DAYS.toSeconds(days) - TimeUnit.HOURS.toSeconds(hours) -
                    TimeUnit.MINUTES.toSeconds(minutes);
            String elapsedTimeStr = (days > 0 ? String.valueOf(days) + "d " : "")
                    + (days > 0 || hours > 0 ? String.valueOf(hours) + "h " : "")
                    + (days > 0 || hours > 0 || minutes > 0 ? String.valueOf(minutes) + "min " : "")
                    + String.valueOf(seconds) + "s";
            //endregion
            receiverLifeInfoTextView.setTextColor(getResources().getColor(R.color.colorSuccess));
            receiverLifeInfoTextView.setText(getResources().getString(
                    R.string.receiver_life_info_running, elapsedTimeStr));
        } else {
            receiverChangeStateFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);

            receiverLifeInfoTextView.setTextColor(getResources().getColor(R.color.colorError));
            receiverLifeInfoTextView.setText(R.string.receiver_life_info_not_running);
        }
    }

    private void startSMSReceiverService() {
        if (!PermissionUtils.AppHasPermissions(this, new String[]{
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS
        })) {
            startReceiverAfterPermRequest = true;
            PermissionUtils.RequestCommonPermissions(this,
                    new String[]{
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.SEND_SMS
                    }, CODE_PERM_REQUEST_SMS);
            return;
        }

        UserSettings userSettings = AppDataManager.getDefault().getUserData().getUserSettings();
        SMSReceiverService.start(getBaseContext(), userSettings.isReceiverStartForeground());
        updateReceiverStatus();
    }

    private void stopSMSReceiverService() {
        SMSReceiverService.stop(getBaseContext());
        updateReceiverStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (showHelpOverlay)
            return false;

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_log:
                startActivity(new Intent(this, LogActivity.class));
                return true;
            case R.id.action_help_tour:
                Intent intent = new Intent(this, this.getClass());
                intent.putExtra("showHelpOverlay", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.action_help_how_to_control:
                startActivity(new Intent(this, HelpHowToControlActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Module module = listAdapter.getItem(i);
        if (module == null)
            return;

        Intent intent = new Intent(this, module.getConfigurationActivityType());
        intent.putExtra("controlActionId", module.getId());
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_PERM_REQUEST_SMS) {
            boolean permissionGranted = grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (permissionGranted) {
                if (startReceiverAfterPermRequest) {
                    startSMSReceiverService();
                    startReceiverAfterPermRequest = false;
                }
            } else {
                Snackbar.make(coordinatorLayout, R.string.permission_receive_sms_not_granted,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.simple_request_again, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtils.RequestCommonPermissions(MainActivity.this,
                                        new String[]{
                                                Manifest.permission.RECEIVE_SMS,
                                                Manifest.permission.SEND_SMS
                                        },
                                        CODE_PERM_REQUEST_SMS);
                            }
                        })
                        .show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_receiver_change_state) {
            if (SMSReceiverService.isRunning(this))
                stopSMSReceiverService();
            else
                startSMSReceiverService();
        }
    }
}
