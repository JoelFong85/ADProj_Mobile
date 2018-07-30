package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import Utilities.Constants;
import Utilities.JSONParser;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        setContentView(R.layout.activity_main);

        // Setup onclicklistener for the login button
        final Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginTask loginTask = new LoginTask();
                loginTask.execute();
            }
        });

        final Button clerkButton = findViewById(R.id.ClerkButton);
        final Button employeeButton = findViewById(R.id.EmployeeButton);
        final Button debugButton = findViewById(R.id.debugButton);

        clerkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Clerk_MainActivity.class);
                startActivity(i);
            }
        });

        employeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), User_MainActivity.class);
                startActivity(i);
            }
        });

        debugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DebugActivity.class);
                startActivity(i);
            }
        });

    }

    // An asynchronous task that logs the user in.
    private class LoginTask extends AsyncTask<Void, Void, JSONObject> {
        protected JSONObject doInBackground(Void... params) {


            EditText usernameEditText = findViewById(R.id.usernameEditText);
            EditText passwordEditText = findViewById(R.id.passwordEditText);

            return JSONParser.getJSONFromUrl(Constants.SERVICE_HOST + "/Login/" + usernameEditText.getText() + "/" + passwordEditText.getText());

        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                // Login is successful
                String printString = result.getString("Token");
                Toast t = Toast.makeText(getApplicationContext(), "Greetings, fellow human being with token " + printString, Toast.LENGTH_LONG);
                t.show();

                // Save token in SharedPreference
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
                pref.edit().putString(Constants.PREFERENCE_TOKEN, result.getString("Token"));
                pref.edit().commit();


                // Redirect user to appropriate activity
                String role = result.getString("Role");

                if(role.equals(Constants.ROLES_STORE_CLERK) || role.equals(Constants.ROLES_STORE_MANAGER) || role.equals(Constants.ROLES_STORE_SUPERVISOR)) {
                    Intent i = new Intent(getApplicationContext(), Clerk_MainActivity.class);
                    startActivity(i);
                }

                else if(role.equals(Constants.ROLES_EMPLOYEE) || role.equals(Constants.ROLES_DEPARTMENT_REPRESENTATIVE) || role.equals(Constants.ROLES_DEPARTMENT_HEAD)) {
                    Intent i = new Intent(getApplicationContext(), User_MainActivity.class);
                    startActivity(i);
                }

            } catch (Exception ex) {
                // Login failed
                Toast t = Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT);
                t.show();
                ex.printStackTrace();
            }
        }
    }
}
