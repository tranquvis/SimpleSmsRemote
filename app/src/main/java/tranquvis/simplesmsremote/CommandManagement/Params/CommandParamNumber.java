package tranquvis.simplesmsremote.CommandManagement.Params;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Kaltenleitner Andreas on 30.10.2016.
 */

public class CommandParamNumber extends CommandParam<Double> {
    public CommandParamNumber(String id) {
        super(id);
    }

    @Override
    public Double getValueFromInput(String input) throws ParseException {
        return DecimalFormat.getInstance(Locale.getDefault()).parse(input).doubleValue();
    }
}
