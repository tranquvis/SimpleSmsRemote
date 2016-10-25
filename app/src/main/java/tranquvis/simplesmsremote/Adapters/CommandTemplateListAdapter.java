package tranquvis.simplesmsremote.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import tranquvis.simplesmsremote.CommandManagement.ControlCommand;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 21.10.2016.
 */

public class CommandTemplateListAdapter extends ArrayAdapter<ControlCommand>
{
    private static final int LAYOUT_RES = R.layout.listview_item_commands;
    public CommandTemplateListAdapter(Context context, ControlCommand[] commands)
    {
        super(context, LAYOUT_RES, commands);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater inflater =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(LAYOUT_RES, parent, false);
        }

        ControlCommand command = getItem(position);
        if(command == null)
            return convertView;

        ((TextView)convertView.findViewById(R.id.textView_command_template))
                .setText(command.getTitle());

        return convertView;
    }
}
