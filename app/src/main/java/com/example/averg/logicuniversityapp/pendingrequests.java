package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import Models.ApproveRO;
import Utilities.Constants;
import Utilities.JSONParser;

public class pendingrequests extends Activity  implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        ListView lv = (ListView) findViewById(R.id.listView1);
        new AsyncTask<Void, Void, List<ApproveRO>>() {
            @Override
            protected List<ApproveRO> doInBackground(Void... params) {
                return ApproveRO.jread();
            }
            @Override
            protected void onPostExecute(List<ApproveRO> result) {
                ListView lv = (ListView) findViewById(R.id.listView1);
                lv.setAdapter(new SimpleAdapter
                        (pendingrequests.this,result,R.layout.row,
                                new String[]{"requisition_id", "name","status","sum"},
                                new int[]{R.id.textView1, R.id.text2,R.id.text3,R.id.text4}));
            }
        }.execute();
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int position, long id) {
        ApproveRO r = (ApproveRO) av.getAdapter().getItem(position);
        Intent i = new Intent(this, RequisitionDetail.class);
        i.putExtra("roid", r.get("requisition_id"));
        startActivity(i);
    }
}
