package tranquvis.simplesmsremote.CommandManagement.Commands;

import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandGrantPhoneRemotely.PARAM_MODULE_NAMES;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandGrantPhoneRemotely.PARAM_PASSWORD;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.GrantModuleSettingsData;
import tranquvis.simplesmsremote.Data.ModuleUserData;
import tranquvis.simplesmsremote.TestDataManager;

public class CommandGrantPhoneRemotelyTest extends CommandTest {
    private static final String password = "pwd";

    @Override
    @Before
    public void setUp() throws Exception {
        command = Instances.GRANT_PHONE_REMOTELY.commandGrantPhoneRemotely;
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
    public void testExecution() throws Exception { }

    @Test
    public void testExecutionGrantAll() throws Exception {
        String phone = "123";

        TestDataManager dataManager = new TestDataManager();
        dataManager.enableModule(command.getModule(), new GrantModuleSettingsData(password));
        dataManager.enableModule(Instances.AUDIO);
        dataManager.enableModule(Instances.WIFI_HOTSPOT);
        dataManager.enableModule(Instances.BATTERY);

        assertThat("grant pwd all").matches().fromPhone(phone).executes(dataManager);

        Assert.assertTrue(dataManager.isPhoneGranted(Instances.AUDIO, phone));
        Assert.assertTrue(dataManager.isPhoneGranted(Instances.WIFI_HOTSPOT, phone));
        Assert.assertTrue(dataManager.isPhoneGranted(Instances.BATTERY, phone));
    }

    @Test
    public void testExecutionGrantSpecific() throws Exception {
        String phone = "123";

        TestDataManager dataManager = new TestDataManager();
        dataManager.enableModule(command.getModule(), new GrantModuleSettingsData(password));
        dataManager.enableModule(Instances.AUDIO);
        dataManager.enableModule(Instances.WIFI_HOTSPOT);
        dataManager.enableModule(Instances.BATTERY);

        assertThat("grant pwd audio wifi-hotspot").matches().fromPhone(phone)
                .executes(dataManager);

        Assert.assertTrue(dataManager.isPhoneGranted(Instances.AUDIO, phone));
        Assert.assertTrue(dataManager.isPhoneGranted(Instances.WIFI_HOTSPOT, phone));
        Assert.assertFalse(dataManager.isPhoneGranted(Instances.BATTERY, phone));
    }

    @Test
    public void testExecutionWrongPassword() throws Exception {
        String phone = "123";

        TestDataManager dataManager = new TestDataManager();
        dataManager.enableModule(command.getModule(), new GrantModuleSettingsData(password));
        dataManager.enableModule(Instances.AUDIO);
        dataManager.enableModule(Instances.WIFI_HOTSPOT);
        dataManager.enableModule(Instances.BATTERY);

        assertThat("grant pwd-wrong audio wifi-hotspot").matches().fromPhone(phone)
                .executesWithError(dataManager);

        Assert.assertFalse(dataManager.isPhoneGranted(Instances.AUDIO, phone));
        Assert.assertFalse(dataManager.isPhoneGranted(Instances.WIFI_HOTSPOT, phone));
        Assert.assertFalse(dataManager.isPhoneGranted(Instances.BATTERY, phone));
    }
}