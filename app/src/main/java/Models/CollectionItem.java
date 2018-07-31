package com.example.joel.adprojjoel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.example.joel.adprojjoel.MainActivity.token;

public class CollectionItem extends HashMap<String, String> {

    final static String ipAdd = "http://172.23.200.89/LogicUniversity/Services/androidService.svc/";
    final static String getCollectionListURL = "WarehouseCollection/List";
    final static String sortCollectedGoodsURL = "WarehouseCollection/Sort";
    final static String deductFromInventoryURL = "WarehouseCollection/DeductInventory";

    final static String getSortingListByDepartmentURL = "Department/Sorting/List/";

    // Full constructor
    public CollectionItem(String collectedQty, String currentInventoryQty, String description, String itemNumber, String pendingQty, String quantityOrdered, String requisitionId, String unitOfMeasurement) {
        put("CollectedQty", collectedQty);
        put("CurrentInventoryQty", currentInventoryQty);
        put("Description", description);
        put("ItemNumber", itemNumber);
        put("PendingQty", pendingQty);
        put("QuantityOrdered", quantityOrdered);
        put("RequisitionId", requisitionId);
        put("UnitOfMeasurement", unitOfMeasurement);
    }

    // For Collection List
    public CollectionItem(String description, String itemNumber, String unitOfMeasurement, String currentInventoryQty, String collectedQty, String quantityOrdered) {
        put("Description", description);
        put("ItemNumber", itemNumber);
        put("UnitOfMeasurement", unitOfMeasurement);
        put("CurrentInventoryQty", currentInventoryQty);
        put("CollectedQty", collectedQty);
        put("QuantityOrdered", quantityOrdered);
    }

    //For Sorting List
    public CollectionItem(String itemNumber, String description, String quantityOrdered, String collectedQty, String pendingQty) {
        put("Description", description);
        put("ItemNumber", itemNumber);
        put("CollectedQty", collectedQty);
        put("QuantityOrdered", quantityOrdered);
        put("PendingQty", pendingQty);
    }

    public CollectionItem() {
    }

    //Collection List - Adding list of collection items from JSON svc to activity
    public static List<String> getCollectionList() {
        List<String> list = new ArrayList<String>();

        JSONArray a = JSONParser.getJSONArrayFromUrl(ipAdd + getCollectionListURL + "/" + token);
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject o = a.getJSONObject(i); // hard code?
                list.add(o.getString("Description"));
            }
        } catch (Exception e) {
            Log.e("CollectList.getList", "JSONArray error");
        }
        return (list);
    }

    // Collection List - get individual collection Item details
    public static CollectionItem getCollectionItemDetails(String description) {
        CollectionItem ci = new CollectionItem();
        JSONArray a = JSONParser.getJSONArrayFromUrl(ipAdd + getCollectionListURL + "/" + token);

        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject o = a.getJSONObject(i);
                String des = o.getString("Description");

                if (des.equals(description)) {
                    ci = new CollectionItem(o.getString("Description"), o.getString("ItemNumber"), o.getString("UnitOfMeasurement"), o.getString("CurrentInventoryQty"), o.getString("CollectedQty"), o.getString("QuantityOrdered"));
                    return ci;
                }
            }

        } catch (Exception e) {
            Log.e("CollectList.getItem()", "JSONArray error");
        }
        return (null);
    }

    // Collection List - update individual collection items
    public static void updateCollectionItem(CollectionItem ci) {
        JSONObject cItem = new JSONObject();
        try {
            cItem.put("Description", ci.get("Description"));
            cItem.put("ItemNumber", ci.get("ItemNumber"));
            cItem.put("UnitOfMeasurement", ci.get("UnitOfMeasurement"));
            cItem.put("CurrentInventoryQty", Integer.parseInt(ci.get("CurrentInventoryQty")));
            cItem.put("CollectedQty", Integer.parseInt(ci.get("CollectedQty")));
            cItem.put("QuantityOrdered", Integer.parseInt(ci.get("QuantityOrdered")));
            cItem.put("Token", token);
        } catch (Exception e) {
            Log.e("CollectList.updateItem", "JSONArray error");

        }

        // Sort Collected Good
        String sortCollectedGoods = JSONParser.postStream(ipAdd + sortCollectedGoodsURL, cItem.toString());

        // Update Inventory
        String deductFromInventory = JSONParser.postStream(ipAdd + deductFromInventoryURL, cItem.toString());
    }

    // For Sorting List - get sorting list by department
    public static List<CollectionItem> getSortingListByDepartment(String selectedDptName) {
        List<CollectionItem> cList = new ArrayList<CollectionItem>();

        String encodedUrl = "";
        if (selectedDptName.contains(" ")) {
            encodedUrl = selectedDptName.replace(" ", "%20");
        }

        String fullURL = ipAdd + getSortingListByDepartmentURL + encodedUrl  + "/" + token;
        JSONArray a = JSONParser.getJSONArrayFromUrl(ipAdd + getSortingListByDepartmentURL + encodedUrl  + "/" + token);

        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject o = a.getJSONObject(i);
                if (Integer.parseInt(o.getString("CollectedQty"))>0){

                    String itemNum = o.getString("ItemNumber");
                    String description = o.getString("Description");
                    String quantityOrdered = o.getString("QuantityOrdered");
                    String collectedQty = o.getString("CollectedQty");
                    String pendingQty = o.getString("PendingQty");
                    cList.add(new CollectionItem(itemNum, description, quantityOrdered, collectedQty, pendingQty));
                }

            }
            return cList;


        } catch (Exception e) {
            Log.e("CollectList.sortList()", "JSONArray error");
        }
        return (null);

    }

}
