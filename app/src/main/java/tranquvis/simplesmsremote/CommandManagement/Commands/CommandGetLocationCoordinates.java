package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.LocationUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Kaltenleitner Andreas on 26.10.2016.
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
    public void execute(Context context, CommandInstance commandInstance, CommandExecResult result,
            DataManager dataManager)
            throws Exception {
        Location location = LocationUtils.GetLocation(context, 20000);
        if (location == null)
            throw new Exception("Location Request timed out");

        String locationDescription = String.format(Locale.ENGLISH, "%1$.4f %2$.4f",
                location.getLatitude(), location.getLongitude());
        // get timestamp in RFC3339 format
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
                .format(new Date(location.getTime()));
        String accuracy = location.hasAccuracy()
                ? String.format(Locale.getDefault(), "%1$.0fm radius (68%% probability)", location.getAccuracy())
                : context.getString(R.string.unknown_accuracy);
        // see https://developer.android.com/reference/android/location/Location.html#getAccuracy()
        result.setCustomResultMessage(context.getString(
                R.string.result_msg_location_coordinates, locationDescription, timestamp, accuracy));
        result.setForceSendingResultSmsMessage(true);
    }
}
