package tranquvis.simplesmsremote.Utils.UnitTools;

import org.intellij.lang.annotations.Language;

public enum Unit
{
    MILLISECONDS("ms", 1000f, UnitType.TIME), SECONDS("s", 1f, UnitType.TIME),
    MINUTES("min", 1f/60f, UnitType.TIME), HOURS("h", 1f/3600f, UnitType.TIME);

    @Language("RegExp")
    private String pattern;
    private Float factor;
    private UnitType unitType;

    Unit(@Language("RegExp") String pattern, Float factor, UnitType unitType) {
        this.pattern = pattern;
        this.factor = factor;
        this.unitType = unitType;
    }

    public String getPattern() {
        return "(?i)" + pattern;
    }

    /**
     * @return factor based on basic unit (s)
     */
    public Float getFactor() {
        return factor;
    }

    /**
     * Get pattern, which matches all units of the given unit type.
     * @param unitType unit type
     * @return the full pattern
     */
    @Language("RegExp")
    public static String GetFullPattern(UnitType unitType)
    {
        @Language("RegExp")
        String pattern = "(?i)";

        boolean first = true;
        for (Unit unit : values()) {
            if(unit.unitType == unitType)
            {
                if(!first) pattern += "|";
                else first = false;

                pattern += unit.getPattern();
            }
        }
        return pattern;
    }
}