package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import Adapters.InventoryAdapter;
import Models.Inventory;
import Utilities.Constants;
import Utilities.JSONParser;

public class User_RequestFormActivity extends Activity{

    // Used to keep track of inventory items for the activity
    ArrayList<Inventory> invList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request_form);

        // Fetch all inventory items
        new InventoryTask(this).execute("", "", "");

        // Create a textWatcher
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayList<Inventory> dupeInvList = new ArrayList<>();

                // Filter the dupe inv list based on the text change
                for(Inventory inv : invList){
                    if(containsCategoryOrDescription(inv)){
                        dupeInvList.add(inv);
                    }
                }

                // Remake the adapter
                InventoryAdapter adapter = new InventoryAdapter(getApplicationContext(), dupeInvList);
                ListView listView = findViewById(R.id.inventoryListView);
                listView.setAdapter(adapter);
            }
        };

        // Add textwatcher listeners to the editTexts
        final TextView categoryEditText = findViewById(R.id.categoryEditText);
        final TextView descriptionEditText = findViewById(R.id.descriptionEditText);
        categoryEditText.addTextChangedListener(textWatcher);
        descriptionEditText.addTextChangedListener(textWatcher);





    }


    // Activity utility: Check if list item contains words from category or description
    private Boolean containsCategoryOrDescription(Inventory i){
        final TextView categoryEditText = findViewById(R.id.categoryEditText);
        final TextView descriptionEditText = findViewById(R.id.descriptionEditText);
        if(i.getCategory().trim().toLowerCase().contains(categoryEditText.getText()) || i.getDescription().trim().toLowerCase().contains(descriptionEditText.getText())){
            return true;
        }

        else{
            return false;
        }
    }


    private class InventoryTask extends AsyncTask<String, Void, JSONArray> {

        private final WeakReference<Activity> weakActivity;

        InventoryTask(Activity myActivity) {
            this.weakActivity = new WeakReference<>(myActivity);
        }


        protected JSONArray doInBackground(String... params) {
            return JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST + "/Inventory/List");
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            try {

                // Convert JSONArray into list items.
                for(int i = 0; i < result.length(); i++){
                    JSONObject objectI = result.getJSONObject(i);
                    invList.add(new Inventory(objectI.getString("ItemNumber"),
                            objectI.getString("Description"),
                            objectI.getString("Category"),
                            objectI.getString("UnitOfMeasurement"),
                            objectI.getInt("CurrentQuantity"),
                            objectI.getInt("ReorderLevel"),
                            objectI.getInt("ReorderQuantity"),
                            objectI.getInt("ItemBin"),
                            objectI.getString("ItemStatus"),
                            objectI.getInt("PriorityUnitPrice")));
                }

                InventoryAdapter adapter = new InventoryAdapter(weakActivity.get(), invList);
                ListView listView = findViewById(R.id.inventoryListView);
                listView.setAdapter(adapter);


            } catch (Exception ex) {
                Toast t = Toast.makeText(getApplicationContext(), "die", Toast.LENGTH_SHORT);
                t.show();
                ex.printStackTrace();
            }

        }
    }


}
