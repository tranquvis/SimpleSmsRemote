package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.Nullable;

import org.intellij.lang.annotations.Language;

import tranquvis.simplesmsremote.CommandManagement.Command;
import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamOnOff;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.BluetoothUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 28.10.2016.
 */
public class CommandSetBluetoothState extends Command
{
    final static CommandParamOnOff PARAM_BLUETOOTH_STATE = new CommandParamOnOff("bluetooth_state");

    @Language("RegExp")
    private static final String
            PATTERN_ROOT = GetPatternFromTemplate(PATTERN_TEMPLATE_SET_STATE_ON_OFF, "bluetooth");

    public CommandSetBluetoothState(@Nullable Module module)
    {
        super(module);

        this.titleRes = R.string.command_title_set_bluetooth_state;
        this.syntaxDescList = new String[]{
                "enable bluetooth",
                "disable bluetooth"
        };
        this.patternTree = new PatternTreeNode(PARAM_BLUETOOTH_STATE.getId(),
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }

    @Override
    protected void execute(Context context, CommandInstance commandInstance,
                           CommandExecResult result) throws Exception
    {
        BluetoothUtils.SetBluetoothState(commandInstance.getParam(PARAM_BLUETOOTH_STATE));
    }
}