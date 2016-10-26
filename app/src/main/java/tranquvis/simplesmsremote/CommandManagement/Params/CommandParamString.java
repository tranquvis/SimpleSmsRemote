package tranquvis.simplesmsremote.CommandManagement.Params;

/**
 * Created by Andreas Kaltenleitner on 26.10.2016.
 */

public class CommandParamString extends CommandParam<String>
{
    public CommandParamString(String id) {
        super(id);
    }

    @Override
    public String getValueFromInput(String input) {
        return input;
    }
}
