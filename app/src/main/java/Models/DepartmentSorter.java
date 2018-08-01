package Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Utilities.Constants;
import Utilities.JSONParser;

public class DepartmentSorter extends HashMap<String, String> {

    //final static String ipAdd = "http://172.23.200.89/LogicUniversity/Services/androidService.svc/";

    // Displaying sorting details
    final private static String displayListofDepartmentsForCollectionURL = "/Department/Sorting/DptList";
    final private static String getDptIdFromDptNameURL = "/Department/Sorting/";
    final private static String getPlaceIdFromDptIdRURL = "/Department/Sorting/PlaceId/";

    // Updating ready for collection
    final private static String insertCollectionDetailsRowURL = "/Department/Sorting/InsertCollectionDetail";
    final private static String insertDisbursementListROIdURL = "/Department/Sorting/InsertDisbursementDetail/";
    final private static String getDptRepEmailAddFromDptIDURL = "/Department/Sorting/DptRepEmail/";

    public DepartmentSorter(String departmentName) {
        put("DepartmentName", departmentName);
    }

    public DepartmentSorter() {
    }

    public static List<String> displayListofDepartmentsForCollection() {
        List<String> list = new ArrayList<String>();

        JSONArray a = JSONParser.getJSONArrayFromUrl(Constants.SERVICE_HOST + displayListofDepartmentsForCollectionURL + "/" + Constants.TOKEN);
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject o = a.getJSONObject(i); // hard code?
                list.add(o.getString("DepartmentName"));
            }
        } catch (Exception e) {
            Log.e("Department.getList", "JSONArray error");
        }
        return (list);
    }

    public static String getDptIdFromDptName(String selectedDptName) {

        String encodedUrl = "";
        if (selectedDptName.contains(" ")) {
            encodedUrl = selectedDptName.replace(" ", "%20");
        }

        String a = JSONParser.getStream(Constants.SERVICE_HOST + getDptIdFromDptNameURL + encodedUrl + "/" + Constants.TOKEN);

        a.replace("\"'", "").trim();
        return a;
    }

    public static String getPlaceIdFromDptId(String dptId) {
        dptId = dptId.replace("\"", "");
        String a = JSONParser.getStream(Constants.SERVICE_HOST + getPlaceIdFromDptIdRURL + dptId + "/" + Constants.TOKEN);
        return a;
    }

    public static void readyForCollection(String dptID, String selectedDate) {

        dptID = dptID.replace("\n", "").replace("\r", "").replace("\"", "");
        String placeId = DepartmentSorter.getPlaceIdFromDptId(dptID);
        placeId = placeId.replace("\n", "").replace("\r", "");

        selectedDate = formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", selectedDate);

        JSONObject cDetails = new JSONObject();
        try {
            cDetails.put("PlaceId", Integer.parseInt(placeId));
            cDetails.put("CollectionDate", selectedDate);
            cDetails.put("DepartmentId", dptID);
            cDetails.put("Token", Constants.TOKEN);

            // Insert to collection details table
            String insertCollectionDetailsRow = JSONParser.postStream(Constants.SERVICE_HOST + insertCollectionDetailsRowURL, cDetails.toString());

            // Insert ROIDs to Disbursement List
            String insertDisbursementListROId = JSONParser.getStream(Constants.SERVICE_HOST + insertDisbursementListROIdURL + dptID + "/" + Constants.TOKEN);

        } catch (Exception e) {
            Log.e("Department.rdyCollect", "JSONArray error");
        }
    }

    public static String getDptRepEmailAddFromDptID(String dptID) {
        String a = JSONParser.getStream(Constants.SERVICE_HOST + getDptRepEmailAddFromDptIDURL + dptID + "/" + Constants.TOKEN);
        a = a.replace("\n", "").replace("\r", "");
        return a;
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e("Department.DateFormat", "ParseException - dateFormat");
        }

        return outputDate;

    }

}
