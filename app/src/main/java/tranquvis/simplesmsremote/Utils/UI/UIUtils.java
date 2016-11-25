package tranquvis.simplesmsremote.Utils.UI;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class UIUtils {

    /**
     * Set ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @see <a href="http://stackoverflow.com/questions/1778485/android-listview-display-all-available-items-without-scroll-with-static-header">stackoverflow answer</a>
     */
    public static void SetListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            throw new RuntimeException("an adapter must be set before list view can be resized");

        int numberOfItems = listAdapter.getCount();

        // Get total height of all items.
        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            View item = listAdapter.getView(itemPos, null, listView);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
        }

        // Get total height of all item dividers.
        int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

        //get vertical padding
        int paddingVertical = listView.getPaddingTop() + listView.getPaddingBottom();

        // Set list height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight + paddingVertical;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}