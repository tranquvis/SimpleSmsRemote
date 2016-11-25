package tranquvis.simplesmsremote.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.CameraUtils;

/**
 * Created by Andreas Kaltenleitner on 18.10.2016.
 */

public class CameraDeviceSpinnerAdapter extends ArrayAdapter<CameraUtils.MyCameraInfo> {
    private static final int LAYOUT_RES = R.layout.spinner_item_camera_device;

    public CameraDeviceSpinnerAdapter(Context context, List<CameraUtils.MyCameraInfo> objects) {
        super(context, LAYOUT_RES, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(LAYOUT_RES, parent, false);
        }

        CameraUtils.MyCameraInfo cameraInfo = getItem(position);
        if (cameraInfo == null)
            return convertView;

        int lensFacingDescRes = -1;
        if (cameraInfo.getLensFacing() == null)
            lensFacingDescRes = R.string.camera_facing_unknown_title;
        else if (cameraInfo.getLensFacing() == CameraUtils.LensFacing.BACK)
            lensFacingDescRes = R.string.camera_facing_back_title;
        else if (cameraInfo.getLensFacing() == CameraUtils.LensFacing.FRONT)
            lensFacingDescRes = R.string.camera_facing_front_title;
        else if (cameraInfo.getLensFacing() == CameraUtils.LensFacing.EXTERNAL)
            lensFacingDescRes = R.string.camera_facing_external_title;

        String title = "Camera " + cameraInfo.getId() + " ("
                + getContext().getString(lensFacingDescRes) + ")";

        TextView textView = (TextView) convertView.findViewById(R.id.textView_title);
        textView.setText(title);
        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
