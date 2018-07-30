package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import Adapters.InventoryAdapter;
import Models.Inventory;
import Utilities.Constants;
import Utilities.JSONParser;

public class Clerk_RequisitionOrder_ItemDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk__requisition_order__item_details);

        // Fetch the strings from our intent.
        Intent intent = getIntent();
        String requisitionId = intent.getStringExtra("requisitionId");
        String requisitionQuantity = intent.getStringExtra("quantityOrdered");
        String itemNumber = intent.getStringExtra("itemNumber");

        // Start an asynctask to populate our view.
        InventoryTask iTask = new InventoryTask(this);
        iTask.execute(itemNumber, requisitionQuantity);

        // Add a listener to the prepare button
        Button prepareButton = findViewById(R.id.details_prepareButton);
        prepareButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        // Validate the user's input.
        EditText collectQuantityEditText = findViewById(R.id.collectQuantityEditText);
        int collectQuantity = Integer.parseInt(collectQuantityEditText.getText().toString());

        TextView quantityAvailableTextView = findViewById(R.id.details_QtyAvailable);
        int quantityAvailable = Integer.parseInt(quantityAvailableTextView.getText().toString());

        TextView quantityOrderedTextView = findViewById(R.id.details_QtyOrdered);
        int quantityOrdered = Integer.parseInt(quantityOrderedTextView.getText().toString());

        // Quantity that can be collected by the dept rep must be lower than quantity available and quantity ordered.
        if (collectQuantity > quantityAvailable) {
            Toast t = Toast.makeText(this, "Quantity to collect must be less than quantity available!", Toast.LENGTH_LONG);
            t.show();
        } else if (collectQuantity > quantityOrdered) {
            Toast t = Toast.makeText(this, "Quantity to collect must be less than quantity you ordered!", Toast.LENGTH_LONG);
            t.show();
        }

        else{
            // Run the Async task
            new PrepareTask(this).execute();
        }


    }

    // Loads the information on the item details page.
    private class InventoryTask extends AsyncTask<String, Void, JSONObject> {

        private final WeakReference<Activity> weakActivity;
        private String requisitionQuantity;


        InventoryTask(Activity myActivity) {
            this.weakActivity = new WeakReference<>(myActivity);
        }

        protected JSONObject doInBackground(String... params) {
            requisitionQuantity = params[1];
            return JSONParser.getJSONFromUrl(Constants.SERVICE_HOST + "/Inventory/" + params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                // Populate our views
                TextView descriptionTextView = findViewById(R.id.details_Description);
                TextView itemCodeTextView = findViewById(R.id.details_ItemCode);
                TextView qtyAvailableTextView = findViewById(R.id.details_QtyAvailable);
                TextView qtyOrderedTextView = findViewById(R.id.details_QtyOrdered);
                TextView uomTextView = findViewById(R.id.details_UOM);

                descriptionTextView.setText(result.getString("description"));
                itemCodeTextView.setText(result.getString("item_number"));
                qtyAvailableTextView.setText(result.getString("current_quantity"));
                qtyOrderedTextView.setText(requisitionQuantity);
                uomTextView.setText(result.getString("unit_of_measurement"));

                // Set Quantity to collect to be the smaller of quantity available and quantity ordered.
                TextView qtyAvailable = findViewById(R.id.details_QtyAvailable);
                int available = Integer.parseInt(qtyAvailable.getText().toString());

                TextView qtyOrdered = findViewById(R.id.details_QtyOrdered);
                int ordered = Integer.parseInt(qtyOrdered.getText().toString());

                EditText qtyToCollect = findViewById(R.id.collectQuantityEditText);
                if(available <= ordered) {
                    qtyToCollect.setText(String.valueOf(available));
                }

                else if(available >= ordered) {
                    qtyToCollect.setText(String.valueOf(ordered));
                }

                else{
                    qtyToCollect.setText(0);
                }


                // Enable the prepare button once ready.
                findViewById(R.id.details_prepareButton).setEnabled(true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }



    //Async task for our lovely prepare button.
    private class PrepareTask extends AsyncTask<String, Void, String> {

        private final WeakReference<Activity> weakActivity;
        private String requisitionQuantity;

        PrepareTask(Activity myActivity) {
            this.weakActivity = new WeakReference<>(myActivity);
        }

        protected String doInBackground(String... params) {
            // Fetch the variables
            Intent intent = getIntent();
            String requisitionId = intent.getStringExtra("requisitionId");

            TextView descriptionTextView = findViewById(R.id.details_Description);
            TextView itemCodeTextView = findViewById(R.id.details_ItemCode);
            TextView qtyAvailableTextView = findViewById(R.id.details_QtyAvailable);
            TextView qtyOrderedTextView = findViewById(R.id.details_QtyOrdered);
            TextView uomTextView = findViewById(R.id.details_UOM);
            EditText quantityCollectedEditText = findViewById(R.id.collectQuantityEditText);
            EditText quantityPendingEditText = findViewById(R.id.collectQuantityEditText);
            //EditText quantityCollectedEditText = findViewById(R.id.collectQuantityEditText);

            String itemNumber = itemCodeTextView.getText().toString();
            String description = descriptionTextView.getText().toString();
            String unitOfMeasurement = uomTextView.getText().toString();
            int quantityOrdered = Integer.parseInt(qtyOrderedTextView.getText().toString());
            int quantityCollected = Integer.parseInt(quantityCollectedEditText.getText().toString());
            int quantityPending = Integer.parseInt(qtyOrderedTextView.getText().toString());
            int quantityCurrent = Integer.parseInt(qtyOrderedTextView.getText().toString());


            // Create a JSON to deliver the payload
            JSONObject j = new JSONObject();
            try {
                j.put("RequisitionId", requisitionId);
                j.put("ItemNumber", itemNumber);
                j.put("Description", description);
                j.put("UnitOfMeasurement", unitOfMeasurement);
                j.put("QuantityOrdered", quantityOrdered);
                j.put("CollectedQty", quantityCollected);
                j.put("PendingQty", quantityPending);
                j.put("CurrentInventoryQty", quantityCurrent);

            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            // Sort collected goods
            // Deduct from inventory
            return JSONParser.postStream(Constants.SERVICE_HOST + "/WarehouseCollection/Sort", j.toString());
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
}

