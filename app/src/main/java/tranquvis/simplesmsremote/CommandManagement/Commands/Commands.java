package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import tranquvis.simplesmsremote.CommandManagement.Command;

/**
 * Created by Andreas Kaltenleitner on 26.10.2016.
 */

public class Commands {
    public static final Command
            TAKE_PICTURE_WITH_OPTIONS = new CommandTakePictureWithOptions(),
            TAKE_PICTURE = new CommandTakePicture(),
            SET_WIFI_STATE = new CommandSetWifiState(),
            GET_WIFI_STATE = new CommandGetWifiState(),
            SET_HOTSPOT_STATE = new CommandSetHotspotState(),
            GET_HOTSPOT_STATE = new CommandGetHotspotState(),
            SET_MOBILE_DATA_STATE = new CommandSetMobileDataState();


    /**
     * Get all Commands by using reflection.
     * @return all defined control commands
     */
    public static List<Command> GetAll()
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
