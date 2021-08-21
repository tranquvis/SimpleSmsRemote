package tranquvis.simplesmsremote.CommandManagement.Commands;

import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandGrantPhoneRemotely.PARAM_MODULE_NAMES;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandGrantPhoneRemotely.PARAM_PASSWORD;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Modules.ModuleGrantPhoneRemotely;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.GrantModuleSettingsData;
import tranquvis.simplesmsremote.Data.ModuleUserData;
import tranquvis.simplesmsremote.Data.PhoneAllowlistModuleUserData;
import tranquvis.simplesmsremote.Data.UserData;
import tranquvis.simplesmsremote.Utils.UnitTestUtils;

public class CommandGrantPhoneRemotelyTest extends CommandTest {
    private static final String password = "pwd";

    @Override
    @Before
    public void setUp() throws Exception {
        Module module = new ModuleGrantPhoneRemotely();
        GrantModuleSettingsData settings = new GrantModuleSettingsData(password);
        ModuleUserData userData = new ModuleUserData(module.getId(), settings);
        DataManager.getUserData().setControlModule(userData);
        command = new CommandGrantPhoneRemotely(module);

        super.setUp();
    }

    @Override
    @Test
    public void testPattern() throws Exception {
        assertThat("grant pwdABCXYZabcxyz1239-_ all").matches()
                .has(PARAM_PASSWORD, "pwdABCXYZabcxyz1239-_");
        assertThat("grant pwd all").matches()
                .has(PARAM_MODULE_NAMES, "all");
        assertThat("grant pwd audio wifi-hotspot").matches()
                .has(PARAM_MODULE_NAMES, "audio wifi-hotspot");
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("grant pwd all").matches().executes();
        assertThat("grant pwd audio wifi-hotspot").matches().executes();
    }
}