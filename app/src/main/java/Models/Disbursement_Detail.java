package Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utilities.Constants;
import Utilities.JSONParser;

import static Models.Disbursement_List.clearlist;

public class Disbursement_Detail extends HashMap<String, String>{

    private  static List<Disbursement_Detail> DLDetail = new ArrayList<Disbursement_Detail>();

    public static List<Disbursement_Detail> getDLDetail() {
        return DLDetail;
    }

    public Disbursement_Detail(){}

    private String collection_id;
    private String item_number;
    private String description;
    private String order_quantity;
    private String receive_quantity;
    private String altered_quantity;


    public void setAltered_quantity(String altered_quantity) {
        this.altered_quantity = altered_quantity;
    }


    public Disbursement_Detail(String collection_id, String item_number, String description, String order_quantity, String receive_quantity, String altered_quantity)
    {
        put("collection_id", collection_id);
        put("item_number", item_number);
        put("description", description);
        put("order_quantity",order_quantity);
        put("receive_quantity",receive_quantity);
        put("altered_quantity", altered_quantity);
    }

    public static List<Disbursement_Detail> getdetail(String id){
        List<Disbursement_Detail> list = new ArrayList<Disbursement_Detail>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST + "/Disbursement/Detail/" +id+"/"+ Constants.TOKEN);
        try {
            for (int i =0; i<a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new Disbursement_Detail(b.getString("collection_id").trim(), b.getString("item_number").trim(),b.getString("description").trim(),b.getString("order_quantity").trim(), b.getString("receive_quantity").trim(), b.getString("altered_quantity").trim()));
            }
        } catch (Exception e) {
            Log.e("DLDetail.list()", "JSONArray error");
        }
        DLDetail = list;
        return list;
    }

    public static void  updatesupplyquantity(String id, String q)
    {
        for(int i=0;i<DLDetail.size();i++)
        {
            if(DLDetail.get(i).get("item_number").trim()==id.trim())
            {
                Log.i("i", "entered loop");
                DLDetail.get(i).put("altered_quantity",q);
            }
        }
    }

    public static void acknowledgereceiptdetails(Disbursement_Detail DL, String collect_id)
    {
        JSONObject jdetail = new JSONObject();
        try {
            jdetail.put("collection_id", DL.get("collection_id"));
            jdetail.put("item_number", DL.get("item_number"));
            jdetail.put("receive_quantity", DL.get("receive_quantity"));
            jdetail.put("altered_quantity", DL.get("altered_quantity"));
            jdetail.put("token", Constants.TOKEN);
        }
        catch (Exception e) {
            Log.i("Acknowledge JSON object", "Error");
        }
        Log.i("req-detail", jdetail.toString());
        String re = JSONParser.postStream(Constants.SERVICE_HOST+"/Disbursement/Acknowledge", jdetail.toString());
    }

    public static boolean updatecollectionstatus (String collect_id)
    {
        try {
            JSONObject jdetail = new JSONObject();
            try {
                jdetail.put("collection_id", collect_id);
                jdetail.put("token", Constants.TOKEN);
            } catch (Exception e) {
                Log.i("ChangeStatJSON object", "Error");
            }
            Log.i("req-detail", jdetail.toString());
            String re = JSONParser.postStream(Constants.SERVICE_HOST + "/Disbursement/ChangeStatus", jdetail.toString());
            return true;
        }catch (Exception e){ return false;}
    }

}
