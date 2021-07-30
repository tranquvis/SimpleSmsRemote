package tranquvis.simplesmsremote.Data;

import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andreas Kaltenleitner on 29.08.2016.
 */
public class PhoneWhitelistModuleUserData extends ModuleUserData {
    private final List<String> grantedPhones;

    public PhoneWhitelistModuleUserData(String controlModuleId, @Nullable List<String> grantedPhones,
                                        @Nullable ModuleSettingsData settings) {
        super(controlModuleId, settings);
        this.grantedPhones = grantedPhones != null ? grantedPhones : new ArrayList<String>();
    }

    public List<String> getGrantedPhones() {
        return Collections.unmodifiableList(grantedPhones);
    }

    public boolean isPhoneGranted(String phone) {
        for (String grantedPhone : grantedPhones) {
            if (PhoneNumberUtils.compare(phone, grantedPhone))
                return true;
        }
        return false;
    }

    public PhoneWhitelistModuleUserData withGrantedPhone(String phone) {
        if (isPhoneGranted(phone)) {
            return this;
        } else {
            List<String> newGrantedPhones = new ArrayList<>(grantedPhones);
            newGrantedPhones.add(phone);
            return new PhoneWhitelistModuleUserData(getControlModuleId(), newGrantedPhones, getSettings());
        }
    }

    public PhoneWhitelistModuleUserData withGrantedPhones(List<String> phones) {
        return new PhoneWhitelistModuleUserData(
                getControlModuleId(), new ArrayList<>(phones), getSettings()
        );
    }

    @Override
    public ModuleUserData withSettings(ModuleSettingsData settings) {
        return new PhoneWhitelistModuleUserData(getControlModuleId(), getGrantedPhones(), settings);
    }
}
