package tranquvis.simplesmsremote;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.Helper.MyNotificationManager;
import tranquvis.simplesmsremote.Sms.MyCommandMessage;

/**
 * Created by Andreas Kaltenleitner on 02.09.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MyNotificationManagerTest extends AppContextTest
{
    private MyNotificationManager notificationManager;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        notificationManager = MyNotificationManager.getInstance(appContext);
    }

    @Test
    public void testNotifySmsCommandsReceived() throws Exception
    {
        CommandInstance ci1 = CommandInstance.CreateFromCommand("enable wifi");
        CommandInstance ci2 = CommandInstance.CreateFromCommand("disable hotspot");

        MyCommandMessage smsCommandMessage = new MyCommandMessage("000");
        smsCommandMessage.addCommandInstance(ci1);
        smsCommandMessage.addCommandInstance(ci2);

        CommandExecResult result1 = ci1.executeCommand(appContext, smsCommandMessage);
        CommandExecResult result2 = ci2.executeCommand(appContext, smsCommandMessage);

        List<CommandExecResult> executionResults = new ArrayList<>();
        executionResults.add(result1);
        executionResults.add(result2);

        notificationManager.notifySmsCommandsExecuted(smsCommandMessage, executionResults);
    }

    @Test
    public void testNotifyStartReceiverAfterBootFailed() throws Exception
    {
        notificationManager.notifyStartReceiverAfterBootFailed();
    }

    @After
    public void beforeEnd() throws InterruptedException
    {
        //Thread.sleep(20000);
    }
}