package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Adapters.MyAdapterRequestCart;
import Models.reqcart;

import static Models.reqcart.removeitem;
import static Models.reqcart.updatequantity;

public class Request_Cart extends Activity implements AdapterView.OnItemClickListener{

    ListView lv;
    Button b;
    Button p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__cart);
        lv = (ListView)findViewById(R.id.listviewcart);
        b = (Button) findViewById(R.id.buttonplacerequest);
        p=(Button)findViewById(R.id.buttonadditem);
        setuplistviewcontent();
        b.setOnClickListener(placerequestlistener);
        p.setOnClickListener(additemListener);
        lv.setOnItemClickListener(this);
    }

    public void setuplistviewcontent()
    {
        new AsyncTask<Void,Void,List<reqcart>>(){
            @Override
            protected List<reqcart> doInBackground(Void... params){
                return reqcart.showcart();
            }
            @Override
            protected void onPostExecute(List<reqcart> result){
                    MyAdapterRequestCart adapter = new MyAdapterRequestCart(getApplicationContext(), R.layout.requestcartrow, result);
                    ListView lv = (ListView) findViewById(R.id.listviewcart);
                    lv.setAdapter(adapter);
                if(result.size()>0) {
                    b.setEnabled(true);
                }
                else
                {   b.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "No Item in Cart", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final reqcart item = (reqcart) parent.getItemAtPosition(position);
        int q = item.getQ();
        Log.i("q", Integer.toString(q));
        final Dialog d = new Dialog(this);
        d.setTitle(getString(R.string.customdialogtitle));
        d.setContentView(R.layout.customdiaglogeditrequest);
        d.setCancelable(false);
            TextView t = (TextView) findViewById(R.id.textView);
            final EditText et = (EditText)d.findViewById(R.id.editText3);
            et.setText(Integer.toString(q));
            Button rb = (Button)d.findViewById(R.id.buttonremove);
            Button ub = (Button)d.findViewById(R.id.buttonupdate);
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
                        updatequantity(item.getInv(),i);
                        setuplistviewcontent();
                        d.dismiss();
                    }
                }
            });

         rb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                    removeitem(item.getInv());
                    setuplistviewcontent();
                    d.dismiss();
                }
            });
        d.show();
    }

    public View.OnClickListener placerequestlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            b.setEnabled(false);

            new AsyncTask<Void, Void, String>(){
                @Override
                protected String doInBackground(Void... params){
                    return reqcart.placerequest();
                }
                @Override
                protected void onPostExecute(String result){
                    Intent i = new Intent(getApplicationContext(), RequestConfirm.class);
                    i.putExtra("orderid", result.replace("\"", ""));
                    startActivity(i);
                }
            }.execute();
        }
    };

    public View.OnClickListener additemListener = new View.OnClickListener(){
      @Override
        public void onClick(View view){
          Intent i= new Intent(getApplicationContext(),NewRequestActivity.class);
          startActivity(i);
      }

    };
}

