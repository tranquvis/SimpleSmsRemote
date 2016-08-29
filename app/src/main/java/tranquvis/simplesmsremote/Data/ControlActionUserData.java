package tranquvis.simplesmsremote.Data;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andreas Kaltenleitner on 29.08.2016.
 */
public class ControlActionUserData
{
    private String controlActionId;
    private List<String> grantedPhones;

    public ControlActionUserData(String controlActionId, List<String> grantedPhones)
    {
        this.controlActionId = controlActionId;
        this.grantedPhones = grantedPhones;
    }

    /**
     * parse ControlActionUserDatafrom text line
     * @param textLine line of text
     * @return user data
     */
    public static ControlActionUserData Parse(String textLine)
    {
        String[] parts = textLine.split(":");
        String id = parts[0];
        String[] dataParts = parts[1].split(";");
        String[] phones = dataParts[0].split(",");

        return new ControlActionUserData(id, Arrays.asList(phones));
    }

    public String getControlActionId()
    {
        return controlActionId;
    }

    public List<String> getGrantedPhones()
    {
        return grantedPhones;
    }

    public String toTextLine()
    {
        return controlActionId + ":" + StringUtils.join(grantedPhones, ',');
    }
}
