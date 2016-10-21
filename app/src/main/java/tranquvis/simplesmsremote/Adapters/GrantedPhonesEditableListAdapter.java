package tranquvis.simplesmsremote.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.UIUtils;

/**
 * Created by Andreas Kaltenleitner on 30.08.2016.
 */
public class GrantedPhonesEditableListAdapter extends ArrayAdapter<String>
{
    private static final int LAYOUT_RES = R.layout.listview_item_granted_phones_editable;

    private List<String> phones;
    private ListView listView;

    private List<String> usedNumbers;
    private ArrayAdapter phoneListAdapter;

    public GrantedPhonesEditableListAdapter(Context context, List<String> phones, ListView listView)
    {
        super(context, LAYOUT_RES, phones);
        this.phones = phones;
        this.listView = listView;

        this.usedNumbers = DataManager.getUserData().getAllUsedPhones();
        phoneListAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item_phone,
                R.id.textView_phone, usedNumbers);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(LAYOUT_RES, parent, false);
        }

        String phone = phones.get(position);

        final AutoCompleteTextView phoneEditText = (AutoCompleteTextView)
                convertView.findViewById(R.id.edittext_phonenumber);
        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.imageButton_delete);

        phoneEditText.setText(phone);
        if(phoneListAdapter != null)
            phoneEditText.setAdapter(phoneListAdapter);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                removePhone(position);
            }
        });

        return convertView;
    }

    private void removePhone(int position)
    {
        updateData();
        phones.remove(position);
        notifyDataSetChanged();
    }

    public void addPhone(String phone)
    {
        updateData();
        phones.add(phone);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
        UIUtils.SetListViewHeightBasedOnItems(listView);
    }

    public void updateData()
    {
        for(int i = 0; i < listView.getChildCount(); i++) {
            View view = listView.getChildAt(i);
            EditText editText = (EditText) view.findViewById(R.id.edittext_phonenumber);
            phones.set(i, editText.getText().toString());
        }
    }
}
