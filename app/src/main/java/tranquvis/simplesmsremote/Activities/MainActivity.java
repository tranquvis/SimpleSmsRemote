package tranquvis.simplesmsremote.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import tranquvis.simplesmsremote.Adapters.ManageControlModulesListAdapter;
import tranquvis.simplesmsremote.ControlModule;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Helper.PermissionHelper;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.ReceiverService.ReceiverStatus;
import tranquvis.simplesmsremote.ReceiverService.SMSReceiverService;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener
{
    private static final int CODE_PERM_REQUEST_RECEIVE_SMS = 1;

    ListView listView;
    ManageControlModulesListAdapter listAdapter;

    boolean startReceiverAfterPermRequest;
    Thread receiverStatusUpdateThread;

    FloatingActionButton receiverChangeStateFab;
    FrameLayout receiverStatusIndicatorLayout;
    TextView receiverLifeInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            DataManager.LoadData(this);
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
        receiverStatusIndicatorLayout = (FrameLayout)
                findViewById(R.id.frameLayout_receiver_status_indicator);
        receiverLifeInfoTextView = (TextView) findViewById(R.id.textView_receiver_life_info);

        startUpdatingReceiverStatusAsync();
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
        if (SMSReceiverService.isRunning())
        {
            receiverStatusIndicatorLayout.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
            receiverChangeStateFab.setImageResource(R.drawable.ic_stop_white_24dp);
        }
        else
        {
            receiverStatusIndicatorLayout.setBackgroundColor(getResources().getColor(R.color.colorError));
            receiverChangeStateFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
                    Toast.makeText(this, R.string.permission_receive_sms_not_granted,
                            Toast.LENGTH_LONG).show();
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
                if(SMSReceiverService.isRunning())
                    stopSMSReceiverService();
                else
                    startSMSReceiverService();
                break;
        }
    }
}
