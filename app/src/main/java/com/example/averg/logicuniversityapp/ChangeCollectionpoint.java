package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.List;

import Models.collectionpoint;
import Utilities.Constants;
import Utilities.JSONParser;

public class ChangeCollectionpoint extends Activity{
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_collectionpoint);
        Spinner dropdown = (Spinner) findViewById(R.id.spinner4);
        registerForContextMenu(findViewById(R.id.collectionpointactivity));
        new AsyncTask<Void,Void, List<collectionpoint>>() {
            @Override
            protected List<collectionpoint> doInBackground(Void... params) {
                return collectionpoint.jread();
            }
            @Override
            protected void onPostExecute(List<collectionpoint> result) {
                Spinner dropdown = (Spinner) findViewById(R.id.spinner4);
                String[] points=new String[result.size()];
                collectionpoint.Values=new String[result.size()];
                for(int i=0;i<result.size();i++)
                {
                    points[i]=result.get(i).get("collectionplace");
                    collectionpoint.Values[i]=result.get(i).get("id");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChangeCollectionpoint.this,android.R.layout.simple_spinner_item,points);
                adapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);
            }
        }.execute();
        b=(Button)findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.setEnabled(false);
                b.setText("Please Wait...");
                Spinner s1=(Spinner)findViewById(R.id.spinner4);
                int i=s1.getSelectedItemPosition();
                String id1=collectionpoint.Values[i];
                collectionpoint c1=new collectionpoint();
                c1.put("collectionplace",s1.getSelectedItem().toString());
                c1.put("id",id1);
                new AsyncTask<collectionpoint, Void, Void>() {
                    @Override
                    protected Void doInBackground(collectionpoint... params) {
                        try {
                            collectionpoint.updatecollection(params[0]);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        Intent i = new Intent(getApplicationContext(), DepartmentRep.class);
                        startActivity(i);
                        Toast.makeText(ChangeCollectionpoint.this,"Location is Changed",Toast.LENGTH_SHORT).show();
                    }
                }.execute(c1);
            }
        });
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
                new LogoutTask(ChangeCollectionpoint.this).execute();
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
