package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamString;
import tranquvis.simplesmsremote.Data.GrantModuleSettingsData;
import tranquvis.simplesmsremote.Data.PhoneAllowlistModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

public class CommandGrantPhoneRemotely extends PhoneDependentCommand {
    public static final CommandParamString PARAM_MODULE_NAMES = new CommandParamString("module name(s)");
    public static final CommandParamString PARAM_PASSWORD = new CommandParamString("password");
    private static final Collection<String> allModuleKeywords = Arrays.asList(
            "all", "every", "allmodules", "each"
    );
    private static final String PATTERN_ROOT = AdaptSimplePattern("grant ([a-zA-Z0-9-_]*) ([a-zA-Z-_]*(?: [a-zA-Z-_]*)*)");
    private static final String PATTERN_MODULE_NAMES = "[a-zA-Z-_]*(?: [a-zA-Z-_]*)*";
    private static final String PATTERN_PASSWORD = "[a-zA-Z0-9-_]*";

    public CommandGrantPhoneRemotely(@NonNull Module module) {
        super(module);

        this.titleRes = R.string.command_title_get_audio_volume;
        this.syntaxDescList = new String[]{
                "grant [password] [module name(s)...]\n" +
                "grant [password] all"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.BY_INDEX_STRICT,
                new PatternTreeNode(
                        PARAM_PASSWORD.getId(),
                        PATTERN_PASSWORD,
                        MatchType.DO_NOT_MATCH
                ),
                new PatternTreeNode(
                        PARAM_MODULE_NAMES.getId(),
                        PATTERN_MODULE_NAMES,
                        MatchType.DO_NOT_MATCH
                )
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        String phone, CommandExecResult result) throws Exception {
        String providedPassword = commandInstance.getParam(PARAM_PASSWORD);
        String savedPassword = getSettings().getPassword();

        if (!savedPassword.equals(providedPassword)) {
            result.setSuccess(false);
            result.setCustomResultMessage("Granting phone failed: password incorrect");
            return;
        }

        String moduleSearchNamesConcat = commandInstance.getParam(PARAM_MODULE_NAMES);
        String[] moduleSearchNames = moduleSearchNamesConcat.split(" ");

        List<Module> modules = getEnabledPhoneAllowlistModules();

        String firstModuleSearchName = moduleSearchNames[0];
        if (moduleSearchNames.length == 1 && allModuleKeywords.contains(firstModuleSearchName)) {
            for (Module moduleToGrant : modules) {
                PhoneAllowlistModuleUserData userData =
                        (PhoneAllowlistModuleUserData) moduleToGrant.getUserData();
                DataManager.getUserData().setControlModule(userData.withGrantedPhone(phone));
            }

            result.setCustomResultMessage(String.format(
                    "Granting phone successful: granted number \"%s\" access to all modules",
                    phone
            ));
        } else {
            List<Module> modulesToGrant = new ArrayList<>(moduleSearchNames.length);
            for (String moduleSearchName : moduleSearchNames) {
                Module moduleToGrant = findModule(modules, context, moduleSearchName);

                if (moduleToGrant == null) {
                    result.setSuccess(false);
                    result.setForceSendingResultSmsMessage(true);
                    result.setCustomResultMessage(String.format(
                            "Granting phone failed: no such module \"%s\"",
                            moduleSearchName
                    ));
                    return;
                }

                modulesToGrant.add(moduleToGrant);
            }

            List<String> moduleTitles = new ArrayList<>(modulesToGrant.size());
            for (Module moduleToGrant : modulesToGrant) {
                PhoneAllowlistModuleUserData userData =
                        (PhoneAllowlistModuleUserData) moduleToGrant.getUserData();
                DataManager.getUserData().setControlModule(userData.withGrantedPhone(phone));

                String moduleTitle = context.getString(moduleToGrant.getTitleRes());
                moduleTitles.add(String.format("\"%s\"", moduleTitle));
            }

            result.setCustomResultMessage(String.format(
                    "Granting phone successful: granted phone \"%s\" access to module(s) %s",
                    phone, StringUtils.join(moduleTitles, ", ")
            ));
        }

        DataManager.SaveUserData(context);
        result.setForceSendingResultSmsMessage(true);
    }

    private GrantModuleSettingsData getSettings() {
        return (GrantModuleSettingsData) module.getUserData().getSettings();
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
                .replaceAll("[ -_]", "")
                .replaceAll("module", "");
    }
}
