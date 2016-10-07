package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.media.AudioManager;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.Aspects.ExecSequentially.ExecSequentially;
import tranquvis.simplesmsremote.Helper.AudioHelper.AudioType;

import static org.junit.Assert.*;

/**
 * Created by Andreas Kaltenleitner on 07.10.2016.
 */
public class AudioHelperTest extends AppContextTest
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
        AudioHelper.GetVolumeIndex(appContext, AudioHelper.AudioType.MUSIC);
    }

    @Test
    @ExecSequentially("audio")
    public void getVolumeIndex() throws Exception
    {
        AudioHelper.GetVolumeIndex(appContext, AudioHelper.AudioType.MUSIC);
    }

    private float spreadVolume = -1;
    private void testSetVolumePercentage(AudioHelper.AudioType audioType, float volumePercentage)
    {
        if(spreadVolume == -1)
        {
            AudioManager audioManager = (AudioManager)
                    appContext.getSystemService(Context.AUDIO_SERVICE);
            spreadVolume = 100f / audioManager.getStreamMaxVolume(audioType.getStreamType()) / 2f;
        }

        AudioHelper.SetVolumePercentage(appContext, volumePercentage, audioType);
        float actualVolume = AudioHelper.GetVolumePercentage(appContext, audioType);
        assertTrue(actualVolume < volumePercentage + spreadVolume
                && actualVolume > volumePercentage - spreadVolume);
    }

    private void testSetVolumeIndex(AudioHelper.AudioType audioType, int volumeIndex)
    {
        AudioHelper.SetVolumeIndex(appContext, volumeIndex, audioType);
        int actualVolumeIndex = AudioHelper.GetVolumeIndex(appContext, audioType);
        assertTrue(volumeIndex == actualVolumeIndex);
    }
}