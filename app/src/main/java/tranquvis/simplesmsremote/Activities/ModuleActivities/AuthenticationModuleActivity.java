package tranquvis.simplesmsremote.Activities.ModuleActivities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import tranquvis.simplesmsremote.Activities.ModuleActivity;
import tranquvis.simplesmsremote.Data.AuthenticationModuleSettingsData;
import tranquvis.simplesmsremote.Data.ModuleUserData;
import tranquvis.simplesmsremote.R;

public class AuthenticationModuleActivity extends ModuleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isModuleEnabled) {
            if (moduleSettings == null) {
                this.moduleSettings = new AuthenticationModuleSettingsData();
            }

            final AuthenticationModuleSettingsData settings = getSettings();

            setSettingsContentLayout(R.layout.content_module_settings_authentication);

            EditText passwordEditText = findViewById(R.id.editText_password);
            passwordEditText.setText(settings.getPassword());

            passwordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    String newPassword = passwordEditText.getText().toString();
                    settings.setPassword(newPassword);
                }
            });
        }
    }

    private AuthenticationModuleSettingsData getSettings() {
        return (AuthenticationModuleSettingsData) moduleSettings;
    }

    @Override
    public void setupModuleUserData() {
        userData = new ModuleUserData(module.getId(), moduleSettings);
    }
}
