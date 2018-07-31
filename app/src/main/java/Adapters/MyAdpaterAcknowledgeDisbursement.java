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

import Models.Disbursement_Detail;

public class MyAdpaterAcknowledgeDisbursement extends ArrayAdapter<Disbursement_Detail> {

    private List<Disbursement_Detail> items;
    int resource;

    public MyAdpaterAcknowledgeDisbursement(Context context, int resource, List<Disbursement_Detail> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource, null);
        String des = items.get(position).get("description");
        String oq = items.get(position).get("order_quantity");
        String rq = items.get(position).get("altered_quantity");
        if (des != null) {
            TextView textView_des  = (TextView) v.findViewById(R.id.textViewdisburseitem);
            textView_des.setText(des);
            TextView textView_oq = (TextView) v.findViewById(R.id.textVieworder);
            textView_oq.setText(oq);
            TextView textView_rq = (TextView) v.findViewById(R.id.textViewreceived);
            textView_rq.setText(rq);
        }
        return v;
    }
}
