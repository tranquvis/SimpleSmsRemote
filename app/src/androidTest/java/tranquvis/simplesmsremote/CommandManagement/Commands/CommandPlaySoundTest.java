package tranquvis.simplesmsremote.CommandManagement.Commands;

import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandPlaySound.PARAM_DURATION_UNIT;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandPlaySound.PARAM_DURATION_VALUE;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import tranquvis.simplesmsremote.ServiceTestUtils;
import tranquvis.simplesmsremote.Services.PlaySoundService;
import tranquvis.simplesmsremote.Utils.Device.AudioUtils;
import tranquvis.simplesmsremote.Utils.UnitTools.Unit;

public class CommandPlaySoundTest extends CommandTest {
    @Override
    @Test
    public void testPattern() throws Exception {
        assertThat("\n play sound \r").matches();
        assertThat("play ringtone").matches();
        assertThat("\n play sound for 10s \r").matches()
            .has(PARAM_DURATION_VALUE, 10.0d)
            .has(PARAM_DURATION_UNIT, Unit.SECONDS);
        assertThat(String.format("play ringtone for %,.4fms", 10000d)).matches()
                .has(PARAM_DURATION_VALUE, 10000d)
                .has(PARAM_DURATION_UNIT, Unit.MILLISECONDS);
        assertThat("play sound 10 S").matches()
                .has(PARAM_DURATION_VALUE, 10d)
                .has(PARAM_DURATION_UNIT, Unit.SECONDS);
        assertThat(format("play ringtone %.4f min", 21.4d)).matches()
                .has(PARAM_DURATION_VALUE, 21.4d)
                .has(PARAM_DURATION_UNIT, Unit.MINUTES);
        assertThat(format("play sound for %.4fh", 1.444d)).matches()
                .has(PARAM_DURATION_VALUE, 1.444d)
                .has(PARAM_DURATION_UNIT, Unit.HOURS);
        assertThat(format("play sound for 1h", 1.0d)).matches()
                .has(PARAM_DURATION_VALUE, 1.0d)
                .has(PARAM_DURATION_UNIT, Unit.HOURS);
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        Map<AudioUtils.AudioType, Integer> volumeIndices = getVolumeIndices(appContext);
        assertThat("play sound for 2s").matches().executes();
        Thread.sleep(500);
        Assert.assertTrue(ServiceTestUtils.isServiceRunning(appContext, PlaySoundService.class));
        Thread.sleep(3000);
        Assert.assertFalse(ServiceTestUtils.isServiceRunning(appContext, PlaySoundService.class));
        Assert.assertEquals(volumeIndices, getVolumeIndices(appContext));
    }

    private static Map<AudioUtils.AudioType, Integer> getVolumeIndices(Context context) {
        Map<AudioUtils.AudioType, Integer> map = new HashMap<>();
        for (AudioUtils.AudioType type : AudioUtils.AudioType.values()) {
            map.put(type, AudioUtils.GetVolumeIndex(context, type));
        }
        return map;
    }
}