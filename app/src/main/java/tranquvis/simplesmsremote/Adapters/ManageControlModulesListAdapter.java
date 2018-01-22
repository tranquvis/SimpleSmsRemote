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

import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andi on 28.08.2016.
 */
public class ManageControlModulesListAdapter extends ArrayAdapter<Module> {
    private static final int LAYOUT_RES = R.layout.listview_item_manage_control_modules;

    public ManageControlModulesListAdapter(Context context, List<Module> data) {
        super(context, LAYOUT_RES, data);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(LAYOUT_RES, parent, false);
        }

        Module module = getItem(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView_title);
        ImageView stateImageView = (ImageView) convertView.
                findViewById(R.id.imageView_state);
        ImageView moduleIconImageView = (ImageView) convertView.findViewById(R.id.imageView_type);

        if (module.getIconRes() != -1) {
            moduleIconImageView.setImageResource(module.getIconRes());
        }

        if (module.getTitleRes() != -1)
            titleTextView.setText(module.getTitleRes());
        else
            titleTextView.setText(module.getId());

        //changeStateButton.setImageDrawable();
        if (!module.isCompatible(getContext())) {
            stateImageView.setImageResource(R.drawable.ic_remove_circle_red_400_24dp);
        } else if (!module.isEnabled()) {
            stateImageView.setImageResource(R.drawable.ic_add_circle_indigo_400_24dp);
        } else {
            stateImageView.setImageResource(R.drawable.ic_check_circle_green_400_24dp);
        }

        return convertView;
    }
}
