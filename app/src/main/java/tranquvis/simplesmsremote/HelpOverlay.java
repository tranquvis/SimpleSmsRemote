package tranquvis.simplesmsremote;

public class HelpOverlay
{
    public static HelpOverlay GetMainActivityOverlay()
    {
        return new HelpOverlay(new View[]{
                new View(R.string.help_start_title, R.string.help_start_content, -1),
                new View(R.string.help_receiver_title, R.string.help_receiver_content,
                        R.id.layout_help_receiver),
                new View(R.string.help_module_title, R.string.help_module_content,
                        R.id.layout_help_module),
                new View(R.string.help_other_title, R.string.help_other_content, -1)
        });
    }

    private View[] helpViews;

    private HelpOverlay(View[] helpViews)
    {
        this.helpViews = helpViews;
    }

    public View getView(int position)
    {
        if (position >= helpViews.length)
            return null;
        return helpViews[position];
    }

    public int getHelpViewCount()
    {
        return helpViews.length;
    }

    public static class View
    {
        private int titleRes;
        private int descRes;
        private int hintContainerResId;

        public View(int titleRes, int descRes, int hintContainerResId)
        {
            this.titleRes = titleRes;
            this.descRes = descRes;
            this.hintContainerResId = hintContainerResId;
        }

        public int getTitleRes()
        {
            return titleRes;
        }

        public int getDescRes()
        {
            return descRes;
        }

        public int getHintContainerResId()
        {
            return hintContainerResId;
        }
    }
}