package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.DisplayUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 31.10.2016.
 */
public class CommandTurnDisplayOff extends Command {

    private static final String PATTERN_ROOT = AdaptSimplePattern("turn (display|screen) off");

    public CommandTurnDisplayOff(@NonNull Module module) {
        super(module);

        this.titleRes = R.string.command_title_turn_display_off;
        this.syntaxDescList = new String[]{
                "turn display off"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result, DataManager dataManager) throws Exception {
        DisplayUtils.TurnScreenOff(context);
        Thread.sleep(2000); // otherwise the notification forces the screen to turn on again
    }
}