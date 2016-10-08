package tranquvis.simplesmsremote.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.ControlModule;
import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andi on 28.08.2016.
 */
public class ManageControlModulesListAdapter extends ArrayAdapter<ControlModule> {
    private static final int LAYOUT_RES = R.layout.listview_item_manage_control_modules;

    public ManageControlModulesListAdapter(Context context, List<ControlModule> data) {
        super(context, LAYOUT_RES, data);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
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
        ImageView moduleIconImageView = (ImageView) convertView.findViewById(R.id.imageView_type);

        titleTextView.setText(controlModule.getTitleRes());
        //changeStateButton.setImageDrawable();
        if(!controlModule.isCompatible()) {
            stateImageView.setImageResource(R.drawable.ic_remove_circle_red_400_24dp);
        }
        else if(userData == null) {
            stateImageView.setImageResource(R.drawable.ic_add_circle_indigo_400_24dp);
        }
        else {
            stateImageView.setImageResource(R.drawable.ic_check_circle_green_400_24dp);
        }

        moduleIconImageView.setImageResource(controlModule.getIconRes());

        return convertView;
    }
}
