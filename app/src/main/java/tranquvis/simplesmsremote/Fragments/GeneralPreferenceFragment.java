package tranquvis.simplesmsremote.Fragments;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GeneralPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onResume()
    {
        SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
        preferences.registerOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onPause()
    {
        SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        savePreference(key);
    }

    private void savePreference(String key)
    {
        Preference pref = findPreference(key);
        switch (key)
        {
            case "pref_switch_receiver_autostart":
                DataManager.getUserData().getUserSettings().setStartReceiverOnSystemStart(
                        ((SwitchPreference)pref).isChecked());
                break;
            case "pref_switch_notify_sms_commands_executed":
                DataManager.getUserData().getUserSettings().setNotifyCommandsExecuted(
                        ((SwitchPreference)pref).isChecked());
                break;
        }
    }
}