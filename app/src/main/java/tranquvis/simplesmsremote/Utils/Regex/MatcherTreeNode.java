package tranquvis.simplesmsremote.Utils.Regex;

import android.support.annotation.Nullable;

import java.util.ArrayList;
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

    private MatchResult lastMatchResult;

    /**
     * Create regex matcher tree node.
     *
     * @param patternTreeNode the related pattern node if available
     * @param childNodes      the children if available
     */
    MatcherTreeNode(@Nullable PatternTreeNode patternTreeNode,
                    @Nullable List<MatcherTreeNode> childNodes) {
        this.patternTreeNode = patternTreeNode;
        this.childNodes = childNodes != null ? childNodes : new ArrayList<MatcherTreeNode>();
    }

    public String getInput() {
        return input;
    }

    public PatternTreeNode getPattern() {
        return patternTreeNode;
    }

    public MatchResult getLastMatchResult() {
        return lastMatchResult;
    }

    /**
     * Test input through the whole tree recursively.
     *
     * @param input the input string, which should be tested
     * @return if input matches
     */
    public boolean testInput(String input) {
        this.input = input;
        return testInput();
    }

    /**
     * get first node with pattern id recursively
     *
     * @param patternId the pattern id
     * @return the first node with the given pattern id
     */
    public MatcherTreeNode getNodeByPatternId(String patternId) {
        if (patternTreeNode.id.equals(patternId))
            return this;
        else {
            for (MatcherTreeNode childNode : childNodes) {
                MatcherTreeNode n = childNode.getNodeByPatternId(patternId);
                if (n != null) return n;
            }
        }
        return null;
    }

    /**
     * Test input through the whole tree recursively.
     *
     * @return if input matches or pattern is not defined
     */
    private boolean testInput() {
        if (input == null || patternTreeNode == null)
            return true;

        if (matcher == null)
            matcher = patternTreeNode.getMatcher(input);
        else
            matcher.reset();

        MatchType childMatchType = patternTreeNode.childMatchType;

        // find all regexGroups
        List<String> regexGroups = new ArrayList<>();
        int matchCount = 0;
        while (matcher.find()) {
            matchCount++;
            if (childMatchType == MatchType.DO_NOT_MATCH)
                break;

            // Note that the first group equals the full input.
            for (int groupIndex = 1; groupIndex <= matcher.groupCount(); groupIndex++) {
                String group = matcher.group(groupIndex);
                if (group != null)
                    regexGroups.add(group);
            }
        }

        if (matchCount == 0) {
            // match failed
            lastMatchResult = new MatchResult(false, "Pattern does not match.");
            return false;
        }

        // at least 1 match has been found
        lastMatchResult = new MatchResult(true, null);

        // test child nodes
        if (childMatchType != MatchType.DO_NOT_MATCH) {
            if (childMatchType == MatchType.BY_INDEX
                    || childMatchType == MatchType.BY_INDEX_STRICT
                    || childMatchType == MatchType.BY_INDEX_IF_NOT_EMPTY) {
                for (int i = 0; i < regexGroups.size(); i++) {
                    //retrieve child input
                    String regexGroup = regexGroups.get(i);
                    MatcherTreeNode matcherChild;
                    if (i >= childNodes.size()) {
                        if (childMatchType == MatchType.BY_INDEX_STRICT) {
                            lastMatchResult = new MatchResult(false,
                                    "No child-pattern was found with the group index "
                                            + String.valueOf(i) + ".");
                            return false;
                        } else {
                            // add new empty child if no pattern was defined for it
                            matcherChild = new MatcherTreeNode(null, null);
                            childNodes.add(matcherChild);
                        }
                    } else {
                        matcherChild = childNodes.get(i);
                    }

                    //test input
                    matcherChild.input = regexGroup;
                    if (!(childMatchType == MatchType.BY_INDEX_IF_NOT_EMPTY
                            && matcherChild.input.isEmpty())) {
                        if (!matcherChild.testInput())
                            return false;
                    }
                }
            } else if (childMatchType == MatchType.BY_CHILD_PATTERN
                    || childMatchType == MatchType.BY_CHILD_PATTERN_STRICT) {
                for (int i = 0; i < regexGroups.size(); i++) {
                    //retrieve child input
                    String regexGroup = regexGroups.get(i);
                    MatcherTreeNode matcherChild = null;

                    for (MatcherTreeNode childNode : childNodes) {
                        childNode.matcher = childNode.patternTreeNode.getMatcher(regexGroup);

                        if (childNode.matcher.matches()) {
                            matcherChild = childNode;
                            break;
                        }
                    }

                    if (matcherChild == null) {
                        if (childMatchType == MatchType.BY_CHILD_PATTERN_STRICT) {
                            lastMatchResult = new MatchResult(false,
                                    "No child-pattern found that matches this group of input. (index: "
                                            + String.valueOf(i) + ").");
                            return false;
                        } else {
                            // add new empty child if no pattern was defined for it
                            matcherChild = new MatcherTreeNode(null, null);
                            childNodes.add(matcherChild);
                        }
                    }

                    //test input
                    matcherChild.input = regexGroup;
                    if (!matcherChild.testInput())
                        return false;
                }
            }
        }

        return true;
    }

    public List<MatcherTreeNode> getFailedNodesOfLastMatch() {
        List<MatcherTreeNode> failedNodes = new ArrayList<>();

        if (this.lastMatchResult == null)
            return failedNodes;

        if (!this.lastMatchResult.success)
            failedNodes.add(this);

        for (MatcherTreeNode childNode : childNodes) {
            failedNodes.addAll(childNode.getFailedNodesOfLastMatch());
        }
        return failedNodes;
    }
}

