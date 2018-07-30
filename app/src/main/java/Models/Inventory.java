package Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utilities.Constants;
import Utilities.JSONParser;

public class Inventory extends HashMap<String, String> {
    public Inventory(String item_number, String description, String category, String unit_of_measurement, String current_quantity, String reorder_level, String reorder_quantity, String item_bin, String item_status, String pending_adj_remove){
        put("item_number", item_number);
        put("description", description);
        put("category", category);
        put("unit_of_measurement", unit_of_measurement);
        put("current_quantity", current_quantity);
        put("reorder_level", reorder_level);
        put("reorder_quantity", reorder_quantity);
        put("item_bin", item_bin);
        put("item_status", item_status);
        put("pending_adj_remove", pending_adj_remove);
    }

    public Inventory(String item_number, String description){
        put("item_number", item_number);
        put("description", description);
    }

    public Inventory(){

    }

    public static List<Inventory> GetActiveInventories(String token){
        List<Inventory> list = new ArrayList();
        JSONArray a = JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST+"/Inventory/Active/"+token);
        try {
            for (int i =0; i<a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new Inventory(
                        b.getString("item_number"),
                        b.getString("description"),
                        b.getString("category"),
                        b.getString("unit_of_measurement"),
                        b.getString("current_quantity"),
                        b.getString("reorder_level"),
                        b.getString("reorder_quantity"),
                        b.getString("item_bin"),
                        b.getString("item_status"),
                        b.getString("pending_adj_remove")
                ));
            }
        } catch (Exception e) {
            Log.e("Inventory", "JSONArray error");
            e.printStackTrace();
        }
        return(list);
    }

    public static List<Inventory> GetInventorySearchResult(String search, String token){
        List<Inventory> list = new ArrayList();
        JSONArray a =JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST+"/Inventory/"+search+"/"+token);
        try {
            for (int i =0; i<a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new Inventory(
                        b.getString("item_number"),
                        b.getString("description"),
                        b.getString("category"),
                        b.getString("unit_of_measurement"),
                        b.getString("current_quantity"),
                        b.getString("reorder_level"),
                        b.getString("reorder_quantity"),
                        b.getString("item_bin"),
                        b.getString("item_status"),
                        b.getString("pending_adj_remove")
                ));
            }
        } catch (Exception e) {
            Log.e("Inventory", "JSONArray error");
            e.printStackTrace();
        }
        return(list);
    }

    public static Inventory GetInventoryByItemCode(String itemcode, String token){
        Inventory item= new Inventory();
        JSONObject a =JSONParser.getJSONFromUrl(Constants.SERVICE_HOST+"/Inventory/ItemCode/"+itemcode+"/"+token);
        try {
            item =new Inventory(
                    a.getString("item_number"),
                    a.getString("description"),
                    a.getString("category"),
                    a.getString("unit_of_measurement"),
                    a.getString("current_quantity"),
                    a.getString("reorder_level"),
                    a.getString("reorder_quantity"),
                    a.getString("item_bin"),
                    a.getString("item_status"),
                    a.getString("pending_adj_remove")
            );
        } catch (Exception e) {
            Log.e("Inventory", "JSONArray error");
            e.printStackTrace();
        }
        return(item);
    }
}
