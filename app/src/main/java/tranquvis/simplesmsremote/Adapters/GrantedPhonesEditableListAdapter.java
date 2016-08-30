package tranquvis.simplesmsremote.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 30.08.2016.
 */
public class GrantedPhonesEditableListAdapter extends ArrayAdapter<String>
{
    public static final int LAYOUT_RES = R.layout.listview_item_granted_phones_editable;

    private List<String> phones;
    private ListView listView;
    private int itemHeight = 0;

    public GrantedPhonesEditableListAdapter(Context context, List<String> phones, ListView listView)
    {
        super(context, LAYOUT_RES, phones);
        this.phones = phones;
        this.listView = listView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(LAYOUT_RES, parent, false);

            if(itemHeight == 0)
            {
                convertView.measure(0, 0);
                itemHeight = convertView.getMeasuredHeight();
            }
        }

        String phone = phones.get(position);

        final EditText phoneEditText = (EditText) convertView.findViewById(R.id.editText);
        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.imageButton_delete);

        phoneEditText.setText(phone);
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable)
            {
                phones.set(position, editable.toString());
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                phones.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void addPhone(String phone)
    {
        phones.add(phone);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = (itemHeight + listView.getDividerHeight()) * phones.size();
    }
}
