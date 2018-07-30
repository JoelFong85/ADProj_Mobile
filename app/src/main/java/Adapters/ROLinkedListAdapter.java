package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.averg.logicuniversityapp.Clerk_RequisitionOrder_ItemDetailsActivity;
import com.example.averg.logicuniversityapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Models.Inventory;
import Models.RequisitionOrder;

public class ROLinkedListAdapter extends ArrayAdapter<RequisitionOrder> {

    public ROLinkedListAdapter(Context context, ArrayList<RequisitionOrder> roList) {
        super(context, 0, roList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final RequisitionOrder ro = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_three_column, parent, false);
        }


        // Lookup view for data population
        TextView col1Text = convertView.findViewById(R.id.column1);
        TextView col2Text = convertView.findViewById(R.id.column2);
        TextView col3Text = convertView.findViewById(R.id.column3);
        // Populate the data into the template view using the data object
        col1Text.setText(ro.getItemNumber());
        col2Text.setText(ro.getDescription());
        col3Text.setText(ro.getItemRequisitionQuantity());
        // Toast t = Toast.makeText(getContext(), ro.getItemRequisitionQuantity(), Toast.LENGTH_SHORT);
        //t.show();
        col1Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start Special Orders Collection List Activity

                Intent i = new Intent(getContext().getApplicationContext(), Clerk_RequisitionOrder_ItemDetailsActivity.class);
                i.putExtra("requisitionId", ro.getRequisitionId());
                i.putExtra("itemNumber", ro.getItemNumber());
                i.putExtra("quantityOrdered", ro.getItemRequisitionQuantity());

                getContext().startActivity(i);
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }





}
