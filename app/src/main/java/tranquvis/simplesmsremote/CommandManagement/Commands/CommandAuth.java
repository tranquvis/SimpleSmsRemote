package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamString;
import tranquvis.simplesmsremote.Data.AuthenticationModuleSettingsData;
import tranquvis.simplesmsremote.Data.PhoneAllowlistModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

public class CommandAuth extends PhoneDependentCommand {
    static final CommandParamString PARAM_MODULE_NAME = new CommandParamString("module name");
    static final CommandParamString PARAM_PASSWORD = new CommandParamString("password");
    private static final Collection<String> allModuleKeywords = Arrays.asList(
            "all", "every", "allmodules", "each"
    );
    private static final String PATTERN_ROOT = AdaptSimplePattern("auth ([a-zA-Z-_]*) (.*)");
    private static final String PATTERN_MODULE_NAME = "[a-zA-Z-_]*";
    private static final String PATTERN_PASSWORD = "[a-zA-Z0-9-_]*";

    public CommandAuth(@NonNull Module module) {
        super(module);

        this.titleRes = R.string.command_title_get_audio_volume;
        this.syntaxDescList = new String[]{
                "auth [module name] [password]\n" +
                "auth all [password]"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.BY_INDEX_STRICT,
                new PatternTreeNode(
                        PARAM_MODULE_NAME.getId(),
                        PATTERN_MODULE_NAME,
                        MatchType.DO_NOT_MATCH
                ),
                new PatternTreeNode(
                        PARAM_PASSWORD.getId(),
                        PATTERN_PASSWORD,
                        MatchType.DO_NOT_MATCH
                )
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        String phone, CommandExecResult result) throws Exception {
        String moduleSearchName = commandInstance.getParam(PARAM_MODULE_NAME);

        String providedPassword = commandInstance.getParam(PARAM_PASSWORD);
        String savedPassword = getSettings().getPassword();

        if (!savedPassword.equals(providedPassword)) {
            result.setSuccess(false);
            result.setCustomResultMessage("Authentication failed: password incorrect");
            return;
        }

        List<Module> modules = getEnabledPhoneAllowlistModules();

        if (allModuleKeywords.contains(moduleSearchName)) {
            for (Module moduleToGrant : modules) {
                PhoneAllowlistModuleUserData userData =
                        (PhoneAllowlistModuleUserData) moduleToGrant.getUserData();
                DataManager.getUserData().setControlModule(userData.withGrantedPhone(phone));
            }

            result.setCustomResultMessage(String.format(
                    "Authentication successful: granted number \"%s\" access to all modules",
                    phone
            ));
        } else {
            Module moduleToGrant = findModule(modules, context, moduleSearchName);

            if (moduleToGrant == null) {
                result.setSuccess(false);
                result.setCustomResultMessage(String.format(
                        "Authentication failed: no such module \"%s\"",
                        moduleSearchName
                ));
                return;
            }

            PhoneAllowlistModuleUserData userData =
                    (PhoneAllowlistModuleUserData) moduleToGrant.getUserData();
            DataManager.getUserData().setControlModule(userData.withGrantedPhone(phone));

            String moduleTitle = context.getString(moduleToGrant.getTitleRes());
            result.setCustomResultMessage(String.format(
                    "Authentication successful: granted phone \"%s\" access to module \"%s\"",
                    phone, moduleTitle
            ));
        }

        DataManager.SaveUserData(context);
        result.setForceSendingResultSmsMessage(true);
    }

    private AuthenticationModuleSettingsData getSettings() {
        return (AuthenticationModuleSettingsData) module.getUserData().getSettings();
    }

    private List<Module> getEnabledPhoneAllowlistModules() {
        ArrayList<Module> enabledModules = new ArrayList<>();
        Collection<Module> modules = Instances.GetAll(null);

        for (Module module : modules) {
            if (!module.isEnabled()) continue;
            if (!(module.getUserData() instanceof PhoneAllowlistModuleUserData)) continue;
            enabledModules.add(module);
        }

        return enabledModules;
    }

    private static Module findModule(
            List<Module> modules, Context context, String moduleSearchName
    ) {
        String searchNameNormalized = normalizeModuleName(moduleSearchName);

        // Try module ids first
        for (Module module : modules) {
            String moduleId = normalizeModuleName(module.getId());
            if (searchNameNormalized.equals(moduleId)) {
                return module;
            }
        }

        // Fall back to locale specific title
        for (Module module : modules) {
            String moduleTitle = normalizeModuleName(context.getString(module.getTitleRes()));
            if (searchNameNormalized.equals(moduleTitle)) {
                return module;
            }
        }

        return null;
    }

    private static String normalizeModuleName(String moduleName) {
        return moduleName.toLowerCase()
                .replaceAll("[-_]", "");
    }
}
