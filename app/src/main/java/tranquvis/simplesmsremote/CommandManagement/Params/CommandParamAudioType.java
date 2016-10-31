package tranquvis.simplesmsremote.CommandManagement.Params;

import tranquvis.simplesmsremote.Utils.Device.AudioUtils;

public class CommandParamAudioType extends CommandParam<AudioUtils.AudioType>
{
    private static PatternAssignment[] assignments = new PatternAssignment[]{
            new PatternAssignment("ring|ringtone", AudioUtils.AudioType.RING),
            new PatternAssignment("music", AudioUtils.AudioType.MUSIC),
            new PatternAssignment("alarm(s)?", AudioUtils.AudioType.ALARM),
            new PatternAssignment("notification(s)?|notify", AudioUtils.AudioType.NOTIFICATION),
            new PatternAssignment("system", AudioUtils.AudioType.SYSTEM),
            new PatternAssignment("phonecall(s)?|voicecall(s)?|call(s)?",
                    AudioUtils.AudioType.VOICECALL),
            new PatternAssignment("dtmf", AudioUtils.AudioType.DTMF)
    };

    public CommandParamAudioType(String id) {
        super(id);
    }

    @Override
    public AudioUtils.AudioType getValueFromInput(String input) throws Exception {
        for (PatternAssignment assignment : assignments) {
            if(assignment.pattern.matches(input))
                return assignment.audioType;
        }
        throw new IllegalArgumentException("Invalid audio type given.");
    }

    public static String GetEntirePattern()
    {
        String pattern = assignments[0].pattern;
        for (int i = 1; i < assignments.length; i++) {
            pattern += "|" + assignments[i].pattern;
        }
        return pattern;
    }

    private static class PatternAssignment
    {
        private String pattern;
        private AudioUtils.AudioType audioType;

        private PatternAssignment(String pattern, AudioUtils.AudioType audioType) {
            this.pattern = pattern;
            this.audioType = audioType;
        }
    }
}