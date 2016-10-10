package tranquvis.simplesmsremote.Utils;

import android.content.Context;
import android.media.AudioManager;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by Andreas Kaltenleitner on 07.10.2016.
 */

public class AudioUtils
{
    /**
     * Set volume as percentage
     * @param context app context
     * @param volume volume in percent
     * @param type audio type
     */
    public static void SetVolumePercentage(Context context, float volume, AudioType type)
    {
        if(volume < 0) volume = 0;

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(type.getStreamType(),
                getVolumeIndexFromPercentage(audioManager, type.getStreamType(), volume), 0);
    }

    /**
     * Get volume as percentage
     * @param context app context
     * @param type audio type
     * @return volume in percent
     */
    public static float GetVolumePercentage(Context context, AudioType type)
    {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return getVolumePercentageFromIndex(audioManager, type.getStreamType(),
                audioManager.getStreamVolume(type.getStreamType()));
    }

    /**
     * Set volume as index
     * @param context app context
     * @param volumeIndex volume as index (max. is depends on device)
     * @param type audio type
     */
    public static void SetVolumeIndex(Context context, int volumeIndex, AudioType type)
    {
        if(volumeIndex < 0) volumeIndex = 0;

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(type.getStreamType(), volumeIndex, 0);
    }

    /**
     * Get volume as index
     * @param context app context
     * @param type audio type
     * @return volume index (device dependent)
     */
    public static int GetVolumeIndex(Context context, AudioType type)
    {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(type.getStreamType());
    }

    private static int getVolumeIndexFromPercentage(AudioManager audioManager, int streamType, float volumePercentage)
    {
        int maxIndex = audioManager.getStreamMaxVolume(streamType);
        return Math.round(volumePercentage/100f * (float)maxIndex);
    }

    private static float getVolumePercentageFromIndex(AudioManager audioManager, int streamType, int volumeIndex)
    {
        int maxIndex = audioManager.getStreamMaxVolume(streamType);
        return (float)volumeIndex/(float)maxIndex * 100f;
    }

    public enum AudioType{
        RING(AudioManager.STREAM_RING, "ring", "ringtone"),
        MUSIC(AudioManager.STREAM_MUSIC, "music"),
        ALARM(AudioManager.STREAM_ALARM, "alarm"),
        NOTIFICATION(AudioManager.STREAM_NOTIFICATION, "notification"),
        SYSTEM(AudioManager.STREAM_SYSTEM, "system"), //battery low, usb connected, keyboard press, unlock, boot, ...
        VOICECALL(AudioManager.STREAM_VOICE_CALL, "phonecall", "voicecall", "phonecall", "call"),
        DTMF(AudioManager.STREAM_DTMF, "dtmf");

        private int streamType;
        private String[] names;

        AudioType(int streamType, String... names)
        {
            this.streamType = streamType;
            this.names = names;
        }

        public int getStreamType()
        {
            return streamType;
        }

        public static AudioType GetFromName(String name)
        {
            for(AudioType audioType : AudioType.values())
            {
                if(ArrayUtils.contains(audioType.names, name))
                    return audioType;
            }
            throw new IllegalArgumentException("No audio type found");
        }
    }
}
