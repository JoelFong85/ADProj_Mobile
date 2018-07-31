package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;

import Models.collectionhistory;

public class ViewCollectionhistory extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collectionhistory);
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
}
