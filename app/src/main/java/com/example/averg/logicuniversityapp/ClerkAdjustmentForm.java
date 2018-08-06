package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import Models.Adjustment;
import Models.Inventory;
import Utilities.Constants;
//esther
public class ClerkAdjustmentForm extends Activity {
    String ItemNumber;
    Toast toast;
    Inventory a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_adjustment_form);

        Intent intent = getIntent();
        ItemNumber = intent.getStringExtra("item_number");
        new FetchItemDetails(ClerkAdjustmentForm.this).execute(ItemNumber);

        Button submitbtn = findViewById(R.id.buttonAdjSubmit);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etAdjQty = findViewById(R.id.editTextAdjQty);
                EditText etAdjReason = findViewById(R.id.editTextReason);
                TextView tvAdjQty = findViewById(R.id.tvAdjRemoveQtyValue);
                String qty = String.valueOf(etAdjQty.getText());
                Integer pendingadjqty = Integer.parseInt(a.get("pending_adj_remove"));
                Integer currentqty = Integer.parseInt(a.get("current_quantity"));
                String reason = etAdjReason.getText().toString();
                if (!qty.isEmpty() && !qty.equals("null") && qty != null && qty.trim() != "") {
                    Integer adjqty = Integer.parseInt(qty);
                    if (adjqty < 0 && Math.abs(adjqty) > (pendingadjqty + currentqty)) {
                        if(currentqty==0){
                            showAToast("Current qty is 0. No removal request allowed.");
                        }else{
                            showAToast("Adj qty cannot be less than" + -(pendingadjqty + currentqty));
                        }
                    } else if (adjqty != 0) {
                        try {
                            Adjustment adj = new Adjustment(ItemNumber, qty, reason);
                            try {
                                new CreateAdjustment(ClerkAdjustmentForm.this).execute(adj);
                                etAdjQty.setText("");
                                etAdjReason.setText("");
                            } catch (Exception e) {
                                Log.e("CreateAdjustment", "Submissionfailed");
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            Log.e("CreateAdjustment", "CreationFailed");
                            e.printStackTrace();
                        }
                    } else {
                        showAToast("Qty cannot be 0");
                    }
                } else {
                    showAToast("Enter qty");
                }

            }
        });
    }
//show toast method
    public void showAToast(String st) { //"Toast toast" is declared in the class
        try {
            toast.getView().isShown();     // true if visible
            toast.setText(st);
        } catch (Exception e) {         // invisible if exception
            toast = Toast.makeText(ClerkAdjustmentForm.this, st, Toast.LENGTH_LONG);
        }
        toast.show();  //finally display it
    }
//create adustment async task
    private class CreateAdjustment extends AsyncTask<Adjustment, Void, String> {
        private WeakReference<Activity> weakActivity;

        public CreateAdjustment(Activity myactivity) {
            this.weakActivity = new WeakReference<>(myactivity);
        }

        @Override
        protected String doInBackground(Adjustment... params) {
            Adjustment adj = params[0];
            return Adjustment.CreateAdj(adj);
        }

        @Override
        protected void onPostExecute(String s) {
            Intent Invintent=new Intent(ClerkAdjustmentForm.this, ClerkAdjustmentInventoryActivity.class);
            ClerkAdjustmentForm.this.startActivity(Invintent);
            ClerkAdjustmentForm.this.finish();
            String message = s;
            if(message.isEmpty()){
                s=ItemNumber.trim()+" adj request submitted";
            }
            showAToast(s);
        }
    }
//fetch item details async task
    private class FetchItemDetails extends AsyncTask<String, Void, Inventory> {
        private WeakReference<Activity> weakActivity;

        public FetchItemDetails(Activity myactivity) {
            this.weakActivity = new WeakReference<>(myactivity);
        }

        @Override
        protected Inventory doInBackground(String... params) {
            String itemcode = params[0];
            return Inventory.GetInventoryByItemCode(itemcode);
        }

        @Override
        protected void onPostExecute(Inventory inventory) {
            a = inventory;
            TextView tvCode = findViewById(R.id.tvAdjCodeValue);
            TextView tvDesc = findViewById(R.id.tvAdjDescValue);
            TextView tvUOM = findViewById(R.id.tvAdjUOMvalue);
            TextView tvCurQ = findViewById(R.id.tvAdjCurrQtyValue);
            TextView tvPAdremove = findViewById(R.id.tvAdjRemoveQtyValue);
            tvCode.setText(inventory.get("item_number"));
            tvDesc.setText(inventory.get("description"));
            tvUOM.setText(inventory.get("unit_of_measurement"));
            tvCurQ.setText(inventory.get("current_quantity"));
            tvPAdremove.setText(inventory.get("pending_adj_remove"));
        }
    }
}
