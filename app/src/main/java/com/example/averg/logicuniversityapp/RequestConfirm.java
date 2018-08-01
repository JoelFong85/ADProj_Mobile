package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import Adapters.MyAdapterRequestConfirm;
import Models.EmployeeRequesitionOrder;
import Models.EmployeeRequisitionOrderDetail;
import Models.reqcart;
import Utilities.Constants;
import Utilities.JSONParser;

import static Models.reqcart.emptycart;

public class RequestConfirm extends Activity {

    String request_id;
    TextView id;
    TextView date;
    TextView status;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_confirm);
        Intent i = getIntent();
        request_id = i.getStringExtra("orderid");
        emptycart();
        registerForContextMenu(findViewById(R.id.RequestConfirm));
        Log.i("EnterConfirmationPage", request_id);
        lv = (ListView) findViewById(R.id.listviewconfirm);
        id =(TextView) findViewById(R.id.textViewid);
        date = (TextView) findViewById(R.id.textViewdate);
        status = (TextView)findViewById(R.id.textViewstatus);

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

    }

    public void displayorder(EmployeeRequesitionOrder res)
    {
        emptycart();
        id.setText(res.get("RequisitionId"));
        date.setText(res.get("RequisitionDate"));
        status.setText(res.get("RequisitionStatus"));
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                // do something
                new LogoutTask(RequestConfirm.this).execute();
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
