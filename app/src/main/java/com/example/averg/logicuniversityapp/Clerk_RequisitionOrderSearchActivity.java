package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Adapters.ROLinkedListAdapter;
import Models.RequisitionOrder;
import Utilities.Constants;
import Utilities.JSONParser;

public class Clerk_RequisitionOrderSearchActivity extends Activity{

    public ArrayList<RequisitionOrder> roList;

    public static final int PREPARE_COMPLETE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk__requisition_order_search);

        loadDatePicker();
        // Prevent keyboard from automatically popping up on datepicker
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        // Attach a listener to the "Ready for collection button"
        Button readyForCollectionButton = findViewById(R.id.readyForCollectionButton);
        readyForCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReadyForCollectionTask(Clerk_RequisitionOrderSearchActivity.this).execute();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Grab data from the completed intent from itemdetails activity
        Intent i = intent;

        // If the item details activity is completed without using the back button..
        if(i != null) {
            String itemNumber = intent.getStringExtra("itemNumber");

            // Remove item from the RO list
            RequisitionOrder toDelete = null;

            for (RequisitionOrder item : roList) {
                if (item.getItemNumber().equals(itemNumber)) {
                    toDelete = item;
                    break;
                }
            }
            if (toDelete != null) {
                Log.i("yes: ","Removing item: " + toDelete.getItemNumber() + "!");
                roList.remove(toDelete);
            }

            // Reload adapter.
            reloadAdapter();

            // Check if listview is empty, if it is, enable to ready for collection button
            ListView lv = findViewById(R.id.requisitionOrderListView);
            ListAdapter adapter = lv.getAdapter();
            if(adapter.getCount() == 0){
                Button b = findViewById(R.id.readyForCollectionButton);
                b.setEnabled(true);
            }


        }

        return;
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
                Toast t = Toast.makeText(getApplicationContext(), "Search complete!", Toast.LENGTH_LONG);
                t.show();

                roList = new ArrayList<>();

                // Convert JSONArray into list items.
                for (int i = 0; i < result.length(); i++) {
                    JSONObject row = result.getJSONObject(i);

                    // Filter out requisition order items that are prepared already
                    if (!(Integer.parseInt(row.getString("PendingQty")) == 0
                            || Integer.parseInt(row.getString("CurrentInventoryQty")) == 0)) {
                        roList.add(new RequisitionOrder(
                                row.getString("RequisitionId"),
                                row.getString("ItemNumber"),
                                row.getString("QuantityOrdered"),
                                row.getString("CollectedQty"),
                                row.getString("PendingQty"),
                                "0",
                                row.getString("Description"))
                        );
                    }
                }

                // If there are still items to be prepared, attach adapter to the list
                reloadAdapter();

                // Otherwise, enable the "Ready For Collection" button, labelling the requisition order as "complete"
                if(roList.isEmpty()){
                    Button readyForCollectionButton = findViewById(R.id.readyForCollectionButton);
                    readyForCollectionButton.setEnabled(true);
                }


            } catch (Exception ex) {
                Toast t = Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT);
                t.show();
                ex.printStackTrace();
            }

        }
    }


    private class ReadyForCollectionTask extends AsyncTask<String, Void, String> {

        private final WeakReference<Activity> weakActivity;

        ReadyForCollectionTask(Activity myActivity) {
            this.weakActivity = new WeakReference<>(myActivity);
        }


        protected String doInBackground(String... params) {
            // Create a collection details JSON Object
            JSONObject j = new JSONObject();

            // Fetch the department Id
            SearchView sv = findViewById(R.id.requisitionOrderSearchView);
            String departmentId = sv.getQuery().toString().substring(0,4);

            // Fetch the place id
            String placeId = JSONParser.getStream(Constants.SERVICE_HOST + "/Department/Sorting/PlaceId/" + departmentId + "/" + Constants.TOKEN);
            placeId = placeId.trim();


            // Fetch the Date
            EditText dateEditText = findViewById(R.id.selectedDateEditText);
            String collectionDate = dateEditText.getText().toString();

            // Fetch the RO Id
            String roid = sv.getQuery().toString();

            try {
                j.put("PlaceId", Integer.parseInt(placeId));
                j.put("CollectionDate", collectionDate);
                j.put("DepartmentId", departmentId);
                j.put("RoId", roid);
                j.put("Token", Constants.TOKEN);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            Log.i("UpdateCDRDD: ", j.toString());
            Log.i("Ready Collection URL: ", Constants.SERVICE_HOST + "/SpecialRequest/Sorting/UpdateCDRDD");

            return JSONParser.postStream(Constants.SERVICE_HOST + "/SpecialRequest/Sorting/UpdateCDRDD", j.toString());
        }

        @Override
        protected void onPostExecute(String result) {
            Toast t = Toast.makeText(getApplicationContext(), "Preparation Complete!", Toast.LENGTH_LONG);
            t.show();
        }
    }


    protected void loadDatePicker() {
        final EditText selectedDate = findViewById(R.id.selectedDateEditText);
        Button btnSelectDate = findViewById(R.id.selectDateButton);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(Clerk_RequisitionOrderSearchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {

                        int month = mMonth + 1;
                        String formattedMonth = "" + month;
                        String formattedDayOfMonth = "" + mDay;

                        if (month < 10) {

                            formattedMonth = "0" + month;
                        }
                        if (mDay < 10) {

                            formattedDayOfMonth = "0" + mDay;
                        }
                        selectedDate.setText(formattedDayOfMonth + "-" + formattedMonth + "-" + mYear);
                    }
                }, year, month, day);

                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });
    }

    // Loads the adapter for the list view
    private void reloadAdapter(){
        ListView listView = findViewById(R.id.requisitionOrderListView);
        ROLinkedListAdapter adapter;
        // Clear the list first
        adapter = new ROLinkedListAdapter(getApplicationContext(), new ArrayList<RequisitionOrder>());
        listView.setAdapter(adapter);

        // Populate the list
        adapter = new ROLinkedListAdapter(getApplicationContext(), roList);
        listView.setAdapter(adapter);

        // Add listeners to each row on the listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {

                // Start a new activity when a row is clicked on
                RequisitionOrder ro = (RequisitionOrder) adapter.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), Clerk_RequisitionOrder_ItemDetailsActivity.class);
                i.putExtra("requisitionId", ro.getRequisitionId());
                i.putExtra("itemNumber", ro.getItemNumber());
                i.putExtra("quantityOrdered", ro.getItemPendingQuantity());

                startActivityForResult(i, PREPARE_COMPLETE);
            }
        });
    }
}
