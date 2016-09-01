package tranquvis.simplesmsremote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.ReceiverService.SMSReceiverService;

/**
 * Created by Andi on 01.09.2016.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.i("simplesmsremote", "boot completed");
            DataManager.LoadUserData(context);
            if(DataManager.getUserData().getUserSettings().isStartReceiverOnSystemStart())
                SMSReceiverService.start(context);
        } catch (Exception e) {
            MyNotificationManager.getInstance(context).notifyStartReceiverAfterBootFailed();
        }
    }
}
