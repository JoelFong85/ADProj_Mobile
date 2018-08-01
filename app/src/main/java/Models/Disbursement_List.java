package Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utilities.Constants;
import Utilities.JSONParser;

public class Disbursement_List extends HashMap<String, String> {

    public static List<Disbursement_List> getDL() {
        return DL;
    }

    public String getCollection_date() {
        return collection_date;
    }

    public String getCollection_id() {
        return collection_id;
    }

    private static List<Disbursement_List> DL = new ArrayList<Disbursement_List>();
    private String collection_date;
    private String collection_location;
    private String collection_time;
    private String department_name;
    private String representative_name;
    private String collection_id;
    private String department_pin;

    public Disbursement_List(){}

    public Disbursement_List(String collection_date, String collection_location, String collection_time, String department_name, String representative_name, String collection_id, String department_pin){

        put("collection_date", collection_date);
        put("collection_location",collection_location);
        put("collection_time",collection_time);
        put("department_name",department_name);
        put("representative_name",representative_name);
        put("collection_id", collection_id);
        put("department_pin", department_pin);
    }

    public static List<Disbursement_List> listall()
    {
        clearlist();
        JSONArray a = JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST + "/Disbursement/Alllist/" + Constants.TOKEN);
        try {
            for (int i =0; i<a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                DL.add(new Disbursement_List(b.getString("collection_date").trim(), b.getString("collection_location").trim(),b.getString("collection_time").trim(),b.getString("department_name").trim(), b.getString("representative_name").trim(),b.getString("collection_id").trim(), b.getString("department_pin").trim()));
                Log.i("time", b.get("collection_time").toString());
            }
        } catch (Exception e) {
            Log.e("Inventory.list()", "JSONArray error");
        }
        return DL;
//        Collections.sort(DL, new CustomeComparator());
    }

    public static List<Disbursement_List> selectdate(String date)
    {
        List<Disbursement_List> mdate = new ArrayList<Disbursement_List>();
        for(int i=0;i<DL.size();i++)
            {
                Log.i("collection_date", DL.get(i).get("collection_date"));
                Log.i("date", date);

                if(DL.get(i).get("collection_date").equals(date))
                {
                    Log.i("loop", "true");
                    mdate.add(DL.get(i));
                    Log.i("DLloop", mdate.toString());
                }
            }
            Log.i("madate", mdate.toString());
        return mdate;
    }

    public static Disbursement_List selectid(String id)
    {
        for(int i=0;i<DL.size();i++)
        {
            if(DL.get(i).get("collection_id").trim().equals(id.trim()))
            {
                return DL.get(i);
            }
        }
        return null;
    }

    public static boolean verifypin(int dep_pin, int pin)
    {
        if(dep_pin==pin)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void clearlist()
    {
        DL.clear();
    }
}
