package tranquvis.simplesmsremote.Utils.Regex;

public enum MatchType
{
    DO_NOT_MATCH,

    /**
     * If no child is found on the position, a new child will be added.
     */
    BY_INDEX,
    /**
     * If no child matches the given string, a new child will be added.
     */
    BY_CHILD_PATTERN,

    /**
     * Match fails if no child is found on the position.
     */
    BY_INDEX_STRICT,

    /**
     * Match fails if no child is found, which matches the given string.
     */
    BY_CHILD_PATTERN_STRICT
}