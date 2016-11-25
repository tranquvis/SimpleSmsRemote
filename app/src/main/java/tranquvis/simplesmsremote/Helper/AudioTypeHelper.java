package tranquvis.simplesmsremote.Helper;

import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.AudioUtils;

/**
 * Created by Andreas Kaltenleitner on 31.10.2016.
 */

public class AudioTypeHelper {
    public static int GetNameResFromAudioType(AudioUtils.AudioType audioType) {
        switch (audioType) {
            case RING:
                return R.string.audio_type_name_ring;
            case MUSIC:
                return R.string.audio_type_name_music;
            case ALARM:
                return R.string.audio_type_name_alarm;
            case NOTIFICATION:
                return R.string.audio_type_name_notification;
            case SYSTEM:
                return R.string.audio_type_name_system;
            case VOICECALL:
                return R.string.audio_type_name_voicecall;
            case DTMF:
                return R.string.audio_type_name_dtmf;
            default:
                throw new IllegalArgumentException("unexpected audio type");
        }
    }
}
