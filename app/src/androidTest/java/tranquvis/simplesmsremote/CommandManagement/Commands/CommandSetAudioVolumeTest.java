package tranquvis.simplesmsremote.CommandManagement.Commands;

import tranquvis.simplesmsremote.Utils.Device.AudioUtils;

import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetAudioVolume.*;

/**
 * Created by Kaltenleitner Andreas on 30.10.2016.
 */
public class CommandSetAudioVolumeTest extends CommandTest {

    @Override
    public void testPattern() throws Exception {
        // check special characters
        assertThat("\n Set Music  volume to 0 \r").matches();

        //region check units
        assertThat("set volume index of ring to 4.2").matches()
                .has(PARAM_VOLUME_UNIT, Unit.INDEX);
        assertThat("set volume percentage of ring to 4").matches()
                .has(PARAM_VOLUME_UNIT, Unit.PERCENT);
        assertThat("set volume percentage of ring to 4,5").matches()
                .has(PARAM_VOLUME_UNIT, Unit.PERCENT);
        assertThat("set volume of ring to 4%").matches()
                .has(PARAM_VOLUME_UNIT, Unit.PERCENT);
        assertThat("set volume percentage of ring to 4%").matches()
                .has(PARAM_VOLUME_UNIT, Unit.PERCENT);
        //endregion

        //region check ringer modes
        assertThat("set volume ring to vibrate").matches()
                .has(PARAM_RINGER_MODE, AudioUtils.VOLUME_INDEX_RING_VIBRATE);
        assertThat("set volume ring to silent").matches()
                .has(PARAM_RINGER_MODE, AudioUtils.VOLUME_INDEX_RING_SILENT);
        //endregion

        //region check audio types
        assertThat("set music volume to 1").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.MUSIC);

        assertThat("set ringtone volume to 2").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.RING);
        assertThat("set ring volume to 2").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.RING);

        assertThat("set alarm volume to 3").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.ALARM);

        assertThat("set notification volume to 4").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.NOTIFICATION);
        assertThat("set notify volume to 4").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.NOTIFICATION);

        assertThat("set system volume to 5").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.SYSTEM);

        assertThat("set phonecall volume to 6").matches()
                .has(PARAM_AUDIO_TYPE,
                AudioUtils.AudioType.VOICECALL);
        assertThat("set voicecall volume to 6").matches()
                .has(PARAM_AUDIO_TYPE,
                AudioUtils.AudioType.VOICECALL);
        assertThat("set calls volume to 6").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.VOICECALL);

        assertThat("set dtmf volume to 7").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.DTMF);
        //endregion
        //region check audio type arrangements
        assertThat("set volume music to 0").matches();
        assertThat("set music volume to 0").matches();
        assertThat("set volume of music to 0").matches();
        assertThat("set volume for music to 0").matches();
        //endregion

        //region combinations
        assertThat("set volume of music to 30%").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.MUSIC)
                .has(PARAM_VOLUME_UNIT, Unit.PERCENT)
                .has(PARAM_RINGER_MODE, null)
                .has(PARAM_VOLUME_VALUE, 30d);
        assertThat("set volume of dtmf to 10").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.DTMF)
                .has(PARAM_VOLUME_UNIT, null)
                .has(PARAM_RINGER_MODE, null)
                .has(PARAM_VOLUME_VALUE, 10d);
        assertThat("set ring volume to silent").matches()
                .has(PARAM_AUDIO_TYPE, AudioUtils.AudioType.RING)
                .has(PARAM_VOLUME_UNIT, null)
                .has(PARAM_RINGER_MODE, AudioUtils.VOLUME_INDEX_RING_SILENT)
                .has(PARAM_VOLUME_VALUE, null);
    }

    @Override
    public void testExecution() throws Exception {
        assertThat("set volume music to vibrate").matches().executesWithError();
        assertThat("set volume ring to vibrate").matches().executes();
        assertThat("set volume music to 1").matches().executes();
        assertThat("set volume dtmf to 10%").matches().executes();
        assertThat("set volume alarm to 50.1%").matches().executes();
        assertThat("set volume notify to 50").matches().executes();
        assertThat("set volume system to 50").matches().executes();
        assertThat("set volume phonecalls to 10").matches().executes();

        assertThat("set volume ring to 0").matches().executes();
        assertThat("set volume music to 0").matches().executes();
        assertThat("set volume alarm to 0").matches().executes();
        assertThat("set volume phonecalls to 0").matches().executes();
        assertThat("set volume system to 0").matches().executes();
        assertThat("set volume dtmf to 0").matches().executes();
        assertThat("set volume notify to 0").matches().executes();
    }
}