package tranquvis.simplesmsremote.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.IOException;

import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;

public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStop()
    {
        try
        {
            DataManager.SaveUserData(this);
        } catch (IOException e)
        {
            Toast.makeText(this, R.string.alert_save_data_failed, Toast.LENGTH_SHORT).show();
        }
        super.onStop();
    }
}
