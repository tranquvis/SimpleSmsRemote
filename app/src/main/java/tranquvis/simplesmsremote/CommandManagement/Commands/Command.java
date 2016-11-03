package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 29.08.2016.
 */
public abstract class Command
{
    private static final List<Command> commands = new ArrayList<>();

    @Language("RegExp")
    protected static final String PATTERN_MULTI_PARAMS =
            "(?i)(?<!$)(?:(?:\\s+)?(.*?)(?:\\s+)?(?:and|,|$))";

    @Language("RegExp")
    protected static final String PATTERN_TEMPLATE_SET_STATE_ON_OFF =
                "(?i)^\\s*((enable|disable)\\s+(%1$s))" +
                "|(turn\\s+(%1$s)\\s+(on|off))|(turn\\s+(on|off)\\s+(%1$s))" +
                "|(set\\s+(%1$s)(\\s+state)?\\s+to)\\s*(on|off|enabled|disabled)$";

    @Language("RegExp")
    protected static final String PATTERN_TEMPLATE_GET_STATE_ON_OFF =
                    "(?i)^\\s*(((is\\s+)?(%1$s)\\s+(enabled|disabled|on|off)(\\?)?)" +
                    "|((get|fetch|retrieve)\\s+(%1$s)\\s+state))\\s*$";

    protected static String GetPatternFromTemplate(String template,
                                                   @Language("RegExp") String... values)
    {
        return String.format(template, (Object[]) values);
    }

    protected static String AdaptSimplePattern(@Language("RegExp") String pattern)
    {
        return "(?i)^\\s*" + pattern.replace(" ", "\\s+") + "\\s*$";
    }
    /*
    static
    {
        DISPLAY_GET_BRIGHTNESS = new Command("get brightness");
        DISPLAY_SET_BRIGHTNESS = new Command("set brightness to [" + PARAM_BRIGHTNESS + "]");
        DISPLAY_GET_OFF_TIMEOUT = new Command("get display off timeout");
        DISPLAY_SET_OFF_TIMEOUT = new Command("set display off timeout to ["
                + PARAM_DISPLAY_OFF_TIMEOUT + "]");
        DISPLAY_TURN_OFF = new Command("turn display off");
    }
*/

    protected String typeId = getClass().getName();
    @StringRes
    protected int titleRes;
    protected String[] syntaxDescList;
    protected PatternTreeNode patternTree;
    protected Module module;

    /**
     * Create and register command.
     * @param module related module
     */
    protected Command(@Nullable Module module) {
        this.module = module;
        commands.add(this);
    }

    public int getTitleRes()
    {
        return titleRes;
    }

    public String[] getSyntaxDescList() {
        return syntaxDescList;
    }

    public PatternTreeNode getPatternTree() {
        return patternTree;
    }

    public Module getModule()
    {
        return module;
    }

    public abstract void execute(Context context, CommandInstance commandInstance,
                                    CommandExecResult result) throws Exception;

    /**
     * Get all registered commands.
     * @param sortComparator This comparator is used to sort the list.
     * @return (sorted) list of all commands
     * @see Comparator
     */
    public static List<Command> GetAllCommands(@Nullable Comparator<Command> sortComparator)
    {
        Instances.InitCommands();
        List<Command> commandsSorted = new ArrayList<>(commands);
        if(sortComparator != null)
            Collections.sort(commandsSorted, sortComparator);
        return commands;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Command)) return false;

        Command command = (Command) o;

        return typeId != null ? typeId.equals(command.typeId) : command.typeId == null;

    }

    @Override
    public int hashCode()
    {
        return typeId != null ? typeId.hashCode() : 0;
    }
}

