package Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utilities.Constants;
import Utilities.JSONParser;

public class EmployeeRequisitionOrderDetail extends HashMap<String,String> {

    public EmployeeRequisitionOrderDetail(){}

    public EmployeeRequisitionOrderDetail(String category, String description, String quantity, String unit_of_measurement)
    {
        put("category", category);
        put("description", description);
        put("quantity", quantity);
        put("unit_of_measurement", unit_of_measurement);
    }

    public static List<EmployeeRequisitionOrderDetail> ShowOrderDetail(String id)
    {
        List<EmployeeRequisitionOrderDetail> list = new ArrayList<EmployeeRequisitionOrderDetail>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST+"/NewRequest/Confirm/orderdetail?id="+id.trim()+"&token="+Constants.TOKEN);
        Log.i("JSONARRAY", a.toString());
        try {
            for (int i =0; i<a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                list.add(new EmployeeRequisitionOrderDetail(b.getString("category").trim(), b.getString("description").trim(),b.getString("quantity").trim(),b.getString("unit_of_measurement").trim()));
            }
        } catch (Exception e) {
            Log.e("orderdetaillist", "JSONArray error");
        }
        return list;
    }
}
