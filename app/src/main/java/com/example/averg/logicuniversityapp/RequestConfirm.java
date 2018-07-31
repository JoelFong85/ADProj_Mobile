package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import Adapters.MyAdapterRequestConfirm;
import Models.EmployeeRequesitionOrder;
import Models.EmployeeRequisitionOrderDetail;
import Models.reqcart;

import static Models.reqcart.emptycart;

public class RequestConfirm extends Activity {

    String request_id;
    TextView id;
    TextView date;
    TextView status;
    ListView lv;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_confirm);
        Intent i = getIntent();
        request_id = i.getStringExtra("orderid");
        emptycart();
        Log.i("EnterConfirmationPage", request_id);
        lv = (ListView) findViewById(R.id.listviewconfirm);
        id =(TextView) findViewById(R.id.textViewid);
        date = (TextView) findViewById(R.id.textViewdate);
        status = (TextView)findViewById(R.id.textViewstatus);
        b = (Button) findViewById(R.id.buttonaddnewrequest);
        new AsyncTask<String, Void, EmployeeRequesitionOrder>(){
            @Override
            protected EmployeeRequesitionOrder doInBackground(String... params){
                return EmployeeRequesitionOrder.ShowOrder(request_id);
            }
            @Override
            protected void onPostExecute(EmployeeRequesitionOrder result){
                displayorder(result);
            }
        }.execute(request_id);


        new AsyncTask<String,Void,List<EmployeeRequisitionOrderDetail>>(){
            @Override
            protected List<EmployeeRequisitionOrderDetail> doInBackground(String... params){
                return EmployeeRequisitionOrderDetail.ShowOrderDetail(request_id);
            }
            @Override
            protected void onPostExecute(List<EmployeeRequisitionOrderDetail> result){
                MyAdapterRequestConfirm adapter = new MyAdapterRequestConfirm(getApplicationContext(), R.layout.requestcartrow, result);
                ListView lv = (ListView) findViewById(R.id.listviewconfirm);
                lv.setAdapter(adapter);
            }
        }.execute(request_id);

        b.setOnClickListener(additemListener);
    }

    public void displayorder(EmployeeRequesitionOrder res)
    {
        emptycart();
        id.setText(res.get("RequisitionId"));
        date.setText(res.get("RequisitionDate"));
        status.setText(res.get("RequisitionStatus"));
    }

    public View.OnClickListener additemListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Intent i= new Intent(getApplicationContext(),NewRequestActivity.class);
            startActivity(i);
        }

    };

}
