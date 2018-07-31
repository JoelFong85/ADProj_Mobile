package Models;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utilities.Constants;
import Utilities.JSONParser;

public class collectionpoint extends HashMap<String,String> {
    public collectionpoint(String collectionplace, String id) {
        put("collectionplace", collectionplace);
        put("id", id);
    }
    public static String[] Values;

    public collectionpoint() {
    }

    public static List<collectionpoint> jread() {
        List<collectionpoint> list = new ArrayList<collectionpoint>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST+"/changecollectionlocation/"+Constants.TOKEN);
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new collectionpoint(b.getString("collectionplace"), b.getString("id")));
            }
        } catch (Exception e) {
            Log.e("collectionpoint", "collection error");
        }
        return (list);

    }
    public static void updatecollection(collectionpoint c) throws UnsupportedEncodingException {
        JSONObject c2 = new JSONObject();
        try {
            c2.put("collectionplace", c.get("collectionplace"));
            c2.put("id", c.get("id"));
        } catch (Exception e) {
            Log.e("JSON object error", "JSONArray error");
        }
        String result= JSONParser.postStream(Constants.SERVICE_HOST+"/changecollectionlocation/"+Constants.TOKEN,c2.toString());

    }
}

