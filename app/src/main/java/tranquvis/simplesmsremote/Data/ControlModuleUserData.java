package tranquvis.simplesmsremote.Data;

import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas Kaltenleitner on 29.08.2016.
 */
public class ControlModuleUserData implements Serializable {
    private String controlModuleId;
    private List<String> grantedPhones;
    private ModuleSettingsData settings;

    public ControlModuleUserData(String controlModuleId, @Nullable List<String> grantedPhones,
                                 @Nullable ModuleSettingsData settings) {
        this.controlModuleId = controlModuleId;
        this.grantedPhones = grantedPhones != null ? grantedPhones : new ArrayList<String>();
        this.settings = settings;
    }

    public String getControlModuleId() {
        return controlModuleId;
    }

    public List<String> getGrantedPhones() {
        return grantedPhones;
    }

    public boolean isPhoneGranted(String phone) {
        for (String grantedPhone : grantedPhones) {
            if (PhoneNumberUtils.compare(phone, grantedPhone))
                return true;
        }
        return false;
    }

    public ModuleSettingsData getSettings() {
        return settings;
    }
}
