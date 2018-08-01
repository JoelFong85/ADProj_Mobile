package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import Adapters.SortingListAdapter;
import Models.CollectionItem;
import Models.Department;
import Models.DepartmentSorter;

public class Clerk_SortingListActivity extends Activity implements AdapterView.OnItemClickListener {

    ListView listView;
    String dptID;

    Calendar c;
    DatePickerDialog dpd;
    EditText selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk__sorting_list);
        Intent i = getIntent();
        final String selectedDptName = i.getStringExtra("Department");

        TextView title = findViewById(R.id.textView_SortingListTitle);
        title.setText(selectedDptName + " Orders");

        showList(selectedDptName);
        loadDatePicker();

        Button btnReadyForCollection = findViewById(R.id.button_ReadyForCollection_Sorting);
        btnReadyForCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate.getText().toString().matches("")) {
                    Toast.makeText(Clerk_SortingListActivity.this, getString(R.string.selectDate), Toast.LENGTH_SHORT).show();
                    return;
                }

                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        dptID = DepartmentSorter.getDptIdFromDptName(selectedDptName);
                        DepartmentSorter.readyForCollection(dptID, selectedDate.getText().toString());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }.execute();

            }
        });
    }

    public void showList(String selectedDptName) {
        new AsyncTask<String, Void, List<CollectionItem>>() {
            @Override
            protected List<CollectionItem> doInBackground(String... params) {

                return CollectionItem.getSortingListByDepartment(params[0]);
            }

            @Override
            protected void onPostExecute(List<CollectionItem> result) {
                if (result.isEmpty()) {
                    Toast.makeText(Clerk_SortingListActivity.this, getString(R.string.noSortForThisDpt), Toast.LENGTH_LONG).show();
                }
                SortingListAdapter adapter = new SortingListAdapter(Clerk_SortingListActivity.this, R.layout.activity_clerk__sorting_list_row, result);
                listView = findViewById(R.id.listView_SortingList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(Clerk_SortingListActivity.this);
            }
        }.execute(selectedDptName);
    }

    public void onItemClick(AdapterView<?> av, View v,
                            int position, long id) {

        CollectionItem ci = (CollectionItem) av.getAdapter().getItem(position);

        String itemNum = ci.get("ItemNumber");
        String description = ci.get("Description");

        Intent intent = new Intent(this, Clerk_ReallocateActivity.class);
        intent.putExtra("ItemNumber", itemNum);
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

    protected void loadDatePicker() {
        selectedDate = findViewById(R.id.textView_SortingList_selectedDate);
        Button btnSelectDate = findViewById(R.id.button_ReadyForCollection_selectDate);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(Clerk_SortingListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {

                        int month = mMonth + 1;
                        String formattedMonth = "" + month;
                        String formattedDayOfMonth = "" + mDay;

                        if (month < 10) {

                            formattedMonth = "0" + month;
                        }
                        if (mDay < 10) {

                            formattedDayOfMonth = "0" + mDay;
                        }
                        selectedDate.setText(formattedDayOfMonth + "-" + formattedMonth + "-" + mYear);
                    }
                }, year, month, day);

                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });
    }
}
