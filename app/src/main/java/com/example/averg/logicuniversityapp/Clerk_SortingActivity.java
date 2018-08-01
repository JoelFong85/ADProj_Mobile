package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import Models.Department;
import Models.DepartmentSorter;

public class Clerk_SortingActivity extends Activity implements AdapterView.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk__sorting);
        showList();
    }

    public void showList() {
        AsyncTask<String, Void, List<String>> a = new AsyncTask<String, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(String... params) {
                return DepartmentSorter.displayListofDepartmentsForCollection();
            }

            @Override
            protected void onPostExecute(List<String> result) {
                if (result.isEmpty()){
                    Toast.makeText(Clerk_SortingActivity.this, getString(R.string.noSort), Toast.LENGTH_LONG).show();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(),
                        R.layout.collectionlist_row, R.id.textViewCollectionItem, result);
                ListView list = findViewById(R.id.listView_Sorting);
                list.setAdapter(adapter);
                list.setOnItemClickListener(Clerk_SortingActivity.this);
            }
        }.execute();
    }

    public void onItemClick(AdapterView<?> av, View v,
                            int position, long id) {
        String department = (String) av.getAdapter().getItem(position);
        Intent intent = new Intent(this, Clerk_SortingListActivity.class);
        intent.putExtra("Department", department);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if(resultCode == RESULT_OK){
                this.onCreate(null);
            }
        }
    }


}
