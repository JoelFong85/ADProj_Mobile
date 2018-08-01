package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import Adapters.MyAdapterDisbursementList;
import Models.Disbursement_List;

public class DisbursementList extends Activity implements AdapterView.OnItemClickListener {

    ListView lv;
    Button b;
    TextView l;
    EditText et;
    String select_id;
    String d;
    String pin;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement_list);
        lv = (ListView)findViewById(R.id.listviewdisburse);
        b = (Button) findViewById(R.id.buttonsearch);
        l=(TextView)findViewById(R.id.textViewlabel);
        et = (EditText) findViewById(R.id.editTextdate);
        setuplistviewcontent();
        b.setOnClickListener(searchbydate);
        lv.setOnItemClickListener(this);
        et.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog pickerDialog = new DatePickerDialog(
                        DisbursementList.this, onDateSetListener, year, month, day );
                pickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month+1;
                    String date;
                    if(month<10 & day<10)
                    {
                        date = "0"+day+"-"+"0"+month+"-"+year;
                    }
                    else if( (month < 10) & (day >=10))
                    {
                        date = day+"-"+"0"+month+"-"+year;
                    }
                    else if((month >=10) & (day <10))
                    {
                        date = "0"+day+"-"+month+"-"+year;
                    }
                    else
                    {
                        date = day+"-"+month+"-"+year;
                    }
                et.setText(date);
            }
        };
    }

    public void setuplistviewcontent()
    {
        new AsyncTask<Void,Void,List<Disbursement_List>>(){
            @Override
            protected List<Disbursement_List> doInBackground(Void... params){
                return Disbursement_List.listall();
            }
            @Override
            protected void onPostExecute(List<Disbursement_List> result){
                MyAdapterDisbursementList adapter = new MyAdapterDisbursementList(getApplicationContext(), R.layout.disbursementrow, result);
                ListView lv = (ListView) findViewById(R.id.listviewdisburse);
                lv.setAdapter(adapter);
                if(result.size()>0) {
                    b.setEnabled(true);
                    l.setText("");
                }
                else
                {   b.setEnabled(false);
                    l.setText("No Pending Disbursement List");
                }
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Disbursement_List item = (Disbursement_List) parent.getItemAtPosition(position);
        select_id = item.get("collection_id");
        pin =  item.get("department_pin");
        Log.i("collection_id", select_id);
        Intent i = new Intent(getApplicationContext(), AcknowledgeDisbursement.class);
        i.putExtra("collection_id", select_id);
        i.putExtra("department_pin", pin);
        startActivity(i);
    }

    public View.OnClickListener searchbydate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            d = et.getText().toString();
            if(d.isEmpty())
            {
                setuplistviewcontent();
            }
            else {
                new AsyncTask<String, Void, List<Disbursement_List>>() {
                    @Override
                    protected List<Disbursement_List> doInBackground(String... params) {
                        return Disbursement_List.selectdate(d);
                    }

                    @Override
                    protected void onPostExecute(List<Disbursement_List> result) {
                        MyAdapterDisbursementList adapter = new MyAdapterDisbursementList(getApplicationContext(), R.layout.disbursementrow, result);
                        ListView lv = (ListView) findViewById(R.id.listviewdisburse);
                        lv.setAdapter(adapter);
                        if (result.size() > 0) {
                            b.setEnabled(true);
                            l.setText("");
                        } else {
                            l.setText("No Pending Disbursement List");
                        }
                    }
                }.execute(d);
            }
        }
    };
}
