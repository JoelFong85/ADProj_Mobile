package Adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.averg.logicuniversityapp.R;

import java.util.List;

import Models.ReallocateItem;
import Utilities.InputFilterMinMax;

public class ReallocateListAdapter extends ArrayAdapter<ReallocateItem> {

    private List<ReallocateItem> items;
    int resource;

    public ReallocateListAdapter(Context context, int resource, List<ReallocateItem> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource, null);

        final ReallocateItem ri = items.get(position);

        TextView dptID = (TextView) v.findViewById(R.id.textView_ReallocateList_dptID1);
        dptID.setText(ri.get("DepartmentID"));

        TextView orderQty = (TextView) v.findViewById(R.id.textView_ReallocateList_OrderQty1);
        orderQty.setText(ri.get("QuantityOrdered"));

        EditText distriQty = (EditText) v.findViewById(R.id.editText_ReallocateList_DistriQty);
        distriQty.setText(ri.get("CollectedQty"));

        int qty = Integer.parseInt(ri.get("QuantityOrdered"));
        final EditText et = (EditText) v.findViewById(R.id.editText_ReallocateList_DistriQty);
        et.setFilters(new InputFilter[]{new InputFilterMinMax(0, qty)});
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateCollectQty(ri.get("DepartmentID"), et.getText().toString());
                //Log.i("onItemClick", et.getText().toString());
            }
        });

        return v;
    }

    public void updateCollectQty(String dptID, String newQty) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).get("DepartmentID").trim() == dptID.trim()) {
                items.get(i).put("NewCollectedQty", newQty);
            }
        }
    }
}
