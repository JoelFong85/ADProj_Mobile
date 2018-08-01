package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import Utilities.Constants;
import Utilities.JSONParser;

public class Departmenthead extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departmenthead);
        Button b=(Button)findViewById(R.id.button6);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Departmenthead.this,pendingrequests.class);
                startActivity(i);
            }
        });
        Button b1=(Button)findViewById(R.id.button7);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Departmenthead.this,Budget.class);
                startActivity(i);
            }
        });
        Button b2 = (Button)findViewById(R.id.button8);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LogoutTask(Departmenthead.this).execute();
            }
        });
    }

    private class LogoutTask extends AsyncTask<String, Void, JSONObject> {

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
