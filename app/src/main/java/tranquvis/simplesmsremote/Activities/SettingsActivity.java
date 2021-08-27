package tranquvis.simplesmsremote.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

import tranquvis.simplesmsremote.Data.AppDataManager;
import tranquvis.simplesmsremote.R;

public class SettingsActivity extends AppCompatActivity {
    private boolean saveOnStop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveUserData();
                saveOnStop = false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        if (saveOnStop) saveUserData();
        super.onStop();
    }

    private void saveUserData() {
        try {
            AppDataManager.getDefault().SaveUserData(this);
        } catch (IOException e) {
            Toast.makeText(this, R.string.alert_save_data_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
