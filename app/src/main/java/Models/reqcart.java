package Models;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utilities.Constants;
import Utilities.JSONParser;

public class reqcart extends HashMap<String, Object> {

    private Inventory inv;
    private int q;

    private static List<reqcart> carts = new ArrayList<reqcart>();


    public List<reqcart> getCarts() {
        return carts;
    }

    public Inventory getInv() {
        return inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public reqcart()
    {}

    public reqcart(Inventory i, int q)
    {
        put("inventory", i);
        setInv(i);
        put("cart_quantity", q);
        setQ(q);
    }

    public static void additemtocart(Inventory i)
    {
        int index = isExisting(i);
        Log.i("index", Integer.toString(index));
        if(index != -1)
        {
            int quan = carts.get(index).getQ();
            Log.i("i",Integer.toString(quan));
            quan = quan +1;
            carts.get(index).setQ(quan);
        }
        else
        {
            carts.add(new reqcart(i, 1));
        }
    }

    public static void updatequantity(Inventory i, int q)
    {
        int index = isExisting(i);
        if(index != -1)
        {
            int quan = q;
            carts.get(index).setQ(quan);
        }
    }

    public static void removeitem(Inventory i)
    {
        int index = isExisting(i);
        if(index != -1)
        {
            carts.remove(index);
        }
    }


    public static int isExisting(Inventory b)
    {

        for(int i=0;i<carts.size();i++)
        {
            if(carts.get(i).getInv().get("item_number").trim().toLowerCase().equals(b.get("item_number").trim().toLowerCase()))
            {
                return i;
            }
        }
        return -1;
    }

    public static List<reqcart> showcart()
    {
        List<reqcart> b = new ArrayList<reqcart>();
        int m = carts.size();
        for(int i=0; i<m;i++)
        {
            reqcart r = new reqcart(carts.get(i).getInv(),carts.get(i).getQ());
            b.add(r);
        }
        return b;
    }

    public static String placerequest()
    {
        JSONObject jrequest = new JSONObject();
        try{jrequest.put("token", Constants.TOKEN);}catch(Exception e){}
        Log.i("jrequest", jrequest.toString());
        String result = JSONParser.postStream(Constants.SERVICE_HOST +"/NewRequest/Addrequest", jrequest.toString());
        String id =  result.replaceAll("\\\\", "");

        for (int i=0;i<carts.size();i++) {
            JSONObject jdetail = new JSONObject();
            try {
                    jdetail.put("inventory", carts.get(i).getInv().get("item_number"));
                    jdetail.put("cart_quantity", carts.get(i).getQ());
                    jdetail.put("id", id.replace("\"","").trim());
                    jdetail.put("token", Constants.TOKEN);
                }
                catch (Exception e) {
                Log.i("json arraylist creation", "Error");
            }
            Log.i("req-detail", jdetail.toString());
            String re = JSONParser.postStream(Constants.SERVICE_HOST+"/NewRequest/Addrequestdetail", jdetail.toString());
        }
        emptycart();
        return id;
    }

    public static void emptycart()
    {
        carts.clear();
    }

}
