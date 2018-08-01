package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.averg.logicuniversityapp.R;

import java.util.List;

import Models.CollectionItem;

public class SortingListAdapter extends ArrayAdapter<CollectionItem> {
    private List<CollectionItem> items;
    int resource;

    public SortingListAdapter(Context context, int resource, List<CollectionItem> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource, null);

        CollectionItem ci = items.get(position);

        TextView itemNum = (TextView) v.findViewById(R.id.textView_SortingListItemNum);
        itemNum.setText(ci.get("ItemNumber"));

        TextView descrip = (TextView) v.findViewById(R.id.textView_SortingListDescription);
        descrip.setText(ci.get("Description"));

        TextView orderQty = (TextView) v.findViewById(R.id.textView_SortingListOrderQty);
        orderQty.setText(ci.get("QuantityOrdered"));

        TextView collectQty = (TextView) v.findViewById(R.id.textView_SortingListCollectQty);
        collectQty.setText(ci.get("CollectedQty"));

        return v;
    }
}
