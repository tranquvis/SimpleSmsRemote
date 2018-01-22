package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 22.1.2018.
 */
public class CommandStopAudioRecording extends Command {
    private static final String PATTERN_ROOT = AdaptSimplePattern(
            "(?:(?:stop|end) (?:audio )?(?:recording))");

    public CommandStopAudioRecording(@NonNull Module module) {
        super(module);

        this.titleRes = R.string.command_title_stop_audio_recording;
        this.syntaxDescList = new String[]{
                "stop audio recording"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result) throws Exception {
        Intent stopIntent = new Intent("com.github.axet.audiorecorder.STOP_RECORDING");
        context.sendBroadcast(stopIntent);
    }
}