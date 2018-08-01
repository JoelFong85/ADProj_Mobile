package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import Models.collectionhistory;
import Utilities.Constants;
import Utilities.JSONParser;

public class ViewCollectionhistory extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collectionhistory);
        registerForContextMenu(findViewById(R.id.viewcollectionhistoryactivity));
        new AsyncTask<Void, Void, List<collectionhistory>>() {
            @Override
            protected List<collectionhistory> doInBackground(Void... params) {
                return collectionhistory.jread();
            }
            @Override
            protected void onPostExecute(List<collectionhistory> result) {
                ListView lv = (ListView) findViewById(R.id.listView2);
                lv.setAdapter(new SimpleAdapter
                        (ViewCollectionhistory.this,result, android.R.layout.simple_list_item_2,
                                new String[]{"collectionDate", "collectionplace"},
                                new int[]{ android.R.id.text1, android.R.id.text2}));
            }
        }.execute();
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
                new LogoutTask(ViewCollectionhistory.this).execute();
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
