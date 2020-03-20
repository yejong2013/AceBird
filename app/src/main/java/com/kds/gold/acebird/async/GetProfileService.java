package com.kds.gold.acebird.async;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kds.gold.acebird.apps.Constants;
import com.kds.gold.acebird.apps.MyApp;
import com.kds.gold.acebird.models.LoginModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetProfileService {
    private String token = (String ) MyApp.instance.getPreference().get(Constants.TOKEN);
    private Context context;
    private String real_portal = (String) MyApp.instance.getPreference().get(Constants.APP_PORTAL);
    private LoginModel loginModel = (LoginModel) MyApp.instance.getPreference().get(Constants.LOGIN_INFO);
    private String mac_address = loginModel.getMac_address()+";";
    private OnGetProfilesListner listner;
    public GetProfileService(Context context){this.context = context;}

    public void  getProfile(final Context context,String Url,final int request_code){
        Log.e("url",Url);
        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    Url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                listner.OnGetProfilesData(response, request_code);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listner.OnGetProfilesData(null, request_code);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization","Bearer"+" "+ token);
                    params.put("Referer",real_portal);
                    params.put("Cookie","mac="+mac_address+" stb_lang=en; timezone="+MyApp.time_zone);
                    return params;
                }
            };
            queue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void OnGetProfilesData(OnGetProfilesListner listner){this.listner = listner;}
    public interface OnGetProfilesListner{
        public void OnGetProfilesData(JSONObject object,int request_code);
    }
}
