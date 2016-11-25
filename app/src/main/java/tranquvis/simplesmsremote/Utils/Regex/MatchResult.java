package tranquvis.simplesmsremote.Utils.Regex;

public class MatchResult {
    boolean success;
    String failDetail;

    public MatchResult(boolean success, String failDetail) {
        this.success = success;
        this.failDetail = failDetail;
    }

    public String getFailDetail() {
        return failDetail;
    }
}
