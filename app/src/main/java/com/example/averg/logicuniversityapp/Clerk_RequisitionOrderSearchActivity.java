package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import Adapters.InventoryAdapter;
import Adapters.ROLinkedListAdapter;
import Models.Inventory;
import Models.RequisitionOrder;
import Utilities.Constants;
import Utilities.JSONParser;

public class Clerk_RequisitionOrderSearchActivity extends Activity {

    public ArrayList<RequisitionOrder> roList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk__requisition_order_search);

        // Attach a listener to the search view
        SearchView searchView = findViewById(R.id.requisitionOrderSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                new RequisitionOrderSearchTask(Clerk_RequisitionOrderSearchActivity.this).execute(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Do nothing.
                return false;
            }
        });

    }

    // Async task
    private class RequisitionOrderSearchTask extends AsyncTask<String, Void, JSONArray> {

        private final WeakReference<Activity> weakActivity;

        RequisitionOrderSearchTask(Activity myActivity) {
            this.weakActivity = new WeakReference<>(myActivity);
        }


        protected JSONArray doInBackground(String... params) {
            String requisitionId = params[0];
            return JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST + "/SpecialRequest/?roid=" + requisitionId);
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            try {
                // TextView requisitionTitle = findViewById(R.id.requisitionSearchTitle);
                // requisitionTitle.setText(result.toString());

                // Convert JSONArray into list items.
                for (int i = 0; i < result.length(); i++) {
                    JSONObject row = result.getJSONObject(i);
                    roList.add(new RequisitionOrder(
                            row.getString("RequisitionId"),
                            row.getString("ItemNumber"),
                            row.getString("QuantityOrdered"),
                            row.getString("CollectedQty"),
                            row.getString("PendingQty"),
                            "0",
                            row.getString("Description"))
                    );
                } ;

                ROLinkedListAdapter adapter = new ROLinkedListAdapter(weakActivity.get(), roList);
                ListView listView = findViewById(R.id.requisitionOrderListView);
                listView.setAdapter(adapter);


            } catch (Exception ex) {
                Toast t = Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT);
                t.show();
                ex.printStackTrace();
            }

        }
    }
}
