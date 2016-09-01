package tranquvis.simplesmsremote.Fragments;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import java.io.IOException;

import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.UserSettings;
import tranquvis.simplesmsremote.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GeneralPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String KEY_RECEIVER_AUTOSTART = "pref_switch_receiver_autostart";
    private static final String KEY_NOTIFY_COMMANDS_EXECUTED =
            "pref_switch_notify_sms_commands_executed";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        UserSettings userSettings = DataManager.getUserData().getUserSettings();

        ((SwitchPreference)findPreference(KEY_RECEIVER_AUTOSTART)).setChecked(
                userSettings.isStartReceiverOnSystemStart());
        ((SwitchPreference)findPreference(KEY_NOTIFY_COMMANDS_EXECUTED)).setChecked(
                userSettings.isNotifyCommandsExecuted());
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
            case KEY_RECEIVER_AUTOSTART:
                DataManager.getUserData().getUserSettings().setStartReceiverOnSystemStart(
                        ((SwitchPreference)pref).isChecked());
                break;
            case KEY_NOTIFY_COMMANDS_EXECUTED:
                DataManager.getUserData().getUserSettings().setNotifyCommandsExecuted(
                        ((SwitchPreference)pref).isChecked());
                break;
        }
    }
}