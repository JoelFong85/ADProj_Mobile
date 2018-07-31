package Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utilities.Constants;
import Utilities.JSONParser;

public class collectionhistory extends HashMap<String,String> {
    public collectionhistory(String collectionDate,String collectionplace) {
        put("collectionDate", collectionDate);
        put("collectionplace",collectionplace);

    }
    public collectionhistory(){

    }
    public static List<collectionhistory> jread(){
        List<collectionhistory> list = new ArrayList<collectionhistory>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST+"/viewcollectionhistory/"+Constants.TOKEN);
        try {
            for (int i =0; i<a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new collectionhistory(b.getString("collectionDate"), b.getString("collectionplace")
                ));
            }
        } catch (Exception e) {
            Log.e("collectionhistory", "collectionhistoryerror");
        }
        return(list);
    }
}

