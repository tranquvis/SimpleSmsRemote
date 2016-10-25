package tranquvis.simplesmsremote.CommandManagement;

import android.icu.text.DateIntervalInfo;
import android.media.MediaCodec;

import java.util.ArrayList;

import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternParam;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by kalte on 25.10.2016.
 */

public class CommandTakePictureWithOptions extends ControlCommand
{
    public static String PARAM_OPTIONS = "options";
    public static String PARAM_CAMERA = "camera";
    public static String PARAM_FLASH = "options";
    public static String PARAM_AUTOFOCUS = "autofocus";

    CommandTakePictureWithOptions() {
        super();
        titleRes = R.string.command_title_take_picture_with_options;
        syntaxDescList = new String[]{
                "take picture with [" + PARAM_OPTIONS + "]"
        };
        patternTree = new PatternTreeNode("root",
                "(?:take|capture)\\s+(?:picture|photo)\\s+with\\s+(.*)",
                MatchType.BY_INDEX_STRICT,
                new PatternTreeNode(PARAM_OPTIONS,
                        "\\s*(.*)(?:,|\\s+|$)",
                        MatchType.BY_CHILD_PATTERN_STRICT,
                        new PatternTreeNode(PARAM_CAMERA,
                                "\\d+" +
                                        "|((back|front)(\\s+cam(era)?)?" +
                                        "|(cam(era)?\\s+(back|front)))",
                                MatchType.DO_NOT_MATCH
                        ),
                        new PatternTreeNode(PARAM_FLASH,
                                "(flash(light)?(\\s+(enabled|disabled|on|off))?)" +
                                        "|(no\\s+flash(light)?)",
                                MatchType.DO_NOT_MATCH
                        ),
                        new PatternTreeNode(PARAM_AUTOFOCUS,
                                "autofocus(\\s+(on|enabled|off|disabled))?|" +
                                        "|(no\\s+autofocus)",
                                MatchType.DO_NOT_MATCH
                        )
                )
        );
    }

    @Override
    public void execute(CommandExec commandExecutor) {

    }
}
