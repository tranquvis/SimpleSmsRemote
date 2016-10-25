package tranquvis.simplesmsremote.CommandManagement;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tranquvis.simplesmsremote.AppContextTest;

import static org.junit.Assert.*;

/**
 * Created by Andreas Kaltenleitner on 25.10.2016.
 */
public class CommandExecTest extends AppContextTest
{
    @Test
    public void execute() throws Exception
    {
        Pattern p = Pattern.compile("take\\s+picture\\s+with\\s+(.*)");
        Matcher m = p.matcher("take picture with flash");

        List<String> groups;
        if(m.find())
        {
            groups = new ArrayList<>();
            for (int i = 0; i < m.groupCount(); i++)
            {
                groups.add(m.group(i));
            }
            Log.i("","");
        }
    }
}