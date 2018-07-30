package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.averg.logicuniversityapp.R;

import java.util.ArrayList;

import Models.Inventory;

public class InventoryAdapter extends ArrayAdapter<Inventory> {
    public InventoryAdapter(Context context, ArrayList<Inventory> inv) {
        super(context, 0, inv);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Inventory inv = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_three_column, parent, false);
        }
        // Lookup view for data population
        TextView column1 = convertView.findViewById(R.id.column1);
        TextView column2 = convertView.findViewById(R.id.column2);
        TextView column3 = convertView.findViewById(R.id.column3);
        // Populate the data into the template view using the data object
        column1.setText(inv.getCategory());
        column2.setText(inv.getDescription());
        column3.setText(String.valueOf(inv.getPriorityUnitPrice()));

        // Return the completed view to render on screen
        return convertView;
    }
}
