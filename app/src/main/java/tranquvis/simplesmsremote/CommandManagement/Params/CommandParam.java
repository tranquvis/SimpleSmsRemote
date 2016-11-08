package tranquvis.simplesmsremote.CommandManagement.Params;

public abstract class CommandParam<T>
{
    private String id;

    public CommandParam(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract T getValueFromInput(String input) throws Exception;

    public boolean isAssigned(String input)
    {
        return input != null && !input.equals("");
    }
}
