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

    public static List<Inventory> GetActiveInventories(){
        List<Inventory> list = new ArrayList();
        JSONArray a = JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST+"/Inventory/Active/"+Constants.TOKEN);
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

    public static List<Inventory> GetInventorySearchResult(String search){
        List<Inventory> list = new ArrayList();
        JSONArray a =JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST+"/Inventory/"+search+"/"+Constants.TOKEN);
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

    public static Inventory GetInventoryByItemCode(String itemcode){
        Inventory item= new Inventory();
        JSONObject a =JSONParser.getJSONFromUrl(Constants.SERVICE_HOST+"/Inventory/ItemCode/"+itemcode+"/"+Constants.TOKEN);
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

    //Tharrani Start

    private static List<Inventory> inventoryList = new ArrayList<Inventory>();

    public static List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public  Inventory(String item, String desc, String category, String UOM, String cq, String reol, String req, String bin, String status)
    {
        put("item_number", item);
        put("description",desc);
        put("category", category);
        put("unit_of_measurement", UOM);
        put("current_quantity", cq);
        put("reorder_level", reol);
        put("reorder_quantity", req);
        put("item_bin", bin);
        put("item_status", status);
    }
    public Inventory(String item, String desc, String category,String UOM )
    {
        put("item_number", item );
        put("description",desc);
        put("category", category);
        put("unit_of_measurement", UOM);
    }

    public static List<Inventory> list()
    {
        List<Inventory> list = new ArrayList<Inventory>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST + "/NewRequest/AllItems/" + Constants.TOKEN);
        try {
            for (int i =0; i<a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new Inventory(b.getString("item_number"), b.getString("description"),b.getString("category"),b.getString("unit_of_measurement")));
            }
        } catch (Exception e) {
            Log.e("Inventory.list()", "JSONArray error");
        }
        inventoryList = list;
        return list;
    }

    public static Inventory getinventory(String id)
    {
        JSONObject a = JSONParser.getJSONFromUrl(Constants.SERVICE_HOST + "/NewRequest/Items/" + id + "/"+Constants.TOKEN );
        try {
            return new Inventory(a.getString("item_number"), a.getString("description"), a.getString("category"), a.getString("unit_of_measurement"));
        }
        catch (Exception e) {
            Log.e("iventory.getinventory()", "JSONArray error");
        }
        return(null);
    }

    public static List<Inventory> getsearchlist(String s)
    {
        List<Inventory> searchlist = new ArrayList<Inventory>();
        for(int i=0; i<inventoryList.size();i++)
        {
            if(getInventoryList().get(i).get("description").toLowerCase().contains(s.toLowerCase()))
            {
                searchlist.add(getInventoryList().get(i));
            }
        }
        return searchlist;
    }

    //Tharrani End
}
