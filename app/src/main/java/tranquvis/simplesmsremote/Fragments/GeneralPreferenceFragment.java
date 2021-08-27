package tranquvis.simplesmsremote.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;

import tranquvis.simplesmsremote.Data.AppDataManager;
import tranquvis.simplesmsremote.Data.UserSettings;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.PermissionUtils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GeneralPreferenceFragment
        extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String KEY_RECEIVER_AUTOSTART = "pref_switch_receiver_autostart";
    private static final String KEY_NOTIFY_COMMANDS_EXECUTED =
            "pref_switch_notify_sms_commands_executed";
    private static final String KEY_REPLY_WITH_RESULT = "pref_switch_reply_with_result";
    private static final String KEY_RECEIVER_START_FOREGROUND =
            "pref_switch_receiver_start_foreground";

    private static final int RESULT_CODE_PERM_REQUEST_FOR_REPLY_WITH_RESULT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        UserSettings userSettings = AppDataManager.getDefault().getUserData().getUserSettings();

        ((SwitchPreference) findPreference(KEY_RECEIVER_AUTOSTART)).setChecked(
                userSettings.isStartReceiverOnSystemStart());
        ((SwitchPreference) findPreference(KEY_NOTIFY_COMMANDS_EXECUTED)).setChecked(
                userSettings.isNotifyCommandsExecuted());
        ((SwitchPreference) findPreference(KEY_REPLY_WITH_RESULT)).setChecked(
                userSettings.isReplyWithResult());
    }


    @Override
    public void onResume() {
        SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
        preferences.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        savePreference(key);
    }

    private void savePreference(String key) {
        Preference pref = findPreference(key);
        switch (key) {
            case KEY_RECEIVER_AUTOSTART:
                AppDataManager.getDefault().getUserData().getUserSettings()
                        .setStartReceiverOnSystemStart(
                                ((SwitchPreference) pref).isChecked());
                break;
            case KEY_NOTIFY_COMMANDS_EXECUTED:
                AppDataManager.getDefault().getUserData().getUserSettings()
                        .setNotifyCommandsExecuted(
                                ((SwitchPreference) pref).isChecked());
                break;
            case KEY_REPLY_WITH_RESULT:
                if (((SwitchPreference) pref).isChecked()) {
                    if (PermissionUtils
                            .AppHasPermission(getActivity(), Manifest.permission.SEND_SMS))
                        AppDataManager.getDefault().getUserData().getUserSettings()
                                .setReplyWithResult(true);
                    else
                        PermissionUtils.RequestCommonPermissions(this,
                                new String[]{Manifest.permission.SEND_SMS},
                                RESULT_CODE_PERM_REQUEST_FOR_REPLY_WITH_RESULT);
                } else
                    AppDataManager.getDefault().getUserData().getUserSettings()
                            .setReplyWithResult(false);
                break;
            case KEY_RECEIVER_START_FOREGROUND:
                AppDataManager.getDefault().getUserData().getUserSettings()
                        .setReceiverStartForeground(
                                ((SwitchPreference) pref).isChecked());
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == RESULT_CODE_PERM_REQUEST_FOR_REPLY_WITH_RESULT) {
            boolean permissionGranted = grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (permissionGranted)
                AppDataManager.getDefault().getUserData().getUserSettings()
                        .setReplyWithResult(true);
            else
                ((SwitchPreference) findPreference(KEY_REPLY_WITH_RESULT)).setChecked(false);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}