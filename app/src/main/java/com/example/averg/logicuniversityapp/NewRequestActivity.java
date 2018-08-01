package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import Adapters.MyAdapterNewrequest;
import Models.Inventory;
import Models.reqcart;
import Utilities.Constants;
import Utilities.JSONParser;

public class NewRequestActivity extends Activity implements AdapterView.OnItemClickListener {

    String select_id;
    ListView lv;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        lv = (ListView)findViewById(R.id.list1);
        Button b = (Button) findViewById(R.id.button);
        et = (EditText)findViewById(R.id.editText);
        et.addTextChangedListener(searchBoxWatcher);
        registerForContextMenu(findViewById(R.id.newrequestactivitiy));
        new AsyncTask<Void, Void, List<Inventory>>() {
            @Override
            protected List<Inventory> doInBackground(Void... params) {
                return Inventory.list();
            }

            @Override
            protected void onPostExecute(List<Inventory> result) {
                MyAdapterNewrequest adapter = new MyAdapterNewrequest(NewRequestActivity.this, R.layout.newrequestrow, result);
                ListView lv = (ListView)findViewById(R.id.list1);
                lv.setAdapter(adapter);
//                setListAdapter(adapter);
            }
        }.execute();
        lv.setOnItemClickListener(this);
        b.setOnClickListener(viewcartlistener);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Inventory item = (Inventory) parent.getItemAtPosition(position);
                select_id = item.get("item_number");

                new AsyncTask<String, Void, Inventory>() {
                    @Override
                    protected Inventory doInBackground(String... params) {
                        return Inventory.getinventory(select_id);
                    }

                    @Override
                    protected void onPostExecute(Inventory result) {
                        reqcart.additemtocart(result);
                        Toast.makeText(getApplicationContext(), "Item " + select_id + " added to request cart", Toast.LENGTH_SHORT).show();
                    }
                }.execute(select_id);
            }

    public View.OnClickListener viewcartlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), Request_Cart.class);
            startActivity(i);
        }
    };

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
            new AsyncTask<String, Void, List<Inventory>>() {
                @Override
                protected List<Inventory> doInBackground(String... params) {
                    return Inventory.getsearchlist(params[0]);
                }
                @Override
                protected void onPostExecute(List<Inventory> result) {
                    //Make a Toast to inform user that no data found based on input
                    if (result.isEmpty()) {
                        Toast t = Toast.makeText(getApplicationContext(), "Item not found", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    else
                    {
                        MyAdapterNewrequest adapter = new MyAdapterNewrequest(NewRequestActivity.this, R.layout.newrequestrow, result);
                        ListView lv = (ListView)findViewById(R.id.list1);
                        lv.setAdapter(adapter);
                    }
                }
            }.execute(et.getText().toString());
        }
    };

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
                new LogoutTask(NewRequestActivity.this).execute();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}