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

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        setContentView(R.layout.activity_main);

        // Setup onclicklistener for the login button
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setText("Please wait...");
                loginButton.setEnabled(false);
                LoginTask loginTask = new LoginTask();
                loginTask.execute();
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
                loginButton.setEnabled(true);
                loginButton.setText("Login");
                // Login is successful
                String printString = result.getString("Token");

                // Save token in Constants
                Constants.TOKEN = result.getString("Token");

                // Redirect user to appropriate activity
                String role = result.getString("Role");

                if(role.equals(Constants.ROLES_STORE_CLERK) || role.equals(Constants.ROLES_STORE_MANAGER) || role.equals(Constants.ROLES_STORE_SUPERVISOR)) {
                    Intent i = new Intent(getApplicationContext(), Clerk_MainActivity.class);
                    startActivity(i);
                }

                else if(role.equals(Constants.ROLES_EMPLOYEE)) {
                    Intent i = new Intent(getApplicationContext(), User_MainActivity.class);
                    startActivity(i);
                }
                else if(role.equals(Constants.ROLES_DEPARTMENT_REPRESENTATIVE))
                {
                    Intent i = new Intent(getApplicationContext(), DepartmentRep.class);
                    startActivity(i);
                }
                else if(role.equals(Constants.ROLES_DEPARTMENT_HEAD))
                {
                    Intent i = new Intent(getApplicationContext(), Departmenthead.class);
                    startActivity(i);
                }

            } catch (Exception ex) {
                // Login failed
                Toast t = Toast.makeText(getApplicationContext(), "Login failed. Please try again", Toast.LENGTH_SHORT);
                t.show();
                ex.printStackTrace();
            }
        }
    }
}
