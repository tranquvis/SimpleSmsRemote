package tranquvis.simplesmsremote.CommandManagement.Params;

/**
 * Created by Andreas Kaltenleitner on 08.11.2016.
 */

public class CommandParamEmpty extends CommandParam<Void>
{
    public CommandParamEmpty(String id)
    {
        super(id);
    }

    @Override
    public Void getValueFromInput(String input) throws Exception
    {
        return null;
    }
}
