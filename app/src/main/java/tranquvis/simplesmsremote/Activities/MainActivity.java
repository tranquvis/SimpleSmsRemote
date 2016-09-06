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
import android.test.suitebuilder.TestMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import tranquvis.simplesmsremote.Adapters.ManageControlModulesListAdapter;
import tranquvis.simplesmsremote.ControlModule;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Helper.MobileDataHelper;
import tranquvis.simplesmsremote.Helper.PermissionHelper;
import tranquvis.simplesmsremote.MyNotificationManager;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Services.SmsReceiver.SMSReceiverService;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener
{
    private static final int CODE_PERM_REQUEST_RECEIVE_SMS = 1;

    CoordinatorLayout coordinatorLayout;
    ListView listView;
    ManageControlModulesListAdapter listAdapter;

    boolean startReceiverAfterPermRequest;
    Thread receiverStatusUpdateThread;

    FloatingActionButton receiverChangeStateFab;
    TextView receiverLifeInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            DataManager.LoadUserData(this);
        } catch (IOException e) {
            Toast.makeText(this, R.string.alert_load_data_failed, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
            DataManager.SaveUserData(this);
        } catch (IOException e) {
            Toast.makeText(this, R.string.alert_load_data_failed, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ManageControlModulesListAdapter(this,
                ControlModule.getAllControlActions());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);

        receiverChangeStateFab = (FloatingActionButton) findViewById(R.id.fab_receiver_change_state);
        receiverChangeStateFab.setOnClickListener(this);
        receiverLifeInfoTextView = (TextView) findViewById(R.id.textView_receiver_life_info);

        startUpdatingReceiverStatusAsync();

        if(DataManager.isFirstStart())
        {
            //TODO
        }
    }

    private void startUpdatingReceiverStatusAsync()
    {
        receiverStatusUpdateThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    while(true)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                updateReceiverStatus();
                            }
                        });
                        Thread.sleep(2000);
                    }
                } catch (InterruptedException e)
                {
                    return;
                }
            }
        });
        receiverStatusUpdateThread.start();
    }

    private void updateReceiverStatus()
    {
        if (SMSReceiverService.isRunning(this))
        {
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
            String elaspedTimeStr = (days > 0 ? String.valueOf(days) + "d " : "")
                    + (days > 0 || hours > 0 ? String.valueOf(hours) + "h " : "")
                    + (days > 0 || hours > 0 || minutes > 0 ? String.valueOf(minutes) + "min " : "")
                    + String.valueOf(seconds) + "s";
            //endregion
            receiverLifeInfoTextView.setTextColor(getResources().getColor(R.color.colorSuccess));
            receiverLifeInfoTextView.setText(getResources().getString(
                    R.string.receiver_life_info_running, elaspedTimeStr));
        }
        else
        {
            receiverChangeStateFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);

            receiverLifeInfoTextView.setTextColor(getResources().getColor(R.color.colorError));
            receiverLifeInfoTextView.setText(R.string.receiver_life_info_not_running);
        }
    }

    private void startSMSReceiverService()
    {
        if(!PermissionHelper.AppHasPermission(this, Manifest.permission.RECEIVE_SMS))
        {
            startReceiverAfterPermRequest = true;
            PermissionHelper.RequestCommonPermissions(this,
                    new String[]{ Manifest.permission.RECEIVE_SMS}, CODE_PERM_REQUEST_RECEIVE_SMS);
            return;
        }
        SMSReceiverService.start(this);
        updateReceiverStatus();
    }

    private void stopSMSReceiverService()
    {
        SMSReceiverService.stop(this);
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

        switch (item.getItemId())
        {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_log:
                startActivity(new Intent(this, LogActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Intent intent = new Intent(this, ConfigureControlModuleActivity.class);
        intent.putExtra("controlActionId", listAdapter.getItem(i).getId());
        startActivity(intent);
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case CODE_PERM_REQUEST_RECEIVE_SMS:
                boolean permissionGranted = grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(permissionGranted)
                {
                    if(startReceiverAfterPermRequest)
                    {
                        startSMSReceiverService();
                        startReceiverAfterPermRequest = false;
                    }
                }
                else
                {
                    Snackbar.make(coordinatorLayout, R.string.permission_receive_sms_not_granted,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.simple_request_again, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PermissionHelper.RequestCommonPermissions(MainActivity.this,
                                            new String[]{ Manifest.permission.RECEIVE_SMS},
                                            CODE_PERM_REQUEST_RECEIVE_SMS);
                                }
                            })
                            .show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.fab_receiver_change_state:
                if(SMSReceiverService.isRunning(this))
                    stopSMSReceiverService();
                else
                    startSMSReceiverService();
                break;
        }
    }
}
