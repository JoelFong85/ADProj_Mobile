package Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import Utilities.Constants;
import Utilities.JSONParser;

import static android.content.Context.MODE_PRIVATE;

public class Adjustment extends HashMap<String, String> {
    public Adjustment(){

    }
    public Adjustment(String ItemNumber, String AdjustmentQty, String EmployeeRemark){
        put("ItemNumber",ItemNumber);
        put("AdjustmentQty",AdjustmentQty);
        put("EmployeeRemark",EmployeeRemark);
    }

    public static String CreateAdj(Adjustment adj, String token){
        JSONObject jadjustment = new JSONObject();

        try {
            jadjustment.put("ItemNumber", adj.get("ItemNumber"));
            jadjustment.put("AdjustmentQty", adj.get("AdjustmentQty"));
            jadjustment.put("EmployeeRemark", adj.get("EmployeeRemark"));
            String result = JSONParser.postStream(Constants.SERVICE_HOST+"/Adjustment/Create/"+token, jadjustment.toString());
            return result;
        } catch (Exception e) {
            Log.e("CreateAdjustment", "JSONArray error");
            e.printStackTrace();
        }
        return "Failed";
    }
}
