package tranquvis.simplesmsremote.CommandManagement.Params;

/**
 * Created by Andreas Kaltenleitner on 26.10.2016.
 */

public class CommandParamOnOff extends CommandParam<Boolean>
{
    public CommandParamOnOff(String id) {
        super(id);
    }

    @Override
    public Boolean getValueFromInput(String input) {
        return input.matches("^(?!(.*?\\s*?(no|disabled|off)($|\\s+)))");
    }
}
