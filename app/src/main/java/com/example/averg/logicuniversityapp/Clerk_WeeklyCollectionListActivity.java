package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import Models.CollectionItem;

public class Clerk_WeeklyCollectionListActivity extends Activity implements AdapterView.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk__weekly_collection_list);
        showList();
    }

    public void showList() {
        new AsyncTask<String, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(String... params) {
                return CollectionItem.getCollectionList();
            }

            @Override
            protected void onPostExecute(List<String> result) {
                if (result.isEmpty()) {
                    Toast.makeText(Clerk_WeeklyCollectionListActivity.this, getString(R.string.noCollect), Toast.LENGTH_LONG).show();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(),
                        R.layout.collectionlist_row, R.id.textViewCollectionItem, result);
                ListView list = findViewById(R.id.listViewCollectionList);
                list.setAdapter(adapter);
                list.setOnItemClickListener(Clerk_WeeklyCollectionListActivity.this);
            }
        }.execute();
    }

    public void onItemClick(AdapterView<?> av, View v,
                            int position, long id) {
        String description = (String) av.getAdapter().getItem(position);
        Intent intent = new Intent(this, Clerk_WeeklyCollectionListDetailActivity.class);
        intent.putExtra("Description", description);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                this.onCreate(null);
            }
        }
    }


}
