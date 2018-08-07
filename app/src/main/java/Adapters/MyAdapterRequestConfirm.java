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

import Models.EmployeeRequisitionOrderDetail;

public class MyAdapterRequestConfirm extends ArrayAdapter<EmployeeRequisitionOrderDetail> {

    //Tharrani Udhayasekar
    private List<EmployeeRequisitionOrderDetail> items;
    int resource;

    public MyAdapterRequestConfirm(Context context, int resource, List<EmployeeRequisitionOrderDetail> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource, null);
        String uom = items.get(position).get("unit_of_measurement").toString();
        String desc = items.get(position).get("description").toString();
        String quantity = items.get(position).get("quantity").toString();
        //Log.i("adapter desc", test);
        String q = items.get(position).get("quantity").toString();
        if (desc != null) {
            TextView textView_desc  = (TextView) v.findViewById(R.id.textViewdesc);
            textView_desc.setText(desc);
            TextView textView_quat = (TextView) v.findViewById(R.id.textViewquan);
            textView_quat.setText(quantity);
            TextView textView_uom = (TextView) v.findViewById(R.id.textViewUOM);
            textView_uom.setText(uom);
        }
        return v;
    }
}
