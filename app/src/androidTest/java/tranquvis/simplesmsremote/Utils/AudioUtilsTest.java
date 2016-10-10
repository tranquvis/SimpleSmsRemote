package tranquvis.simplesmsremote.Utils;

import android.content.Context;
import android.media.AudioManager;

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

        int expectedVolumeIndex = -1;
        if((audioType == AudioType.VOICECALL)
                && volumePercentage < (100f / (float) maxVolumeIndex))
            expectedVolumeIndex = 1;
        else if(volumePercentage > 100)
            expectedVolumeIndex = maxVolumeIndex;
        else if(volumePercentage < 0)
            expectedVolumeIndex = 0;

        if(expectedVolumeIndex != -1)
        {
            float actualVolumeIndex = AudioUtils.GetVolumeIndex(appContext, audioType);
            assertTrue("audio type: " + audioType.name()
                    + ", given volume percentage: " + volumePercentage
                    + ", expected volume index: " + expectedVolumeIndex
                    + ", actual volume: " + actualVolumeIndex,
                    actualVolumeIndex == expectedVolumeIndex);
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

        int expectedVolumeIndex = volumeIndex;
        if((audioType == AudioType.VOICECALL)
                && volumeIndex < 1)
            expectedVolumeIndex = 1;
        else if(volumeIndex > maxVolumeIndex)
            expectedVolumeIndex = maxVolumeIndex;
        else if(volumeIndex < 0)
            expectedVolumeIndex = 0;

        assertTrue("audio type: " + audioType.name()
                + ", given volume: " + volumeIndex
                + ", expected volume: " + expectedVolumeIndex
                + ", actual volume: " + actualVolumeIndex,
                expectedVolumeIndex == actualVolumeIndex);
    }
}