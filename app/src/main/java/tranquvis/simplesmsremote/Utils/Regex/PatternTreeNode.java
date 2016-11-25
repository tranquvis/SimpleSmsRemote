package tranquvis.simplesmsremote.Utils.Regex;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTreeNode {
    String id;

    String regex;
    List<PatternTreeNode> childNodes = new ArrayList<>();
    MatchType childMatchType;

    private Pattern pattern;

    /**
     * Create regex tree leaf.
     *
     * @param id leaf identifier
     */
    public PatternTreeNode(@Nullable String id) {
        this.id = id;
    }

    /**
     * <p>Create regex tree node. </p>
     * <p>Note that the tree leafs must not be defined. If a children is not defined
     * during matching, it is added automatically, however without id.</p>
     *
     * @param id             node identifier
     * @param regex          pattern
     * @param childMatchType defines how child values should be matched in the given childNodes
     * @param childNodes     predefined children of node
     */
    public PatternTreeNode(String id, String regex,
                           @NonNull MatchType childMatchType,
                           @Nullable PatternTreeNode... childNodes) {
        this.id = id;
        this.regex = regex;
        this.childMatchType = childMatchType;
        if (childNodes != null) {
            Collections.addAll(this.childNodes, childNodes);
        }
    }

    public String getRegex() {
        return regex;
    }

    Matcher getMatcher(String input) {
        if (pattern == null)
            compile();
        return pattern.matcher(input);
    }

    public void compile() {
        if (regex != null)
            pattern = Pattern.compile(regex);
    }

    /**
     * Build matcher tree recursively.
     *
     * @return root of matcher tree
     */
    public MatcherTreeNode buildMatcherTree() {
        List<MatcherTreeNode> matcherChildren = new ArrayList<>();
        for (PatternTreeNode childNode : childNodes) {

            matcherChildren.add(childNode.buildMatcherTree());
        }

        return new MatcherTreeNode(this, matcherChildren);
    }
}