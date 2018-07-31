package Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utilities.Constants;
import Utilities.JSONParser;

public class budgetbydepartment extends HashMap<String,String> {
    public budgetbydepartment(String budgetallocated,String budgetspent) {
        put("budgetallocated", budgetallocated);
        put("budgetspent",budgetspent);

    }
    public budgetbydepartment(){
    }
    public static budgetbydepartment jread(){
        JSONObject b = JSONParser.getJSONFromUrl(Constants.SERVICE_HOST+"/getbudget/"+Constants.TOKEN);
        try {
            return (new budgetbydepartment(b.getString("budgetallocated"), b.getString("budgetspent")
            ));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return (null);
    }


}
