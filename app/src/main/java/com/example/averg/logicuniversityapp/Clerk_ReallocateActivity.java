package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Adapters.ReallocateListAdapter;
import Models.ReallocateItem;

public class Clerk_ReallocateActivity extends Activity implements AdapterView.OnItemClickListener {

    ListView listView;
    int counter;
    ReallocateListAdapter adapter;
    List<ReallocateItem> reallocateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk__reallocate);
        Intent i = getIntent();
        final String itemNum = i.getStringExtra("ItemNumber");
        final String description = i.getStringExtra("Description");

        TextView title = findViewById(R.id.textView_ReallocateListTitle);
        title.setText("Item " + itemNum + " Reallocation");

        TextView descrip = findViewById(R.id.textView_ReallocateListDescription);
        descrip.setText(description);

        showCollectQty(itemNum);
        showList(itemNum);

        Button btnReallocate = (Button) findViewById(R.id.button_Reallocate);
        btnReallocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reallocateList = ReallocateItem.getReallocateList();

                int totalNewAllocation = 0;
                for (ReallocateItem ri : reallocateList) {
                    totalNewAllocation += Integer.parseInt(ri.get("NewCollectedQty"));
                }

                TextView count = (TextView) findViewById(R.id.textView_ReallocateListTotalCollectQty);
                int totalCount = Integer.parseInt(count.getText().toString());

                if (totalNewAllocation > totalCount) {
                    Toast.makeText(Clerk_ReallocateActivity.this, getString(R.string.reallocateTooMuch), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (totalNewAllocation < totalCount) {
                    //makeAlertDialog();

                    new AlertDialog.Builder(Clerk_ReallocateActivity.this)
                            .setTitle("Return to warehouse?")
                            .setMessage("You entered less items than collected. Do you want to return items to warehouse/inventory?")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new AsyncTask<List<ReallocateItem>, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(List<ReallocateItem>... params) {
                                            ReallocateItem.updateReallocation(params[0]);
                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void result) {
                                            Intent intent = new Intent();
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    }.execute(reallocateList);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                if (totalNewAllocation == totalCount) {
                    new AsyncTask<List<ReallocateItem>, Void, Void>() {
                        @Override
                        protected Void doInBackground(List<ReallocateItem>... params) {
                            ReallocateItem.updateReallocation(params[0]);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }.execute(reallocateList);
                }

            }
        });

    }

    public void showList(String itemNum) {
        new AsyncTask<String, Void, List<ReallocateItem>>() {
            @Override
            protected List<ReallocateItem> doInBackground(String... params) {
                reallocateList = ReallocateItem.getReallocateListByItemNum(params[0]);
                return reallocateList;
            }

            @Override
            protected void onPostExecute(List<ReallocateItem> result) {

                adapter = new ReallocateListAdapter(Clerk_ReallocateActivity.this, R.layout.activity_clerk_reallocate_list_row, result);
                listView = findViewById(R.id.listView_ReallocateList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(Clerk_ReallocateActivity.this);
            }
        }.execute(itemNum);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final ReallocateItem ri = (ReallocateItem) parent.getItemAtPosition(position);
//        int orderQty = Integer.parseInt(ri.get("QuantityOrdered"));
//        final EditText et = (EditText) view.findViewById(R.id.editText_ReallocateList_DistriQty);
        //et.setFilters(new InputFilter[]{new InputFilterMinMax(0, orderQty)});
//        et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
////                ReallocateActivity.updateCollectQty(ri.get("DepartmentID"), et.getText().toString());
////                Log.i("onItemClick", et.getText().toString());
//            }
//        });
    }

    public void showCollectQty(String itemNum) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                List<ReallocateItem> riList = ReallocateItem.getReallocateListByItemNum(params[0]);

                counter = 0;
                for (ReallocateItem i : riList) {
                    counter += Integer.parseInt(i.get("CollectedQty"));
                }

                String counterText = Integer.toString(counter);

                return counterText;
            }

            @Override
            protected void onPostExecute(String result) {

                TextView collectedQty = findViewById(R.id.textView_ReallocateListTotalCollectQty);
                collectedQty.setText(result);

            }
        }.execute(itemNum);
    }

    public void makeAlertDialog() {


    }


}
