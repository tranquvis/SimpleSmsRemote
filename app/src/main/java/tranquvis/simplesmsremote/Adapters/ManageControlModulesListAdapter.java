package tranquvis.simplesmsremote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import tranquvis.simplesmsremote.ControlModule;
import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andi on 28.08.2016.
 */
public class ManageControlModulesListAdapter extends ArrayAdapter<ControlModule> {
    public static final int LAYOUT_RES = R.layout.listview_item_manage_control_modules;

    public ManageControlModulesListAdapter(Context context, ControlModule[] data) {
        super(context, LAYOUT_RES, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(LAYOUT_RES, parent, false);

        }

        ControlModule controlModule = getItem(position);
        ControlModuleUserData userData = controlModule.getUserData();

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView_title);
        ImageView stateImageView = (ImageView) convertView.
                findViewById(R.id.imageView_state);

        titleTextView.setText(controlModule.getTitleRes());
        //changeStateButton.setImageDrawable();
        if(!controlModule.isCompatible()) {
            stateImageView.setImageResource(R.drawable.ic_unavailable_circle);
        }
        else if(userData == null) {
            stateImageView.setImageResource(R.drawable.ic_add_circle);
        }
        else {
            stateImageView.setImageResource(R.drawable.ic_check_circle);
        }

        return convertView;
    }
}
