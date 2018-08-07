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

import Models.Disbursement_List;

public class MyAdapterDisbursementList extends ArrayAdapter<Disbursement_List> {

    //Tharrani Udhayasekar
    private List<Disbursement_List> items;
    int resource;

    public MyAdapterDisbursementList(Context context, int resource, List<Disbursement_List> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource, null);
        String location = items.get(position).get("collection_location");
        String dep = items.get(position).get("department_name");
        String date = items.get(position).get("collection_date");
        String time = items.get(position).get("collection_time").toString();
        if (dep != null) {
            TextView textView_loc  = (TextView) v.findViewById(R.id.textViewlocation);
            textView_loc.setText(location);
            TextView textView_dep = (TextView) v.findViewById(R.id.textViewdepart);
            textView_dep.setText(dep);
            TextView textView_date = (TextView) v.findViewById(R.id.textViewcollectiondate);
            textView_date.setText(date);
            TextView textView_time = (TextView) v.findViewById(R.id.textViewtime);
            textView_time.setText(time);
        }
        return v;
    }
}


