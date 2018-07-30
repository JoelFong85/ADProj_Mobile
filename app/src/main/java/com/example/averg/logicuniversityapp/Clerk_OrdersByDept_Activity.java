package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import Utilities.Constants;
import Utilities.JSONParser;

public class Clerk_OrdersByDept_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk__orders_by_dept_);

        // Populate the list view.
        ArrayList<String> departments = new ArrayList<>();
        departments.add("English");
        departments.add("Computer Science");
        departments.add("Commerce");

        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, departments);
        ListView listView = findViewById(R.id.departmentListView);
        listView.setAdapter(adapter);

    }

    // An asynchronous task that logs the user in.
    private class LoginTask extends AsyncTask<Void, Void, JSONObject> {
        protected JSONObject doInBackground(Void... params) {
            // return JSONParser.getJSONFromUrl(Constants.SERVICE_HOST + "/Login/" + usernameEditText.getText() + "/" + passwordEditText.getText());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

        }
    }


}
