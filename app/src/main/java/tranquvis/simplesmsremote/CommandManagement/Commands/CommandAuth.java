package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamString;
import tranquvis.simplesmsremote.Data.AuthenticationModuleSettingsData;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

public class CommandAuth extends Command {
    private static final String PATTERN_ROOT = AdaptSimplePattern("auth (.*?)");

    static final CommandParamString PARAM_PASSWORD = new CommandParamString("password");
    private static final String PATTERN_PASSWORD = ".*?";

    public CommandAuth(@NonNull Module module) {
        super(module);

        this.titleRes = R.string.command_title_get_audio_volume;
        this.syntaxDescList = new String[]{
                "auth [password]"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.BY_INDEX_STRICT,
                new PatternTreeNode(
                        PARAM_PASSWORD.getId(),
                        PATTERN_PASSWORD,
                        MatchType.DO_NOT_MATCH
                )
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result) throws Exception {
        String providedPassword = commandInstance.getParam(PARAM_PASSWORD);
        String savedPassword = getSettings().getPassword();

        if (savedPassword.equals(providedPassword)) {
            Log.v("AAA", "Password O.K.");
        } else {
            Log.v("AAA", "Password not O.K.");
        }

        result.setCustomResultMessage("Authentication successful");
        result.setForceSendingResultSmsMessage(true);
    }

    @Override
    public boolean isPhoneGranted(String phone) {
        return true; // All phone numbers are allowed to authenticate remotely
    }

    private AuthenticationModuleSettingsData getSettings() {
        return (AuthenticationModuleSettingsData) module.getUserData().getSettings();
    }
}
