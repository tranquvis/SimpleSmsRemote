package tranquvis.simplesmsremote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.Helper.ContactsHelper;
import tranquvis.simplesmsremote.Helper.ContactsHelper.Contact;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andi on 04.09.2016.
 */
public class ContactsListAdapter extends BaseAdapter implements Filterable
{
    private static int LAYOUT_RES = R.layout.listview_item_contacts;

    private Context context;
    private List<Contact> allContacts;
    private List<Contact> filteredContacts = new ArrayList<>();
    ContactFilter contactFilter = new ContactFilter();

    public ContactsListAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        allContacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(LAYOUT_RES, parent, false);
        }

        Contact contact = getItem(position);

        ((TextView)convertView.findViewById(R.id.textView_name)).setText(contact.getName());
        ((TextView)convertView.findViewById(R.id.textView_phonenumber))
                .setText(contact.getPhoneNumber());

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Contact getItem(int position) {
        return filteredContacts.get(position);
    }

    @Override
    public int getCount() {
        return filteredContacts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    private class ContactFilter extends Filter
    {
        int MAX_RESULTS = 5;
        String previousConstraint = "";

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Contact)resultValue).getPhoneNumber();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null)
            {
                List<Contact> tempContacts = new ArrayList<>();
                String constraintStr = constraint.toString();
                if(!previousConstraint.equals(constraintStr)) {
                    previousConstraint = constraintStr;

                    for (Contact contact : allContacts) {
                        if ((contact.getName() + " " + contact.getPhoneNumber()).toLowerCase()
                                .contains(constraintStr.toLowerCase())) {
                            tempContacts.add(contact);
                            if(tempContacts.size() >= MAX_RESULTS)
                                break;
                        }
                    }
                }
                else
                {
                    tempContacts.addAll(filteredContacts);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = tempContacts;
                filterResults.count = tempContacts.size();
                return filterResults;
            }
            else
            {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0)
            {
                filteredContacts = (List<Contact>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
