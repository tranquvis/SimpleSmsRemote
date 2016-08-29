package tranquvis.simplesmsremote.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

import tranquvis.simplesmsremote.Adapters.ManageControlActionsListAdapter;
import tranquvis.simplesmsremote.ControlModule;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{

    ListView listView;
    ManageControlActionsListAdapter listAdapter;

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
        listAdapter = new ManageControlActionsListAdapter(this,
                ControlModule.getAllControlActions());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
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
        Intent intent = new Intent(this, ConfigureControlActionActivity.class);
        intent.putExtra("controlActionId", listAdapter.getItem(i).getId());
        startActivity(intent);
    }
}
