package com.example.joel.adprojjoel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.joel.adprojjoel.MainActivity.token;

public class ReallocateItem extends HashMap<String, String> {

    final static String ipAdd = "http://172.23.200.89/LogicUniversity/Services/androidService.svc/";

    final static String getReallocateListURL = "Department/Sorting/Reallocate/";
    final static String resetRODTableURL = "Department/Sorting/Reallocate/ResetROD";
    final static String updateRODTableURL = "Department/Sorting/Reallocate/UpdateROD";
    final static String returnToInventoryURL = "Department/Sorting/Reallocate/ReturnInventory/";

    private static List<ReallocateItem> riList = new ArrayList<ReallocateItem>();

    public static List<ReallocateItem> getReallocateList() {
        return riList;
    }

    public ReallocateItem(String collectedQty, String departmentID, String description, String itemNumber, String quantityOrdered, String newCollectedQty) {
        put("CollectedQty", collectedQty);
        put("DepartmentID", departmentID);
        put("Description", description);
        put("ItemNumber", itemNumber);
        put("QuantityOrdered", quantityOrdered);
        put("NewCollectedQty", newCollectedQty);
    }

    // For Sorting List - get sorting list by department
    public static List<ReallocateItem> getReallocateListByItemNum(String itemNum) {
        List<ReallocateItem> cList = new ArrayList<ReallocateItem>();

        JSONArray a = JSONParser.getJSONArrayFromUrl(ipAdd + getReallocateListURL + itemNum  + "/" + token);

        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject o = a.getJSONObject(i);
                String itemNo = o.getString("ItemNumber");
                String description = o.getString("Description");
                String quantityOrdered = o.getString("QuantityOrdered");
                String collectedQty = o.getString("CollectedQty");
                String dptId = o.getString("DepartmentID");
                cList.add(new ReallocateItem(collectedQty, dptId, description, itemNo, quantityOrdered, collectedQty));
            }

            riList = cList;

            return cList;


        } catch (Exception e) {
            Log.e("Reallocate.List()", "JSONArray error");
        }
        return (null);
    }

    public static void updateReallocation(List<ReallocateItem> ri) {
        int oldCollect = 0;
        int newCollect = 0;

        for (ReallocateItem r : ri) {

            JSONObject cItem = new JSONObject();
            try {
                cItem.put("ItemNumber", r.get("ItemNumber"));
                cItem.put("Description", r.get("Description").toString().trim());
                cItem.put("QuantityOrdered", Integer.parseInt(r.get("QuantityOrdered")));
                cItem.put("CollectedQty", Integer.parseInt(r.get("NewCollectedQty").toString().trim()));
                cItem.put("PendingQty", 0);
                cItem.put("DepartmentID", r.get("DepartmentID").toString().trim());
                cItem.put("Token", token);
            } catch (Exception e) {
                Log.e("Reallocate.update", "JSONArray error");
            }

            String ResetRODTable = JSONParser.postStream(ipAdd + resetRODTableURL, cItem.toString());

            String UpdateRODTable = JSONParser.postStream(ipAdd + updateRODTableURL, cItem.toString());

            oldCollect += Integer.parseInt(r.get("CollectedQty").toString().trim());
            newCollect += Integer.parseInt(r.get("NewCollectedQty").toString().trim());
        }

        String itemNum = ri.get(0).get("ItemNumber");
        int balance = oldCollect - newCollect;
        if (balance != 0) {
            String returnToInventory = JSONParser.getStream(ipAdd + returnToInventoryURL + "/" + balance + "/" + itemNum  + "/" + token);
        }

    }


}
