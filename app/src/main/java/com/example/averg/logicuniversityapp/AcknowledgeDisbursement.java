package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Adapters.MyAdpaterAcknowledgeDisbursement;
import Models.Disbursement_Detail;
import Models.Disbursement_List;
import Utilities.InputFilterMinMax;

import static Models.Disbursement_Detail.updatesupplyquantity;
import static Models.Disbursement_List.clearlist;

public class AcknowledgeDisbursement extends Activity implements AdapterView.OnItemClickListener {

    String collect_id;
    TextView de;
    TextView lo;
    TextView t;
    TextView da;
    ListView lv;
    TextView t2;
    TextView t3;
    int dep_pin;
    EditText pin;
    Button ack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acknowledge_disbursement);
        Intent i = getIntent();
        collect_id = i.getStringExtra("collection_id").trim();
        dep_pin = Integer.parseInt(i.getStringExtra("department_pin").trim());
        Disbursement_List l = Disbursement_List.selectid(collect_id);
        de = (TextView) findViewById(R.id.textViewADepname);
        lo = (TextView) findViewById(R.id.textViewAlocation);
        t = (TextView) findViewById(R.id.textViewATime);
        da= (TextView) findViewById(R.id.textViewADate);
        lv = (ListView) findViewById(R.id.listacknowledge);
        pin = (EditText) findViewById(R.id.editTextpin);
        pin.addTextChangedListener(searchBoxWatcher);
        ack = (Button) findViewById(R.id.buttonacknowledge);
        ack.setEnabled(false);

        de.setText(l.get("department_name"));
        lo.setText(l.get("collection_location"));
        t.setText(l.get("collection_time"));
        da.setText(l.get("collection_date"));
        setuplistviewcontent();
        lv.setOnItemClickListener(this);
        ack.setOnClickListener(acknowledgereceipt);
    }

    public void setuplistviewcontent()
    {
        new AsyncTask<String,Void,List<Disbursement_Detail>>(){
            @Override
            protected List<Disbursement_Detail> doInBackground(String... params){
                return Disbursement_Detail.getdetail(collect_id);
            }
            @Override
            protected void onPostExecute(List<Disbursement_Detail> result){
                MyAdpaterAcknowledgeDisbursement adapter = new MyAdpaterAcknowledgeDisbursement(getApplicationContext(), R.layout.disbursementdetailrow, result);
                ListView lv = (ListView) findViewById(R.id.listacknowledge);
                lv.setAdapter(adapter);
            }
        }.execute(collect_id);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final Disbursement_Detail item = (Disbursement_Detail) parent.getItemAtPosition(position);
        int supply_og = Integer.parseInt(item.get("receive_quantity"));
        int supply_alt = Integer.parseInt(item.get("altered_quantity"));
        final Dialog d = new Dialog(this);
        d.setTitle(getString(R.string.customdialogtitle));
        d.setContentView(R.layout.customdialogacknowledgecollection);
        d.setCancelable(false);

        t2 = (TextView)d. findViewById(R.id.textViewoq);
        t2.setText(item.get("order_quantity"));
        t3 = (TextView) d.findViewById(R.id.textViewrqoriginal);
        t3.setText(item.get("receive_quantity"));
        final EditText et = (EditText)d.findViewById(R.id.editTextrqedit);
        et.setText(Integer.toString(supply_alt));
        et.setFilters(new InputFilter[]{new InputFilterMinMax(0, supply_og )});
        Button ub = (Button)d.findViewById(R.id.buttonupdateack);
        ub.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(et.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Quantity should be entered", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int i = Integer.parseInt(et.getText().toString());
                    updatesupplyquantity(item.get("item_number"), et.getText().toString());
                    setuplistviewcontentupdated();
                    d.dismiss();
                }
            }
        });
        d.show();
    }

    public void setuplistviewcontentupdated()
    {
        new AsyncTask<Void,Void,List<Disbursement_Detail>>(){
            @Override
            protected List<Disbursement_Detail> doInBackground(Void... params){
                return Disbursement_Detail.getDLDetail();
            }
            @Override
            protected void onPostExecute(List<Disbursement_Detail> result){
                MyAdpaterAcknowledgeDisbursement adapter = new MyAdpaterAcknowledgeDisbursement(getApplicationContext(), R.layout.disbursementdetailrow, result);
                ListView lv = (ListView) findViewById(R.id.listacknowledge);
                lv.setAdapter(adapter);
            }
        }.execute();
    }


    //Method for TextWatcher
    private final TextWatcher searchBoxWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            //Display the images based on the books found via title
            //To run the UI search on the background and UI display on the front using abstract method of AsyncTasking
            //AsyncTask<[data type]input, [data type]progress, [data type]result>>

            if(pin.getText().toString().isEmpty())
            {
                ack.setEnabled(false);
            }
            else
            {
            new AsyncTask<Integer, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Integer... params) {
                    return Disbursement_List.verifypin(params[0],params[1]);
                }
                @Override
                protected void onPostExecute(Boolean result) {
                    //Make a Toast to inform user that no data found based on input
                    if (result== true) {
                        ack.setEnabled(true);
                        Toast t = Toast.makeText(getApplicationContext(), "PIN Verified", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    else
                    {
                        ack.setEnabled(false);
                        Toast t = Toast.makeText(getApplicationContext(), "Department PIN not matched", Toast.LENGTH_SHORT);
                        t.show();
                    }
                }
            }.execute(dep_pin, Integer.parseInt(pin.getText().toString()));
            }
        }
    };

    public View.OnClickListener acknowledgereceipt = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                Disbursement_Detail d;
            for(int i=0;i<lv.getAdapter().getCount();i++)
            {
                d= (Disbursement_Detail)lv.getAdapter().getItem(i);
                if(d.get("receive_quantity").equals(d.get("altered_quantity")))
                {

                }
                else
                {
                    new AsyncTask<Object, Void, Void>() {
                        @Override
                        protected Void doInBackground(Object... params) {
                        Disbursement_Detail.acknowledgereceiptdetails((Disbursement_Detail)params[0], (String)params[1]);
                        return  null;
                        }

                        @Override
                        protected void onPostExecute(Void param) {
                        }
                    }.execute(lv.getAdapter().getItem(i),collect_id );
                }
            }

            new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... params) {
                    return Disbursement_Detail.updatecollectionstatus(collect_id);
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    if (result) {
                        clearlist();
                        Toast t = Toast.makeText(getApplicationContext(), "Acknowledged", Toast.LENGTH_SHORT);
                        t.show();
                        Intent i = new Intent(getApplicationContext(), DisbursementList.class);
                        startActivity(i);
                        finish();
                    }

                    else {
                        Toast t = Toast.makeText(getApplicationContext(), "Error in Acknowledgement", Toast.LENGTH_SHORT);
                        t.show();
                    }
                }
            }.execute(collect_id);
        }
    };




}
