package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import Models.budgetbydepartment;
import Utilities.Constants;
import Utilities.JSONParser;

public class Budget extends Activity {

//sruthi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        registerForContextMenu(findViewById(R.id.budgetactivitiy));
        new AsyncTask<Void, Void, budgetbydepartment>() {
            @Override
            protected budgetbydepartment doInBackground(Void... params) {
                //to get the budget for the department for the particular month
                return budgetbydepartment.jread();
            }

            @Override
            protected void onPostExecute(budgetbydepartment result) {
                TextView t1 = (TextView) findViewById(R.id.textView18);
                t1.setText(result.get("budgetallocated"));//to get the budget allocated and assign it to the text view
                TextView t2 = (TextView) findViewById(R.id.textView19);
                t2.setText(result.get("budgetspent")); // to get the budget spend and assign it to the text view
            }
        }.execute();

        new AsyncTask<Void, Void, budgetbydepartment>() {
            @Override
            protected budgetbydepartment doInBackground(Void... params) {
                return budgetbydepartment.jread(); //to get the budget for the department for the particular month
            }

            @Override
            protected void onPostExecute(budgetbydepartment result) {

                PieChart pieChart;
                pieChart=(PieChart) findViewById(R.id.piechart1);
                pieChart.setUsePercentValues(true);
                pieChart.getDescription().setEnabled(false);
                pieChart.setExtraOffsets(5,10,5,5);
                pieChart.setDragDecelerationFrictionCoef(0.95f);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleColor(Color.WHITE);
                pieChart.setTransparentCircleRadius(61f);

                ArrayList<PieEntry> yvalues=new ArrayList<PieEntry>();
                Float a=Float.parseFloat(result.get("budgetallocated"))-Float.parseFloat(result.get("budgetspent"));
                if(a>0) {//assigning the budget values to the arraylist to pass on to the pie chart
                    yvalues.add(new PieEntry(Float.parseFloat(result.get("budgetallocated")) - Float.parseFloat(result.get("budgetspent")), "Remaining Budget"));
                    yvalues.add(new PieEntry(Float.parseFloat(result.get("budgetspent")), "Approved Budget"));
                }
                else
                {
                    yvalues.add(new PieEntry(Float.parseFloat(result.get("budgetspent")), "Approved Budget"));
                }
                //assigning the data to the pie chart
                PieDataSet dataSet=new PieDataSet(yvalues,"Budget Usage");
                dataSet.setSliceSpace(3f);
                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                pieChart.setEntryLabelColor(Color.BLACK);

                PieData data=new PieData((dataSet));
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.BLACK);
                pieChart.setData(data);
            }
        }.execute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //to display the logout functionality on right click
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                // do something
                new LogoutTask(Budget.this).execute();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private class LogoutTask extends AsyncTask<String, Void, JSONObject>{

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

