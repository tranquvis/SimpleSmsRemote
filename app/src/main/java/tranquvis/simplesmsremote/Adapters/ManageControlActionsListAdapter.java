package tranquvis.simplesmsremote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import tranquvis.simplesmsremote.Data.ControlAction;
import tranquvis.simplesmsremote.Data.ControlActionUserData;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andi on 28.08.2016.
 */
public class ManageControlActionsListAdapter extends ArrayAdapter<ControlAction> {
    public static final int LAYOUT_RES = R.layout.listview_item_manage_control_actions;

    public ManageControlActionsListAdapter(Context context, List<ControlAction> objects) {
        super(context, LAYOUT_RES, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(LAYOUT_RES, parent, false);
            /*
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item_manage_control_actions, parent);
                    */
        }

        ControlAction controlAction = getItem(position);
        ControlActionUserData userData = controlAction.getUserData();

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView_title);
        ImageButton changeStateButton = (ImageButton) convertView.
                findViewById(R.id.button_change_state);

        titleTextView.setText(controlAction.getCommand());
        //changeStateButton.setImageDrawable();
        if(userData == null)
        {

        }
        changeStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //TODO start configuration activity
            }
        });

        return convertView;
    }
}
