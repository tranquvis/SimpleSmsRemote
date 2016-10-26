package tranquvis.simplesmsremote.CommandManagement.Commands;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.Command;

/**
 * Created by Andreas Kaltenleitner on 26.10.2016.
 */

public class Commands {
    public static final Command TAKE_PICTURE_WITH_OPTIONS =
            new CommandTakePictureWithOptions();
    public static final Command TAKE_PICTURE = new CommandTakePicture();
    public static final Command SET_WIFI_STATE = new CommandSetWifiState();
    public static final Command GET_WIFI_STATE = new CommandGetWifiState();

    /**
     * Get all Commands by using reflection.
     * @return all defined control commands
     */
    public static List<Command> GetAllCommands()
    {
        List<Command> commands = new ArrayList<>();

        for (Field field : Command.class.getDeclaredFields())
        {

            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && field.getType() == Command.class)
            {
                try
                {
                    commands.add((Command) field.get(null));
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return commands;
    }
}
