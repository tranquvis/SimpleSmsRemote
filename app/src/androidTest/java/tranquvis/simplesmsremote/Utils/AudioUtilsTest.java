package tranquvis.simplesmsremote.Utils;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.Aspects.ExecSequentially.ExecSequentially;
import tranquvis.simplesmsremote.Utils.AudioUtils.AudioType;

import static org.junit.Assert.*;

/**
 * Created by Andreas Kaltenleitner on 07.10.2016.
 */
public class AudioUtilsTest extends AppContextTest
{
    @Test
    public void setSpecificVolume()
    {
        testSetVolumeIndex(AudioType.RING, AudioUtils.VOLUME_INDEX_RING_VIBRATE);
    }

    @Test
    @ExecSequentially("audio")
    public void setVolumePercentage() throws Exception
    {
        for(AudioType audioType : AudioType.values())
        {
            testSetVolumePercentage(audioType, 100);
            testSetVolumePercentage(audioType, 0);
            testSetVolumePercentage(audioType, -10);
            testSetVolumePercentage(audioType, 200);
            testSetVolumePercentage(audioType, 45);
        }
    }

    @Test
    @ExecSequentially("audio")
    public void setVolumeIndex() throws Exception
    {
        for(AudioType audioType : AudioType.values())
        {
            testSetVolumeIndex(audioType, 20);
            testSetVolumeIndex(audioType, 0);
            testSetVolumeIndex(audioType, -10);
            testSetVolumeIndex(audioType, 3);
            if(audioType == AudioType.RING)
            {
                testSetVolumeIndex(audioType, AudioUtils.VOLUME_INDEX_RING_SILENT);
                testSetVolumeIndex(audioType, AudioUtils.VOLUME_INDEX_RING_VIBRATE);
            }
        }
    }

    @Test
    @ExecSequentially("audio")
    public void getVolumePercentage() throws Exception
    {
        AudioUtils.GetVolumeIndex(appContext, AudioUtils.AudioType.MUSIC);
    }

    @Test
    @ExecSequentially("audio")
    public void getVolumeIndex() throws Exception
    {
        AudioUtils.GetVolumeIndex(appContext, AudioUtils.AudioType.MUSIC);
    }

    private void testSetVolumePercentage(AudioUtils.AudioType audioType, float volumePercentage)
    {
        AudioManager audioManager = (AudioManager)
                appContext.getSystemService(Context.AUDIO_SERVICE);
        int maxVolumeIndex = audioManager.getStreamMaxVolume(audioType.getStreamType());
        float spreadVolume = 100f / maxVolumeIndex / 2f;

        AudioUtils.SetVolumePercentage(appContext, volumePercentage, audioType);

        int[] expectedVolumeIndexes = null;
        if((audioType == AudioType.VOICECALL)
                && volumePercentage < (100f / (float) maxVolumeIndex))
            expectedVolumeIndexes = new int[]{ 0, 1 };
        else if(volumePercentage > 100)
            expectedVolumeIndexes = new int[]{ maxVolumeIndex };
        else if(volumePercentage < 0)
            expectedVolumeIndexes = new int[]{ 0 };

        if(expectedVolumeIndexes != null)
        {
            int actualVolumeIndex = AudioUtils.GetVolumeIndexWithoutConstants(appContext, audioType);
            assertTrue("audio type: " + audioType.name()
                    + ", given volume percentage: " + volumePercentage
                    + ", expected volume indexes: " + StringUtils.join(expectedVolumeIndexes, ',')
                    + ", actual volume: " + actualVolumeIndex,
                    ArrayUtils.contains(expectedVolumeIndexes, actualVolumeIndex));
        }
        else
        {
            float actualVolume = AudioUtils.GetVolumePercentage(appContext, audioType);
            assertTrue("audio type: " + audioType.name()
                    + ", given volume: " + volumePercentage
                    + ", expected volume: " + volumePercentage
                    + ", actual volume: " + actualVolume,
                    actualVolume < volumePercentage + spreadVolume
                    && actualVolume > volumePercentage - spreadVolume);
        }
    }

    private void testSetVolumeIndex(AudioUtils.AudioType audioType, int volumeIndex)
    {
        AudioUtils.SetVolumeIndex(appContext, volumeIndex, audioType);
        int actualVolumeIndex = AudioUtils.GetVolumeIndex(appContext, audioType);

        AudioManager audioManager = (AudioManager)
                appContext.getSystemService(Context.AUDIO_SERVICE);
        int maxVolumeIndex = audioManager.getStreamMaxVolume(audioType.getStreamType());

        int[] expectedVolumeIndexes = new int[]{ volumeIndex };
        if((audioType == AudioType.VOICECALL) && volumeIndex < 1)
        {
            expectedVolumeIndexes = new int[]{1, 0};
        }
        else if(audioType == AudioType.RING && volumeIndex == AudioUtils.VOLUME_INDEX_RING_SILENT)
        {
            expectedVolumeIndexes = new int[]{AudioUtils.VOLUME_INDEX_RING_SILENT};
        }
        else if (audioType == AudioType.RING && isEmulator()
                && (volumeIndex == AudioUtils.VOLUME_INDEX_RING_VIBRATE || volumeIndex <= 0))
        {
            //allow silent volume setting too on emulators because emulators can not vibrate
            expectedVolumeIndexes = new int[]{AudioUtils.VOLUME_INDEX_RING_VIBRATE,
                    AudioUtils.VOLUME_INDEX_RING_SILENT};
        }
        else if(audioType == AudioType.RING
                && (volumeIndex == AudioUtils.VOLUME_INDEX_RING_VIBRATE || volumeIndex <= 0))
            expectedVolumeIndexes = new int[]{ AudioUtils.VOLUME_INDEX_RING_VIBRATE };
        else if(volumeIndex > maxVolumeIndex)
            expectedVolumeIndexes = new int[]{ maxVolumeIndex };
        else if(volumeIndex < 0)
            expectedVolumeIndexes = new int[]{ 0 };

        assertTrue("audio type: " + audioType.name()
                + ", given volume: " + volumeIndex
                + ", expected volume indexes: " + StringUtils.join(expectedVolumeIndexes, ',')
                + ", actual volume: " + actualVolumeIndex,
                ArrayUtils.contains(expectedVolumeIndexes, actualVolumeIndex));
    }
}