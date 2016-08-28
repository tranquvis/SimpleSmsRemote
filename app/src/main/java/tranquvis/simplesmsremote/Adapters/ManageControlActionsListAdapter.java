package tranquvis.simplesmsremote.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import tranquvis.simplesmsremote.ControlAction;

/**
 * Created by Andi on 28.08.2016.
 */
public class ManageControlActionsListAdapter extends ArrayAdapter<ControlAction> {
    public ManageControlActionsListAdapter(Context context, int resource, List<ControlAction> objects) {
        super(context, resource, objects);
    }
}
