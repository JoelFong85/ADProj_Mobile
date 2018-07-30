package com.example.averg.logicuniversityapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;

import java.lang.ref.WeakReference;
import java.util.List;

import Adapters.AdjInventoryAdapter;
import Models.Inventory;
import Utilities.Constants;

public class ClerkAdjustmentInventoryActivity extends Activity implements SearchView.OnQueryTextListener{
    public static final String Tag="ClerkAdjInventory";
    List<Inventory> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_adjustment_inventory);
        //SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        //token= pref.getString(Constants.PREFERENCE_TOKEN, "Token retrieval failed");
        new GetInventoryList(this).execute();
        SearchView sv = findViewById(R.id.searchView2);
        sv.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if(s.trim().length()>0){
            new GetSearchResult(this).execute(s);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        new GetInventoryList(this).execute();
        return false;
    }

    private class GetInventoryList extends AsyncTask<Void, Void, List<Inventory>>{
        private WeakReference<Activity> weakActivity;

        public GetInventoryList(Activity myactivity) {
            this.weakActivity = new WeakReference<>(myactivity);
        }

        @Override
        protected List<Inventory> doInBackground(Void... params) {
            return Inventory.GetActiveInventories();
        }
        @Override
        protected void onPostExecute(List<Inventory> items) {
            list=items;
            initRecyclerView();
        }

    }
    private class GetSearchResult extends AsyncTask<String, Void, List<Inventory>> {
        private WeakReference<Activity> weakActivity;

        public GetSearchResult(Activity myactivity) {
            this.weakActivity = new WeakReference<>(myactivity);
        }

        @Override
        protected List<Inventory> doInBackground(String... params) {
            return Inventory.GetInventorySearchResult(params[0]);
        }

        @Override
        protected void onPostExecute(List<Inventory> result) {
            list=result;
            initRecyclerView();
        }
    }

    private void initRecyclerView(){
        Log.d(Tag,"initRecyclerView: init recyclerview.");
        android.support.v7.widget.RecyclerView rv = findViewById(R.id.recycler_view);
        AdjInventoryAdapter adapter = new AdjInventoryAdapter(this, list);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
