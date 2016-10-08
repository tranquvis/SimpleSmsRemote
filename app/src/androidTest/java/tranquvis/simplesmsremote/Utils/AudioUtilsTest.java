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
        testSetVolumePercentage(AudioType.MUSIC, 45);
        testSetVolumePercentage(AudioType.RING, 100);
        testSetVolumePercentage(AudioType.RING, 0);
    }

    @Test
    @ExecSequentially("audio")
    public void setVolumeIndex() throws Exception
    {
        testSetVolumeIndex(AudioType.MUSIC, 0);
        testSetVolumeIndex(AudioType.RING, 3);
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

    private float spreadVolume = -1;
    private void testSetVolumePercentage(AudioUtils.AudioType audioType, float volumePercentage)
    {
        if(spreadVolume == -1)
        {
            AudioManager audioManager = (AudioManager)
                    appContext.getSystemService(Context.AUDIO_SERVICE);
            spreadVolume = 100f / audioManager.getStreamMaxVolume(audioType.getStreamType()) / 2f;
        }

        AudioUtils.SetVolumePercentage(appContext, volumePercentage, audioType);
        float actualVolume = AudioUtils.GetVolumePercentage(appContext, audioType);
        assertTrue(actualVolume < volumePercentage + spreadVolume
                && actualVolume > volumePercentage - spreadVolume);
    }

    private void testSetVolumeIndex(AudioUtils.AudioType audioType, int volumeIndex)
    {
        AudioUtils.SetVolumeIndex(appContext, volumeIndex, audioType);
        int actualVolumeIndex = AudioUtils.GetVolumeIndex(appContext, audioType);
        assertTrue(volumeIndex == actualVolumeIndex);
    }
}