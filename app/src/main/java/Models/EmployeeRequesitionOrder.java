package Models;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import Utilities.Constants;
import Utilities.JSONParser;

public class EmployeeRequesitionOrder extends HashMap<String,String> {

    //Tharrani Udhayasekar

    public EmployeeRequesitionOrder(){

    }

    public EmployeeRequesitionOrder(String RequisitionId, String RequisitionDate, String RequisitionStatus)
    {
        put("RequisitionId", RequisitionId);
        put("RequisitionDate", RequisitionDate);
        put("RequisitionStatus", RequisitionStatus);
    }

    public static EmployeeRequesitionOrder ShowOrder(String id)
    {
        JSONObject a = JSONParser.getJSONFromUrl(Constants.SERVICE_HOST+"/NewRequest/Confirm?id="+id.trim()+"&token="+Constants.TOKEN);
        try {
            return new EmployeeRequesitionOrder(a.getString("RequisitionId"), a.getString("RequisitionDate"), a.getString("RequisitionStatus"));
            }
        catch (Exception e) {
            Log.e("requestorder", "JSONArray error");
        }
        return(null);
    }
}
