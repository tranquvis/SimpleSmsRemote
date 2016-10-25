package tranquvis.simplesmsremote.Utils.Regex;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTreeNode
{
    private String id;
    @Language("RegExp")
    private String regex;
    private List<PatternTreeNode> childNodes;
    private MatchType matchType;

    private Pattern pattern;

    /**
     * Create regex tree leaf.
     * @param id leaf identifier
     */
    public PatternTreeNode(@Nullable String id)
    {
        this.id = id;
    }

    /**
     * <p>Create regex tree node. </p>
     * <p>Note that the tree leafs must not be defined. If a children is not defined
     * during matching, it is added automatically, however without id.</p>
     * @param id node identifier
     * @param regex pattern
     * @param matchType defines how child values should be matched in the given childNodes
     * @param childNodes predefined children of node
     */
    public PatternTreeNode(String id, @Language("RegExp") String regex, MatchType matchType,
                           @Nullable PatternTreeNode... childNodes)
    {
        this.id = id;
        this.regex = regex;
        if(childNodes != null) {
            Collections.addAll(this.childNodes, childNodes);
        }
    }

    Matcher getMatcher(String input) {
        return pattern.matcher(input);
    }

    public void compile()
    {
        if(regex != null)
            pattern = Pattern.compile(regex);
    }

    public MatcherTreeNode matcherTree()
    {
        List<MatcherTreeNode> matcherChildren = new ArrayList<>();
        for (PatternTreeNode childNode : childNodes) {

            matcherChildren.add(childNode.matcherTree());
        }

        return new MatcherTreeNode(this, matcherChildren);
    }
}