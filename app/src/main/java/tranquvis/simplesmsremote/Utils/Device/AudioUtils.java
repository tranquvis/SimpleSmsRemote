package tranquvis.simplesmsremote.Utils.Device;

import android.content.Context;
import android.media.AudioManager;

import org.apache.commons.lang3.ArrayUtils;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andreas Kaltenleitner on 07.10.2016.
 */

public class AudioUtils
{
    public static final int VOLUME_INDEX_RING_VIBRATE = 10001;
    public static final int VOLUME_INDEX_RING_SILENT = 10000;
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
        int ringerMode = -1;
        if(type == AudioType.RING && volumeIndex == VOLUME_INDEX_RING_VIBRATE)
        {
            volumeIndex = 0;
            ringerMode = AudioManager.RINGER_MODE_VIBRATE;
        }
        else if(type == AudioType.RING && volumeIndex == VOLUME_INDEX_RING_SILENT)
        {
            volumeIndex = 0;
            ringerMode = AudioManager.RINGER_MODE_SILENT;
        }
        else if(volumeIndex < 0) volumeIndex = 0;

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(type.getStreamType(), volumeIndex, 0);
        if(ringerMode != -1)
            audioManager.setRingerMode(ringerMode);
    }

    /**
     * Get volume as index
     * @param context app context
     * @param type audio type
     * @return volume index (device dependent)
     * or constant value like {@code VOLUME_INDEX_RING_VIBRATE}
     */
    public static int GetVolumeIndex(Context context, AudioType type)
    {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(type == AudioType.RING)
        {
            int ringerMode = audioManager.getRingerMode();
            if(ringerMode == AudioManager.RINGER_MODE_VIBRATE)
                return VOLUME_INDEX_RING_VIBRATE;
            if(ringerMode == AudioManager.RINGER_MODE_SILENT)
                return VOLUME_INDEX_RING_SILENT;
        }
        return audioManager.getStreamVolume(type.getStreamType());
    }

    /**
     * Get volume as index and return no constant values
     * @param context app context
     * @param type audio type
     * @return volume index (device dependent)
     */
    public static int GetVolumeIndexWithoutConstants(Context context, AudioType type)
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
        ALARM(AudioManager.STREAM_ALARM, "alarm", "alarms"),
        NOTIFICATION(AudioManager.STREAM_NOTIFICATION, "notification", "notifications", "notify"),
        SYSTEM(AudioManager.STREAM_SYSTEM, "system"), //battery low, usb connected, keyboard press, unlock, boot, ...
        VOICECALL(AudioManager.STREAM_VOICE_CALL, "phonecall", "voicecall[s]?", "phonecall[s]?", "call[s]?"),
        DTMF(AudioManager.STREAM_DTMF, "dtmf");

        private int streamType;
        private String name;
        private String[] aliases;

        /**
         * create audio type
         * @param streamType related stream type
         * @param name audio type default name
         * @param aliases as regular expressions
         */
        AudioType(int streamType, String name, String... aliases)
        {
            this.streamType = streamType;
            this.name = name;
            this.aliases = aliases;
        }

        public int getStreamType()
        {
            return streamType;
        }

        public static AudioType GetFromName(String name)
        {
            for(AudioType audioType : AudioType.values())
            {
                if(audioType.name.equals(name))
                    return audioType;

                for (String alias : audioType.aliases)
                {
                    if(Pattern.matches(alias, name))
                        return audioType;
                }
            }
            throw new IllegalArgumentException("No audio type found");
        }
    }
}
