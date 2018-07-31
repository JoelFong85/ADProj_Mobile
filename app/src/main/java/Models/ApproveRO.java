package Models;import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utilities.Constants;
import Utilities.JSONParser;

public class ApproveRO extends HashMap<String,String> {
    public ApproveRO(String name, String requisition_Date, String requisition_id, String status, String sum) {
        put("name", name);
        put("requisition_Date", requisition_Date);
        put("requisition_id", requisition_id);
        put("status", status);
        put("sum", sum);
    }

    public ApproveRO() {

    }

    public static List<ApproveRO> jread() {
        List<ApproveRO> list = new ArrayList<ApproveRO>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST+"/pending/"+Constants.TOKEN);
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new ApproveRO(b.getString("name"), b.getString("requisition_Date"),
                        b.getString("requisition_id"), b.getString("status"), b.getString("sum")));
            }
        } catch (Exception e) {
            Log.e("ApproveRO", "JSONArray error");
        }
        return (list);
    }

    public static ApproveRO getRO(String roid) {
        JSONObject b = JSONParser.getJSONFromUrl(Constants.SERVICE_HOST+"/pending/"+Constants.TOKEN+"?id=" + roid);
        try {
            return new ApproveRO(b.getString("name"), b.getString("requisition_Date"),
                    b.getString("requisition_id"), b.getString("status"), b.getString("sum"));
        } catch (Exception e) {
            Log.e("Approvero()", "JSONArray error");
        }
        return (null);
    }

    public static void updateROwithapprove(ApproveRO aprq) throws UnsupportedEncodingException {
        JSONObject updatero = new JSONObject();
        try {
            updatero.put("name", aprq.get("name"));
            updatero.put("requisition_Date", aprq.get("requisition_Date"));
            updatero.put("requisition_id", aprq.get("requisition_id"));
            updatero.put("status", aprq.get("status"));
            updatero.put("sum", aprq.get("sum"));
        } catch (Exception e) {
            Log.e("JSON object error", "JSONArray error");
        }
        String result = JSONParser.postStream(Constants.SERVICE_HOST+"/pending/", updatero.toString());
    }

    public static void updateROwithreject(ApproveRO aprq1) throws UnsupportedEncodingException {
        JSONObject updatero = new JSONObject();
        try {
            updatero.put("name", aprq1.get("name"));
            updatero.put("requisition_Date", aprq1.get("requisition_Date"));
            updatero.put("requisition_id", aprq1.get("requisition_id"));
            updatero.put("status", aprq1.get("status"));
            updatero.put("sum", aprq1.get("sum"));
        } catch (Exception e) {
            Log.e("JSON object error", "JSONArray error");
        }
        String result = JSONParser.postStream(Constants.SERVICE_HOST+"/pending", updatero.toString());
    }
}
