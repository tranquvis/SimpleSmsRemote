package tranquvis.simplesmsremote.Utils;

/**
 * Created by Andreas Kaltenleitner on 08.11.2016.
 */

public class StringUtils
{
    /**
     * Convert whitespace of string to corresponding chars.
     * For example: accuracies of NEWLINE are converted to \n.
     * @return adapted string
     */
    public static String ConvertWhitespace(String string)
    {
        return string.replace("\n", "\\n").replace("\r", "\\r");
    }
}
