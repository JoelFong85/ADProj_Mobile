package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import Utilities.Constants;
import Utilities.JSONParser;

public class DebugActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        DebugTask debug = new DebugTask();
        debug.execute();

    }


    private class DebugTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("DepartmentId", "ENGL");
                jsonObject.put("EmployeeName", "Teh Store Man");
                TextView debugTextView = findViewById(R.id.debugTextView);
                debugTextView.setText(jsonObject.toString());
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            return JSONParser.postStream(Constants.SERVICE_HOST + "/Debug/Post", jsonObject.toString());

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                // TextView debugTextView = findViewById(R.id.debugTextView);
                Toast t = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                t.show();
                // debugTextView.setText(result);

            } catch (Exception ex)
            {
                Toast t = Toast.makeText(getApplicationContext(), "die", Toast.LENGTH_SHORT);
                t.show();
                ex.printStackTrace();
            } finally

            {

            }
        }
    }
}
