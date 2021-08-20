package tranquvis.simplesmsremote.Data;

import java.security.SecureRandom;

public class GrantModuleSettingsData extends ModuleSettingsData {
    private String password;

    // Uppercase latin characters (excluding "O") and digits (excluding "0")
    private static final String RANDOM_PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";
    private static final int RANDOM_PASSWORD_LENGTH = 6;

    public GrantModuleSettingsData() {
        this(generatePassword());
    }

    public GrantModuleSettingsData(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String generatePassword() {
        SecureRandom rng = new SecureRandom();
        char[] passwordChars = new char[RANDOM_PASSWORD_LENGTH];
        for (int i = 0; i < passwordChars.length; i++) {
            int randomCharIndex = rng.nextInt(RANDOM_PASSWORD_CHARACTERS.length());
            passwordChars[i] = RANDOM_PASSWORD_CHARACTERS.charAt(randomCharIndex);
        }
        return new String(passwordChars);
    }
}
