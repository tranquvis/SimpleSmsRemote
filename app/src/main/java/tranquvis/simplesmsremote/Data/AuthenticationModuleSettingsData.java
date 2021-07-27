package tranquvis.simplesmsremote.Data;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.Utils.Device.CameraUtils;

public class AuthenticationModuleSettingsData extends ModuleSettingsData {
    private String password;

    public static final String DEFAULT_PASSWORD = "password";

    public AuthenticationModuleSettingsData() {
        this(DEFAULT_PASSWORD);
    }

    public AuthenticationModuleSettingsData(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
