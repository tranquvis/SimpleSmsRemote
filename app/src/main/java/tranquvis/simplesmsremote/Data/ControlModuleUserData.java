package tranquvis.simplesmsremote.Data;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andreas Kaltenleitner on 29.08.2016.
 */
public class ControlModuleUserData
{
    private String controlModuleId;
    private List<String> grantedPhones;

    public ControlModuleUserData(String controlModuleId, List<String> grantedPhones)
    {
        this.controlModuleId = controlModuleId;
        this.grantedPhones = grantedPhones;
    }

    /**
     * parse ControlActionUserDatafrom text line
     * @param textLine line of text
     * @return user data
     */
    public static ControlModuleUserData Parse(String textLine)
    {
        String[] parts = textLine.split(":");
        String id = parts[0];
        String[] dataParts = parts[1].split(";");
        String[] phones = dataParts[0].split(",");

        return new ControlModuleUserData(id, Arrays.asList(phones));
    }

    public String getControlModuleId()
    {
        return controlModuleId;
    }

    public List<String> getGrantedPhones()
    {
        return grantedPhones;
    }

    public String toTextLine()
    {
        return controlModuleId + ":" + StringUtils.join(grantedPhones, ',');
    }
}
