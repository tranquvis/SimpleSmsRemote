package tranquvis.simplesmsremote.Utils.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by kalte on 25.10.2016.
 */

public class MatcherTreeNode {
    private PatternTreeNode patternTreeNode;
    private List<MatcherTreeNode> childNodes;

    private String input;
    private Matcher matcher;
    private boolean nodeMatchSuccess;

    MatcherTreeNode(PatternTreeNode patternTreeNode, List<MatcherTreeNode> childNodes) {
        this.patternTreeNode = patternTreeNode;
        this.childNodes = childNodes;
    }

    /**
     * Test input to match this tree.
     * @param input the input string, which should be tested
     * @return if input matches
     */
    public boolean testInput(String input)
    {
        this.input = input;
        matcher = patternTreeNode.getMatcher(input);

        List<String> groups = new ArrayList<>();
        int i = 0;
        while(matcher.find())
        {
            // Note that the first group equals the full input.
            for (int groupIndex = 1; groupIndex <= matcher.groupCount(); groupIndex++)
            {
                groups.add(matcher.group(groupIndex));
            }

            i++;
        }

        if(i == 0)
        {
            nodeMatchSuccess = false;
            return false;
        }
        else
        {
            //TODO
            for (MatcherTreeNode childNode : childNodes) {
                if(childNode.testInput())
            }
        }
    }
}
