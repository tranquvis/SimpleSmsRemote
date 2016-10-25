package tranquvis.simplesmsremote;

import android.support.annotation.Nullable;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Andreas Kaltenleitner on 25.10.2016.
 */

class CommandHelper{
    void register(Command c){

    }
}
abstract class Command{
    String[] keywords;


}

class Camera extends Command{
    String pattern = "take photo with (.*)";
    PatternTreeNode patternTree = new PatternTreeNode("take\\s+photo\\s+with\\s+(.*)",
        new PatternTreeNode[]{
            new PatternTreeNode("\\s*(flash|(?:front cam))(?:\\s+|$)", null)
        }
    );
}

class PatternTreeNode
{
    private String pattern;
    private PatternTreeNode[] childNodes;

    public PatternTreeNode(String pattern, @Nullable PatternTreeNode[] childNodes)
    {
        this.pattern = pattern;
        this.childNodes = childNodes;
    }
}



