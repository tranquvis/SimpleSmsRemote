package tranquvis.simplesmsremote.CommandManagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import org.intellij.lang.annotations.Language;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 29.08.2016.
 */
public abstract class Command
{
    private static final List<Command> commands = new ArrayList<>();

    @Language("RegExp")
    protected static final String PATTERN_MULTI_PARAMS = "(?!$)(?:\\s*?(.*?)\\s*?(?:and|,|$))\\s*";

    @Language("RegExp")
    protected static final String
            PATTERN_TEMPLATE_SET_STATE_ON_OFF =
                "(?i)^\\s*((enable|disable)\\s+(%1$s))" +
                "|(turn\\s+(%1$s)\\s+(on|off))|(turn\\s+(on|off)\\s+(%1$s))" +
                "|(set\\s+(%1$s)\\s+state\\s+to)\\s*(on|off|enabled|disabled)$",
            PATTERN_TEMPLATE_GET_STATE_ON_OFF =
                    "(?i)^\\s*(((is\\s+)?(wifi|wlan)\\s+(enabled|disabled|on|off)(\\?)?)" +
                    "|(get\\s+(wifi|wlan)\\s+state))\\s*$";

    protected static String GetPatternFromTemplate(String template,
                                                   @Language("RegExp") String... values)
    {
        return String.format(template, (Object[]) values);
    }

    /*
    static
    {
        BATTERY_LEVEL_GET = new Command("get battery level");
        BATTERY_IS_CHARGING = new Command("is battery charging");

        LOCATION_GET = new Command("get location");

        BLUETOOTH_ENABLE = new Command("enable bluetooth");
        BLUETOOTH_DISABLE = new Command("disable bluetooth");
        BLUETOOTH_IS_ENABLED = new Command("is bluetooth enabled");

        AUDIO_SET_VOLUME = new Command("set volume [" + PARAM_AUDIO_TYPE + "] to [" +
                PARAM_AUDIO_VOLUME + "]");
        AUDIO_GET_VOLUME = new Command("get volume for [" + PARAM_AUDIO_TYPE + "]");
        AUDIO_GET_VOLUME_PERCENTAGE = new Command("get volume percentage for [" +
                PARAM_AUDIO_TYPE + "]");

        DISPLAY_GET_BRIGHTNESS = new Command("get brightness");
        DISPLAY_SET_BRIGHTNESS = new Command("set brightness to [" + PARAM_BRIGHTNESS + "]");
        DISPLAY_GET_OFF_TIMEOUT = new Command("get display off timeout");
        DISPLAY_SET_OFF_TIMEOUT = new Command("set display off timeout to ["
                + PARAM_DISPLAY_OFF_TIMEOUT + "]");
        DISPLAY_TURN_OFF = new Command("turn display off");
    }
*/

    protected Module module;

    @StringRes
    protected int titleRes;
    protected String[] syntaxDescList;
    protected PatternTreeNode patternTree;

    /**
     * Create and register command.
     * @param module related module
     */
    protected Command(@NonNull Module module) {
        if(module == null)
            throw new IllegalArgumentException("module must not be null");
        this.module = module;
        commands.add(this);
    }

    public int getTitleRes()
    {
        return titleRes;
    }

    public Module getModule()
    {
        return module;
    }

    protected abstract void execute(Context context, CommandInstance commandInstance,
                                    CommandExecResult result) throws Exception;

    /**
     * Get all registered commands.
     * @param sortComparator This comparator is used to sort the list.
     * @return (sorted) list of all commands
     * @see Comparator
     */
    public static List<Command> GetAllCommands(@Nullable Comparator<Command> sortComparator)
    {
        List<Command> commandsSorted = new ArrayList<>(commands);
        if(sortComparator != null)
            Collections.sort(commandsSorted, sortComparator);
        return commands;
    }
}

