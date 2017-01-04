package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.LocationUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 26.10.2016.
 */

public class CommandGetLocationCoordinates extends Command {

    private static final String PATTERN_ROOT = AdaptSimplePattern(
            "(get|fetch|retrieve) (location|position|location coordinates|coordinates)");

    public CommandGetLocationCoordinates(@Nullable Module module) {
        super(module);

        this.titleRes = R.string.command_title_get_location_coordinates;
        this.syntaxDescList = new String[]{
                "get location"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance, CommandExecResult result)
            throws Exception {
        Location location = LocationUtils.GetLocation(context, 20000);
        if (location == null)
            throw new Exception("Location Request timed out");
        result.setCustomResultMessage(context.getString(
                R.string.result_msg_location_coordinates,
                location.getLatitude(), location.getLongitude()));
        result.setForceSendingResultSmsMessage(true);
    }
}
