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

import Models.reqcart;

public class MyAdapterRequestCart extends ArrayAdapter<reqcart> {
    private List<reqcart> items;
    int resource;

    public MyAdapterRequestCart(Context context, int resource, List<reqcart> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource, null);
            String uom = items.get(position).getInv().get("unit_of_measurement");
            String desc = items.get(position).getInv().get("description").toString();
          //Log.i("adapter desc", test);
            String q = items.get(position).get("cart_quantity").toString();
            if (desc != null) {
            TextView textView_desc  = (TextView) v.findViewById(R.id.textViewdesc);
            textView_desc.setText(desc);
            TextView textView_quat = (TextView) v.findViewById(R.id.textViewquan);
            textView_quat.setText(q);
            TextView textView_uom = (TextView) v.findViewById(R.id.textViewUOM);
            textView_uom.setText(uom);
        }
        return v;
    }
}
