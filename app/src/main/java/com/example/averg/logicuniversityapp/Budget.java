package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import Models.budgetbydepartment;

public class Budget extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        new AsyncTask<Void, Void, budgetbydepartment>() {
            @Override
            protected budgetbydepartment doInBackground(Void... params) {
                return budgetbydepartment.jread();
            }

            @Override
            protected void onPostExecute(budgetbydepartment result) {
                TextView t1 = (TextView) findViewById(R.id.textView18);
                t1.setText(result.get("budgetallocated"));
                TextView t2 = (TextView) findViewById(R.id.textView19);
                t2.setText(result.get("budgetspent"));
            }
        }.execute();

        new AsyncTask<Void, Void, budgetbydepartment>() {
            @Override
            protected budgetbydepartment doInBackground(Void... params) {
                return budgetbydepartment.jread();
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
                yvalues.add(new PieEntry(Float.parseFloat(result.get("budgetallocated"))-Float.parseFloat(result.get("budgetspent")),"Remaining Budget"));
                yvalues.add(new PieEntry(Float.parseFloat(result.get("budgetspent")),"Approved Budget"));

                PieDataSet dataSet=new PieDataSet(yvalues,"Budget Usage");
                dataSet.setSliceSpace(3f);
                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                PieData data=new PieData((dataSet));
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.BLACK);
                pieChart.setData(data);
            }
        }.execute();






    }


}

