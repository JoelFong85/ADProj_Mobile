package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.List;

import Models.ApproveRO;
import Models.requisitionitemdetails;

public class RequisitionDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_detail);
        Intent i = getIntent();
        final String roid = i.getStringExtra("roid");
        new AsyncTask<Void, Void, ApproveRO>() {
            @Override
            protected ApproveRO doInBackground(Void... params) {
                return ApproveRO.getRO(roid);
            }

            @Override
            protected void onPostExecute(ApproveRO result) {
                TextView t1 = (TextView) findViewById(R.id.textView7);
                t1.setText(result.get("requisition_id"));
                TextView t2 = (TextView) findViewById(R.id.textView9);
                t2.setText(result.get("name"));
                TextView t3 = (TextView) findViewById(R.id.textView11);
                t3.setText(result.get("requisition_Date"));
                TextView t4 = (TextView) findViewById(R.id.textView13);
                t4.setText(result.get("status"));
                TextView t5 = (TextView) findViewById(R.id.textView15);
                t5.setText(result.get("sum"));
                EditText e1 = (EditText) findViewById(R.id.editText);

            }
        }.execute();

        new AsyncTask<Void, Void, List<requisitionitemdetails>>() {
            @Override
            protected List<requisitionitemdetails> doInBackground(Void... params) {
                return requisitionitemdetails.jread(roid);
            }
            @Override
            protected void onPostExecute(List<requisitionitemdetails> result) {
                ListView lv = (ListView) findViewById(R.id.listView3);
                lv.setAdapter(new SimpleAdapter
                        (RequisitionDetail.this,result,R.layout.itemdetails,
                                new String[]{"description","noofitems"},
                                new int[]{ R.id.textView5, R.id.textView6}));
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
                            ApproveRO.updateROwithapprove(params[0]);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        Toast.makeText(RequisitionDetail.this, "Request is approved", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RequisitionDetail.this, pendingrequests.class);
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
                            ApproveRO.updateROwithreject(params[0]);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        Toast.makeText(RequisitionDetail.this, "Request is rejected", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RequisitionDetail.this, pendingrequests.class);
                        startActivity(i);
                        finish();
                    }
                }.execute(x1);
            }
        });
    }
}

