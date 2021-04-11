package comgalaxyglotech.confirmexperts.generalmarket.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import comgalaxyglotech.confirmexperts.generalmarket.ClassPack.HttpCallBackSelector;

public class APIConnection {
    public static void MakeRequest(Context context, String URL, String callBackCode){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        HttpCallBackSelector.SelectCallBack(response,null,callBackCode);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HttpCallBackSelector.SelectCallBack(null, error,"Error");
                    }
                }
        );

        requestQueue.add(objectRequest);
    }
}
