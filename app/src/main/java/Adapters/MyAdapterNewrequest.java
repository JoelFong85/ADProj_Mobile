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

import Models.Inventory;

public class MyAdapterNewrequest extends ArrayAdapter<Inventory> {

    //Tharrani Udhayasekar
    private List<Inventory> items;
    int resource;

    public MyAdapterNewrequest(Context context, int resource, List<Inventory> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource, null);
        String desc = items.get(position).get("description");
        String cat = items.get(position).get("category");
        if (desc != null) {
            TextView textView_desc  = (TextView) v.findViewById(R.id.textView_description);
            textView_desc.setText(desc);
            TextView textView_cat = (TextView) v.findViewById(R.id.textView_category);
            textView_cat.setText(cat);
        }
        return v;
    }
}
