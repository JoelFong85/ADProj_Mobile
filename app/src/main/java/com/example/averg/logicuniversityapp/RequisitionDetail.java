package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.List;

import Models.ApproveRO;
import Models.requisitionitemdetails;
import Utilities.Constants;
import Utilities.JSONParser;

public class RequisitionDetail extends Activity {
//sruthi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_detail);
        Intent i = getIntent();
        final String roid = i.getStringExtra("roid"); // get roid from the extras
        registerForContextMenu(findViewById(R.id.requisitiondetailactivity));
        new AsyncTask<Void, Void, ApproveRO>() {
            @Override
            protected ApproveRO doInBackground(Void... params) {
                return ApproveRO.getRO(roid);
            }//get the ro details based on roid

            @Override
            protected void onPostExecute(ApproveRO result) {
                TextView t1 = (TextView) findViewById(R.id.textView7);
                t1.setText(result.get("requisition_id")); //getting the requisition id and setting it as text
                TextView t2 = (TextView) findViewById(R.id.textView9);
                t2.setText(result.get("name")); //getting the name and setting it as text
                TextView t3 = (TextView) findViewById(R.id.textView11);
                t3.setText(result.get("requisition_Date")); //getting the requisition date and setting it as text
                TextView t4 = (TextView) findViewById(R.id.textView13);
                t4.setText(result.get("status")); //getting the status and setting it as text
                TextView t5 = (TextView) findViewById(R.id.textView15);
                t5.setText(result.get("sum")); //getting the sum and setting it as text
                EditText e1 = (EditText) findViewById(R.id.editText);

            }
        }.execute();

        new AsyncTask<Void, Void, List<requisitionitemdetails>>() {
            @Override
            protected List<requisitionitemdetails> doInBackground(Void... params) {
                return requisitionitemdetails.jread(roid); // get the items in a particular requisition order
            }
            @Override
            protected void onPostExecute(List<requisitionitemdetails> result) {
                ListView lv = (ListView) findViewById(R.id.listView3);
                lv.setAdapter(new SimpleAdapter
                        (RequisitionDetail.this,result,R.layout.itemdetails,
                                new String[]{"description","noofitems"},
                                new int[]{ R.id.textView5, R.id.textView6})); // setting the values to the list view
            }
        }.execute();

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApproveRO x = new ApproveRO();
                x.put("name", ((TextView) findViewById(R.id.textView9)).getText().toString());
                x.put("requisition_Date", ((TextView) findViewById(R.id.textView11)).getText().toString());
                x.put("requisition_id", ((TextView) findViewById(R.id.textView7)).getText().toString());
                x.put("status", ((EditText) findViewById(R.id.editText)).getText().toString());
                x.put("sum", ((TextView) findViewById(R.id.textView15)).getText().toString());
                new AsyncTask<ApproveRO, Void, Void>() {
                    @Override
                    protected Void doInBackground(ApproveRO... params) {
                        try {
                            ApproveRO.updateROwithapprove(params[0]); // method to approve the ro

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        Toast.makeText(RequisitionDetail.this, "Request is approved", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RequisitionDetail.this, pendingrequests.class); // going back to details upon approval
                        startActivity(i);
                        finish();
                    }
                }.execute(x);
            }
        });


        Button b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApproveRO x1 = new ApproveRO();
                x1.put("name", ((TextView) findViewById(R.id.textView9)).getText().toString());
                x1.put("requisition_Date", ((TextView) findViewById(R.id.textView11)).getText().toString());
                x1.put("requisition_id", ((TextView) findViewById(R.id.textView7)).getText().toString());
                x1.put("status", ((EditText) findViewById(R.id.editText)).getText().toString());
                x1.put("sum", ((TextView) findViewById(R.id.textView15)).getText().toString());
                new AsyncTask<ApproveRO, Void, Void>() {
                    @Override
                    protected Void doInBackground(ApproveRO... params) {
                        try {
                            ApproveRO.updateROwithreject(params[0]); // method to reject the ro

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        Toast.makeText(RequisitionDetail.this, "Request is rejected", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RequisitionDetail.this, pendingrequests.class); // going back to pending requests after rejection
                        startActivity(i);
                        finish();
                    }
                }.execute(x1);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);//to get the logout functionality
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                // do something
                new LogoutTask(RequisitionDetail.this).execute();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private class LogoutTask extends AsyncTask<String, Void, JSONObject>{

        private final WeakReference<Activity> weakActivity;

        LogoutTask(Activity myActivity) {
            this.weakActivity = new WeakReference<>(myActivity);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            // Tell the server to logout
            return JSONParser.getJSONFromUrl(Constants.SERVICE_HOST + "/Logout/" + Constants.TOKEN);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            // Clear the token
            Constants.TOKEN = "";

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }
}

