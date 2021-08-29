package tranquvis.simplesmsremote.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import java.io.IOException;

import tranquvis.simplesmsremote.Helper.MyNotificationManager;
import tranquvis.simplesmsremote.Utils.Device.AudioUtils;

public class PlaySoundService extends Service {
    public static String ACTION_START = "start";
    public static String ACTION_STOP = "stop";

    private static final int ID = 932733;
    private static final int REQUEST_CODE_NOTIFICATION_STOP_ACTION = 1;

    private MediaPlayer mp;
    private int previousVolume = -1;

    public PlaySoundService() { }

    public static void start(Context context, Uri source, int duration) {
        Intent intent = new Intent(context, PlaySoundService.class);
        intent.setAction(ACTION_START);
        intent.putExtra("source", source);
        intent.putExtra("duration", duration);
        context.startService(intent);
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, PlaySoundService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null && action.equals(ACTION_START)) {
            Bundle bundle = intent.getExtras();
            Uri uri = bundle.getParcelable("source");
            int duration = bundle.getInt("duration");

            Intent stopIntent = new Intent(this, this.getClass());
            stopIntent.setAction(ACTION_STOP);
            PendingIntent pendingIntent = PendingIntent.getService(this,
                    REQUEST_CODE_NOTIFICATION_STOP_ACTION, stopIntent, 0);
            Notification notification = MyNotificationManager.getInstance(this)
                    .getPlaySoundStatusNotification(uri, duration + 2000, pendingIntent);
            startForeground(ID, notification);

            stopSoundIfActive();
            startSound(uri);
            scheduleStopSound(duration);

            return Service.START_NOT_STICKY;
        } else if (action != null && action.equals(ACTION_STOP)) {
            stopSelf();
            return Service.START_NOT_STICKY;
        } else {
            return Service.START_NOT_STICKY;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not supported");
    }

    protected void scheduleStopSound(int duration) {
        new Thread(() -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                stopSoundIfActive();
                stopSelf();
            }
        }).start();
    }

    protected void stopSoundIfActive() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
        if(previousVolume != -1) {
            AudioUtils.SetVolumeIndex(this, previousVolume, AudioUtils.AudioType.ALARM);
            previousVolume = -1;
        }
    }

    protected void startSound(Uri uri) {
        previousVolume = AudioUtils.GetVolumeIndex(this, AudioUtils.AudioType.ALARM);
        AudioUtils.SetVolumePercentage(this, 100, AudioUtils.AudioType.ALARM);

        mp = new MediaPlayer();
        try {
            mp.setAudioStreamType(AudioManager.STREAM_ALARM);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mp.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .setLegacyStreamType(AudioManager.STREAM_ALARM)
                                .build()
                );
            }
            mp.setDataSource(this, uri);
            mp.setLooping(true);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
            mp.release();
            mp = null;
        }
    }

    @Override
    public void onDestroy() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
        if(previousVolume != -1) {
            AudioUtils.SetVolumeIndex(this, previousVolume, AudioUtils.AudioType.ALARM);
            previousVolume = -1;
        }

        super.onDestroy();
    }
}
