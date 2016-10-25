package tranquvis.simplesmsremote.CommandManagement;

import android.content.Context;

import org.apache.commons.lang3.ArrayUtils;

import tranquvis.simplesmsremote.Sms.MyCommandMessage;

/**
 * Created by Andi on 07.10.2016.
 */

public class CommandInstance {
    private ControlCommand command;
    private String commandText;

    /**
     * Create command instance.
     * @param command the underlying control command
     */
    private CommandInstance(ControlCommand command) {
        this(command, new String[command.getParamNames().length], "");
    }

    /**
     * Create command instance and fill it with parameters
     * @param command the underlying control command
     * @param params command parameters
     * @param commandText the command text which represents this instance
     */
    private CommandInstance(ControlCommand command, String[] params, String commandText) {
        this.command = command;
        this.params = params;
        this.commandText = commandText;
    }

    private void setParam(int index, String value)
    {
        params[index] = value;
    }

    private void setParam(String name, String value)
    {
        params[ArrayUtils.indexOf(command.getParamNames(), name)] = value;
    }

    public String getParam(int index)
    {
        return params[index];
    }

    public String getParam(String name)
    {
        return params[ArrayUtils.indexOf(command.getParamNames(), name)];
    }

    public ControlCommand getCommand() {
        return command;
    }

    public String[] getParams() {
        return params;
    }

    public String getCommandText() {
        return commandText;
    }

    @Override
    public String toString() {
        return getCommandText();
    }

    /**
     * get control command instance from command text
     * @param command command text
     * @return control command instance
     * @throws Exception
     */
    public static CommandInstance CreateFromCommand(String command) throws Exception
    {
        String commandNorm = command.trim().toLowerCase();

        for (ControlCommand com : ControlCommand.GetAllCommands())
        {
            CommandInstance commandInstance = new CommandInstance(com);
            commandInstance.commandText = command;

            String commandTemplateNorm = com.getTitle();

            boolean match = true; //assume that the template matches

            boolean inWhitespace = false;

            boolean inParam = false;
            char paramAfterC = 0;
            int paramStartIndex = 0;
            int paramPos = 0;

            boolean inQuotation = false;
            boolean paramWrappedWithQuotation = false;
            char quotationC = 0;

            for(int i = 0, ti = 0; i <= commandNorm.length();)
            {
                if(i == commandNorm.length()) //end reached
                {
                    if(inQuotation) //unclosed quotations are not allowed
                    {
                        match = false;
                    }
                    if(paramAfterC == 0) //save param, if it reaches to end
                    {
                        match = true;

                        //add param
                        if(paramWrappedWithQuotation)
                        {
                            commandInstance.setParam(paramPos, commandNorm.substring(
                                    paramStartIndex + 1, commandNorm.length() - 1));
                        }
                        else
                        {
                            commandInstance.setParam(paramPos, commandNorm.substring(
                                    paramStartIndex));
                        }
                    }
                    break;
                }

                char c = commandNorm.charAt(i);
                char tc = commandTemplateNorm.charAt(ti);

                if(inParam)
                {
                    if(inQuotation && (c == quotationC))
                    {
                        inQuotation = false;
                    }

                    if(!inQuotation && paramAfterC != 0 && (c == paramAfterC
                            || (Character.isWhitespace(c) && Character.isWhitespace(paramAfterC))))
                    {
                        //param end reached
                        inParam = false;
                        match = true;

                        //add param
                        if(paramWrappedWithQuotation)
                        {
                            commandInstance.setParam(paramPos, commandNorm.substring(
                                    paramStartIndex + 1, i - 1));
                        }
                        else
                        {
                            commandInstance.setParam(paramPos, commandNorm.substring(
                                    paramStartIndex, i));
                        }
                        paramPos++;

                        i++; ti++;
                    }
                    else
                    {
                        i++;
                    }

                }
                else if(tc == '[')
                {
                    inParam = true;
                    match = false;
                    int pei = commandTemplateNorm.indexOf(']', ti);
                    if(pei == -1)
                        throw new Exception("Invalid command template syntax");
                    //check if param reaches end of command
                    if(pei == commandTemplateNorm.length() - 1) {
                        //reaches end
                        paramAfterC = 0;
                        ti = pei;
                    }
                    else {
                        paramAfterC = commandTemplateNorm.charAt(pei + 1);
                        ti = pei + 1;
                    }

                    paramStartIndex = i;

                    //check if param is wrapped with quotations
                    if(c == '"' || c == '\'')
                    {
                        inQuotation = true;
                        paramWrappedWithQuotation = true;
                        quotationC = c;
                    }
                    else
                        paramWrappedWithQuotation = false;

                    i++;
                }
                else if(Character.isWhitespace(c) && Character.isWhitespace(tc))
                {
                    inWhitespace = true;
                    i++; ti++;
                }
                else if(c == tc) {
                    if(inWhitespace && !Character.isWhitespace(c))
                        inWhitespace = false;
                    i++; ti++;
                }
                else if(inWhitespace)
                {
                    if(!Character.isWhitespace(c) && !Character.isWhitespace(tc))
                    {
                        match = false;
                        break;
                    }
                    if(Character.isWhitespace(c)) i++;
                    if(Character.isWhitespace(tc)) ti++;
                }
                else
                {
                    match = false;
                    break;
                }
            }
            if(match)
            {
                //command matches with template
                return commandInstance;
            }
        }
        return null;
    }

    /**
     * execute control command
     * @param context app context
     * @param controlSms command message
     * @return execution result
     */
    public CommandExecResult executeCommand(Context context, MyCommandMessage controlSms)
    {
        return new CommandExec(this, context).execute(context, controlSms);
    }
}
