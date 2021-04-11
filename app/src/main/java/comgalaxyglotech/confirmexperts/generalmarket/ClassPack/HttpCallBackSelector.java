package comgalaxyglotech.confirmexperts.generalmarket.ClassPack;

import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public class HttpCallBackSelector {
    public static void SelectCallBack(JSONObject responseObject, VolleyError error, String callBackCode){
        if(error != null){
            //error
        }
        else if(callBackCode.equals("test")){
            Log.d("market", responseObject.toString());
        }
        else{

        }
    }
}
