package tranquvis.simplesmsremote.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 31.08.2016.
 */
public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.LogEntryViewHolder>
{
    private static final int LAYOUT_RES = R.layout.listview_item_log;

    private Context context;
    private List<LogEntry> logEntries;

    public LogListAdapter(Context context, List<LogEntry> logEntries)
    {
        this.context = context;
        this.logEntries = logEntries;
    }

    @Override
    public int getItemCount()
    {
        return logEntries.size();
    }

    @Override
    public void onBindViewHolder(final LogEntryViewHolder viewHolder, int position)
    {
        LogEntry logEntry = logEntries.get(position);

        viewHolder.vTitle.setText(logEntry.getTitle());
        viewHolder.vTitle.setTextColor(context.getResources().getColor(logEntry.getType().
                getColorRes()));
        viewHolder.vSummary.setText(logEntry.getSummary());
        viewHolder.vTime.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(logEntry.getTime()));
        viewHolder.vType.setImageResource(logEntry.getType().getIconRes());

        View.OnClickListener onToggleClick = new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(viewHolder.vSummary.getVisibility() == View.GONE)
                {
                    //expand
                    viewHolder.vSummary.setVisibility(View.VISIBLE);
                    viewHolder.vToggle.setImageResource(R.drawable.ic_arrow_drop_up_grey_700_18dp);
                }
                else
                {
                    //fold
                    viewHolder.vSummary.setVisibility(View.GONE);
                    viewHolder.vToggle.setImageResource(R.drawable.ic_arrow_drop_down_grey_700_18dp);
                }
            }
        };
        viewHolder.vToggle.setOnClickListener(onToggleClick);
    }

    @Override
    public LogEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(context).inflate(LAYOUT_RES, parent, false);
        return new LogEntryViewHolder(itemView);
    }

    public static class LogEntryViewHolder extends RecyclerView.ViewHolder
    {
        TextView vTitle;
        TextView vSummary;
        TextView vTime;
        ImageButton vToggle;
        ImageView vType;

        public LogEntryViewHolder(View itemView)
        {
            super(itemView);
            vTitle = (TextView) itemView.findViewById(R.id.textView_title);
            vSummary = (TextView) itemView.findViewById(R.id.textView_summary);
            vTime = (TextView) itemView.findViewById(R.id.textView_time);
            vToggle = (ImageButton) itemView.findViewById(R.id.imageButton_toggle);
            vType = (ImageView) itemView.findViewById(R.id.imageView_type);
        }
    }
}
