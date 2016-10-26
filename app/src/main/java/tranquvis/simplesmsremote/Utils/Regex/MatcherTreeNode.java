package tranquvis.simplesmsremote.Utils.Regex;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by Andreas Kaltenleitner on 25.10.2016.
 */

public class MatcherTreeNode {
    private PatternTreeNode patternTreeNode;
    private List<MatcherTreeNode> childNodes;

    private String input;
    private Matcher matcher;
    private boolean nodeMatchSuccess;

    /**
     * Create regex matcher tree node.
     * @param patternTreeNode the related pattern node if available
     * @param childNodes the children if available
     */
    MatcherTreeNode(@Nullable PatternTreeNode patternTreeNode,
                    @Nullable List<MatcherTreeNode> childNodes) {
        this.patternTreeNode = patternTreeNode;
        this.childNodes = childNodes != null ? childNodes : new ArrayList<MatcherTreeNode>();
    }

    public String getInput() {
        return input;
    }

    /**
     * Test input through the whole tree recursively.
     * @param input the input string, which should be tested
     * @return if input matches
     */
    public boolean testInput(String input)
    {
        this.input = input;
        return testInput();
    }

    /**
     * get first node with pattern id recursively
     * @param patternId the pattern id
     * @return the first node with the given pattern id
     */
    public MatcherTreeNode getNodeByPatternId(String patternId)
    {
        if(patternTreeNode.id.equals(patternId))
            return this;
        else
        {
            for (MatcherTreeNode childNode : childNodes)
            {
                MatcherTreeNode n = childNode.getNodeByPatternId(patternId);
                if(n != null) return n;
            }
        }
        return null;
    }

    /**
     * Test input through the whole tree recursively.
     * @return if input matches or pattern is not defined
     */
    private boolean testInput()
    {
        if(input == null || patternTreeNode == null)
            return true;

        if(matcher == null)
            matcher = patternTreeNode.getMatcher(input);
        else
            matcher.reset();

        MatchType childMatchType = patternTreeNode.childMatchType;

        // find all regexGroups
        List<String> regexGroups = new ArrayList<>();
        int matchCount = 0;
        while(matcher.find())
        {
            matchCount++;
            if(childMatchType == MatchType.DO_NOT_MATCH)
                break;

            // Note that the first group equals the full input.
            for (int groupIndex = 1; groupIndex <= matcher.groupCount(); groupIndex++)
            {
                String group = matcher.group(groupIndex);
                regexGroups.add(group);
            }
        }

        if(matchCount == 0)
        {
            // match failed
            nodeMatchSuccess = false;
            return false;
        }

        // at least 1 match has been found
        nodeMatchSuccess = true;

        // test child nodes
        if(childMatchType != MatchType.DO_NOT_MATCH)
        {
            if(childMatchType == MatchType.BY_INDEX
                    || childMatchType == MatchType.BY_INDEX_STRICT)
            {
                for (int i = 0; i < regexGroups.size(); i++)
                {
                    //retrieve child input
                    String regexGroup = regexGroups.get(i);
                    MatcherTreeNode matcherChild;
                    if(i >= childNodes.size())
                    {
                        if (childMatchType == MatchType.BY_INDEX_STRICT)
                        {
                            nodeMatchSuccess = false;
                            return false;
                        }
                        else
                        {
                            // add new empty child if no pattern was defined for it
                            matcherChild = new MatcherTreeNode(null, null);
                            childNodes.add(matcherChild);
                        }
                    }
                    else
                    {
                        matcherChild = childNodes.get(i);
                    }

                    //test input
                    matcherChild.input = regexGroup;
                    if(!matcherChild.testInput())
                        return false;
                }
            }
            else if(childMatchType == MatchType.BY_CHILD_PATTERN
                    || childMatchType == MatchType.BY_CHILD_PATTERN_STRICT)
            {
                for (int i = 0; i < regexGroups.size(); i++)
                {
                    //retrieve child input
                    String regexGroup = regexGroups.get(i);
                    MatcherTreeNode matcherChild = null;

                    for (MatcherTreeNode childNode : childNodes)
                    {
                        childNode.matcher = childNode.patternTreeNode.getMatcher(regexGroup);

                        if(childNode.matcher.matches())
                        {
                            matcherChild = childNode;
                            break;
                        }
                    }

                    if(matcherChild == null)
                    {
                        if (childMatchType == MatchType.BY_CHILD_PATTERN_STRICT)
                        {
                            nodeMatchSuccess = false;
                            return false;
                        }
                        else
                        {
                            // add new empty child if no pattern was defined for it
                            matcherChild = new MatcherTreeNode(null, null);
                            childNodes.add(matcherChild);
                        }
                    }

                    //test input
                    matcherChild.input = regexGroup;
                    if(!matcherChild.testInput())
                        return false;
                }
            }
        }

        return true;
    }
}
