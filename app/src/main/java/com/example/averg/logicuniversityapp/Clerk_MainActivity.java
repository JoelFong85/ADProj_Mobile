package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import Adapters.ROLinkedListAdapter;
import Models.RequisitionOrder;
import Utilities.Constants;
import Utilities.JSONParser;

public class Clerk_MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk__main);

        // Attach listeners to each button that links to their respective intent

        Button roSearchMenuButton = findViewById(R.id.requisitionOrderSearchMenuButton);
        roSearchMenuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Clerk_RequisitionOrderSearchActivity.class);
                startActivity(i);
            }
        });

        Button weeklyCollectionListMenuButton = findViewById(R.id.WeeklyCollectionListMenuButton);
        weeklyCollectionListMenuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Clerk_WeeklyCollectionListActivity.class);
                startActivity(i);
            }
        });

        Button ordersByDeptLinkButton = findViewById(R.id.OrderByDeptLinkButton);
        ordersByDeptLinkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Clerk_OrdersByDept_Activity.class);
                startActivity(i);
            }
        });

        // Logout button logs the user out
        Button logoutButton = findViewById(R.id.LogoutMenuButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new LogoutTask(Clerk_MainActivity.this).execute();
            }
        });


    }

    private class LogoutTask extends AsyncTask<String, Void, JSONObject>{

        private final WeakReference<Activity> weakActivity;

        LogoutTask(Activity myActivity) {
            this.weakActivity = new WeakReference<>(myActivity);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
            return JSONParser.getJSONFromUrl(Constants.SERVICE_HOST + "/Logout/" + pref.getString(Constants.PREFERENCE_TOKEN, "Token retrieval failed"));
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            // Clear the token
            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, getApplicationContext().MODE_PRIVATE).edit();
            editor.remove(Constants.PREFERENCE_TOKEN);
            editor.commit();

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }

}
