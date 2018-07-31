package Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utilities.Constants;
import Utilities.JSONParser;

public class requisitionitemdetails extends HashMap<String,String> {
    public requisitionitemdetails(String description,String noofitems) {
        put("description", description);
        put("noofitems",noofitems);

    }
    public requisitionitemdetails(){

    }
    public static List<requisitionitemdetails> jread(String roid){
        List<requisitionitemdetails> list = new ArrayList<requisitionitemdetails>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST+"/getitemdetails/"+Constants.TOKEN+"?id="+roid);
        try {
            for (int i =0; i<a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new requisitionitemdetails(b.getString("description"), b.getString("noofitems")
                ));
            }
        } catch (Exception e) {
            Log.e("itemdetails", "itemdetailsarray");
        }
        return(list);
    }
}
