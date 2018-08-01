package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import Models.Adjustment;
import Models.CollectionItem;

public class Clerk_WeeklyCollectionListDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk__weekly_collection_list_detail);
        Intent i = getIntent();
        final String description = i.getStringExtra("Description");

        final TextView txt_description = (TextView) findViewById(R.id.textView_ColItem_description1);
        final TextView itemNum = (TextView) findViewById(R.id.textView_ColItem_ItemNum1);
        final TextView uom = (TextView) findViewById(R.id.textView_ColItem_uom1);
        final TextView availQty = (TextView) findViewById(R.id.textView_ColItem_availableQty1);
        final EditText collectQty = (EditText) findViewById(R.id.editText_ColItem_collectQty1);
        final TextView orderQty = (TextView) findViewById(R.id.textView_ColItem_orderQty1);

        final TextView pendingAdjRemoveQty = (TextView) findViewById(R.id.textView_ColItem_pendingAdjRemove1);
        final EditText reason = (EditText) findViewById(R.id.editText_reason);
        final EditText adjQty = (EditText) findViewById(R.id.editText_ColItem_adjQty1);
        adjQty.setText("0");

        //Load details
        getCollectionItemDetails(description);

        //Cancel button
        Button btnCancel = (Button) findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        //Prepare button
        Button btnPrep = (Button) findViewById(R.id.button_prepare);
        btnPrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validating Collection Qty
                try {
                    int num = Integer.parseInt(collectQty.getText().toString());
                    int num1 = Integer.parseInt(adjQty.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(Clerk_WeeklyCollectionListDetailActivity.this, getString(R.string.enterNumber), Toast.LENGTH_SHORT).show();
                    return;
                }

                TextView availQty = (TextView) findViewById(R.id.textView_ColItem_availableQty1);
                TextView orderQty = (TextView) findViewById(R.id.textView_ColItem_orderQty1);
                int availableQty = Integer.parseInt(availQty.getText().toString());
                int orderedQty = Integer.parseInt(orderQty.getText().toString());
                int collectedQty = Integer.parseInt(collectQty.getText().toString());


                //prevent -ve collection
                if (collectedQty < 0) {
                    Toast.makeText(Clerk_WeeklyCollectionListDetailActivity.this, getString(R.string.notZero), Toast.LENGTH_SHORT).show();
                    return;
                }

                //checking collected qty is less than ordered or what is available in inventory
                if (collectedQty > orderedQty || collectedQty > availableQty) {
                    Toast.makeText(Clerk_WeeklyCollectionListDetailActivity.this, getString(R.string.collectQtyTooBig), Toast.LENGTH_SHORT).show();
                    return;
                }

                int adjustedQty = 0;
                //checking if adjust qty is null or "". then adj qty = 0
                if ((adjQty.getText().toString()).matches("")) {
                    adjustedQty = 0; //Integer.parseInt(adjQty.getText().toString());
                } else {
                    adjustedQty = Integer.parseInt(adjQty.getText().toString()); //otherwise adj qty = what is in edittext
                }

                EditText etAdjReason = (EditText) findViewById(R.id.editText_reason);
                String inputAdjReason = etAdjReason.getText().toString();

                //validate adj qty + collected qty is available in inventory
                if (adjustedQty < (collectedQty - availableQty)) {
                    Toast.makeText(Clerk_WeeklyCollectionListDetailActivity.this, getString(R.string.adjQtyTooBig), Toast.LENGTH_SHORT).show();
                    return;
                }

                String ci_descrip = txt_description.getText().toString();
                String ci_itemNumber = itemNum.getText().toString();
                String ci_uom = uom.getText().toString();
                String ci_currentQty = availQty.getText().toString();
                String ci_collectQty = collectQty.getText().toString();
                String ci_orderQty = orderQty.getText().toString();

                CollectionItem ci = new CollectionItem(ci_descrip, ci_itemNumber, ci_uom, ci_currentQty, ci_collectQty, ci_orderQty);

                Adjustment adj = new Adjustment(ci_itemNumber, Integer.toString(adjustedQty), inputAdjReason);


                new AsyncTask<Adjustment, Void, Void>() {
                    @Override
                    protected Void doInBackground(Adjustment... params) {
                        Adjustment.CreateAdj(params[0]);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
//                        Intent intent = new Intent();
//                        setResult(RESULT_OK, intent);
//                        finish();
                    }
                }.execute(adj);

                new AsyncTask<CollectionItem, Void, Void>() {
                    @Override
                    protected Void doInBackground(CollectionItem... params) {
                        CollectionItem.updateCollectionItem(params[0]);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }.execute(ci);


            }
        });
    }

    public void getCollectionItemDetails(String description) {
        AsyncTask<String, Void, CollectionItem> task = new AsyncTask<String, Void, CollectionItem>() {
            @Override
            protected CollectionItem doInBackground(String... params) {
                CollectionItem ci = new CollectionItem();
                ci = CollectionItem.getCollectionItemDetails(params[0]);
                return ci;
            }

            @Override
            protected void onPostExecute(CollectionItem result) {
                TextView txt_description = (TextView) findViewById(R.id.textView_ColItem_description1);
                txt_description.setText(result.get("Description"));

                TextView itemNum = (TextView) findViewById(R.id.textView_ColItem_ItemNum1);
                itemNum.setText(result.get("ItemNumber"));

                TextView uom = (TextView) findViewById(R.id.textView_ColItem_uom1);
                uom.setText(result.get("UnitOfMeasurement"));

                TextView availQty = (TextView) findViewById(R.id.textView_ColItem_availableQty1);
                availQty.setText(result.get("CurrentInventoryQty"));

                TextView orderQty = (TextView) findViewById(R.id.textView_ColItem_orderQty1);
                orderQty.setText(result.get("QuantityOrdered"));

                EditText collectQty = (EditText) findViewById(R.id.editText_ColItem_collectQty1);

                int availableQty = Integer.parseInt(result.get("CurrentInventoryQty"));
                int orderedQty = Integer.parseInt(result.get("QuantityOrdered"));

                if (availableQty < orderedQty) {
                    collectQty.setText(result.get("CurrentInventoryQty"));
                } else {
                    collectQty.setText(result.get("QuantityOrdered"));
                }

                TextView pendingAdjRemove = (TextView) findViewById(R.id.textView_ColItem_pendingAdjRemove1);
                pendingAdjRemove.setText(result.get("pending_adj_remove"));
            }
        };
        task.execute(description);
    }

//    private class CreateAdjustment extends AsyncTask<Adjustment, Void, String> {
//        private WeakReference<Activity> weakActivity;
//
//        public CreateAdjustment(Activity myactivity) {
//            this.weakActivity = new WeakReference<>(myactivity);
//        }
//
//        @Override
//        protected String doInBackground(Adjustment... params) {
//            Adjustment adj = params[0];
//            return Adjustment.CreateAdj(adj);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            //showAToast(s);
//        }
//    }
}


//
//                TextView tvPendingAdjRemoveQty = (TextView) findViewById(R.id.textView_ColItem_pendingAdjRemove1);
//                int PendingAdjRemoveQty = Integer.parseInt(tvPendingAdjRemoveQty.getText().toString());
//
//
//                if (inputAdjQty > (PendingAdjRemoveQty + availableQty)) {
//                    Toast.makeText(Clerk_WeeklyCollectionListDetailActivity.this, getString(R.string.adjQtyTooBig), Toast.LENGTH_SHORT).show();
//                    return;
//                }
