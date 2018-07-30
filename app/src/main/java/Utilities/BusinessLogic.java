package Utilities;

import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.example.averg.logicuniversityapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import Adapters.ROLinkedListAdapter;
import Models.Inventory;
import Models.RequisitionOrder;

public class BusinessLogic {
/*
    public static Inventory getInventoryByItemNumber(String itemNumber) {


        new AsyncTask<String, Void, JSONObject>() {
            protected JSONObject doInBackground(String... params) {
                return JSONParser.getJSONFromUrl(Constants.SERVICE_HOST + "/Inventory/=" + params[0]);
            }

            @Override
            protected Inventory onPostExecute(JSONObject result) {
                try {
                    return new Inventory(result.getString("item_number"), result.getString("description"), result.getString("category"), result.getString("unit_of_measurement"), result.getInt("current_quantity"), result.getInt("reorder_level"), result.getInt("reorder_quantity"), result.getInt("item_bin"), result.getString("item_status"), 0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }.execute(itemNumber);
    }

    return null;
    */
}
