package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.content.Context;

import tranquvis.simplesmsremote.CommandManagement.Commands.CommandStartAudioRecording;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandStopAudioRecording;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.AppUtils;

/**
 * Created by Andreas Kaltenleitner on 22.1.2018.
 */

public class ModuleAudioRecording extends Module {
    public final CommandStartAudioRecording commandStartAudioRecording = new CommandStartAudioRecording(this);
    public final CommandStopAudioRecording commandStopAudioRecording = new CommandStopAudioRecording(this);

    public ModuleAudioRecording() {
        this.titleRes = R.string.control_module_title_audio_recording;
        this.descriptionRes = R.string.control_module_desc_audio_recording;
        this.iconRes = R.drawable.ic_mic_grey_700_36dp;
    }

    @Override
    public boolean isCompatible(Context context) {
        return super.isCompatible(context)
                && AppUtils.isAppInstalled(context,"com.github.axet.audiorecorder");
    }
}
