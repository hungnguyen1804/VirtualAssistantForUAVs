package com.example.cps_lab411.RestAPI.Thingsboard;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.cps_lab411.MainActivity;
import com.example.cps_lab411.R;
import com.example.cps_lab411.RestAPI.GlobalPosition;
import com.example.cps_lab411.RestAPI.State;
import com.example.cps_lab411.RestAPI.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GetMsgStateGlobalPositionDevice extends Dialog implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String baseURL = "https://thingsboard.cloud:443/api";
    private String authTokenState;
    private String authTokenGlobalPosition;
    private String deviceIdState;
    private String deviceIdGlobal;
    private Context context;

//    public static int batteryCloud = 0;
//    public static int armedCloud = 0;
//    public static int connectedCloud = -1;
//    public static int modeCloud = 0;
//    public static float altitudeCloud = 0;
//    public static double latitudeCloud = 21.0064388;
//    public static double longitudeCloud = 105.8428835;
//    public static float VxCloud = 0;
//    public static float VyCloud = 0;
//    public static float VzCloud = 0;
//    public static float YawCloud = 0;


    public GetMsgStateGlobalPositionDevice(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_get_data_state_global_position);
        //Token
        authTokenState = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsYWI0MTEudHJhaW5pbmdAZ21haWwuY29tIiwidXNlcklkIjoiM2VjMWNhNTAtNzQ0MC0xMWVkLWIyNGMtYWI0MDgyNmM2ODliIiwic2NvcGVzIjpbIlRFTkFOVF9BRE1JTiJdLCJpc3MiOiJ0aGluZ3Nib2FyZC5jbG91ZCIsImlhdCI6MTY3MDMxMzc2NywiZXhwIjoxNjcwMzQyNTY3LCJmaXJzdE5hbWUiOiJUcmFpbmluZyIsImxhc3ROYW1lIjoiTGFiNDExIiwiZW5hYmxlZCI6dHJ1ZSwiaXNQdWJsaWMiOmZhbHNlLCJpc0JpbGxpbmdTZXJ2aWNlIjpmYWxzZSwicHJpdmFjeVBvbGljeUFjY2VwdGVkIjp0cnVlLCJ0ZXJtc09mVXNlQWNjZXB0ZWQiOnRydWUsInRlbmFudElkIjoiM2RiZWQ5NDAtNzQ0MC0xMWVkLWIyNGMtYWI0MDgyNmM2ODliIiwiY3VzdG9tZXJJZCI6IjEzODE0MDAwLTFkZDItMTFiMi04MDgwLTgwODA4MDgwODA4MCJ9.2_xHbX4oDye_J6Xeg1JvMrgg8luL4GosikJVBoSjxsLkWMo2QBeh4DOY7xy6SAQ75l9D9GKRJJ-RbIEiJVcSXg";
        authTokenGlobalPosition = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsYWI0MTEudHJhaW5pbmdAZ21haWwuY29tIiwidXNlcklkIjoiM2VjMWNhNTAtNzQ0MC0xMWVkLWIyNGMtYWI0MDgyNmM2ODliIiwic2NvcGVzIjpbIlRFTkFOVF9BRE1JTiJdLCJpc3MiOiJ0aGluZ3Nib2FyZC5jbG91ZCIsImlhdCI6MTY3MDMxMzc2NywiZXhwIjoxNjcwMzQyNTY3LCJmaXJzdE5hbWUiOiJUcmFpbmluZyIsImxhc3ROYW1lIjoiTGFiNDExIiwiZW5hYmxlZCI6dHJ1ZSwiaXNQdWJsaWMiOmZhbHNlLCJpc0JpbGxpbmdTZXJ2aWNlIjpmYWxzZSwicHJpdmFjeVBvbGljeUFjY2VwdGVkIjp0cnVlLCJ0ZXJtc09mVXNlQWNjZXB0ZWQiOnRydWUsInRlbmFudElkIjoiM2RiZWQ5NDAtNzQ0MC0xMWVkLWIyNGMtYWI0MDgyNmM2ODliIiwiY3VzdG9tZXJJZCI6IjEzODE0MDAwLTFkZDItMTFiMi04MDgwLTgwODA4MDgwODA4MCJ9.2_xHbX4oDye_J6Xeg1JvMrgg8luL4GosikJVBoSjxsLkWMo2QBeh4DOY7xy6SAQ75l9D9GKRJJ-RbIEiJVcSXg";
        //DeviceId
        deviceIdState = "cc996430-746f-11ed-8b62-e9eba22b9df6";
        deviceIdGlobal = "183a8180-7470-11ed-a3e8-cd953d903e76";


        Runnable runnable = new Runnable() {
            public void run() {
                addDataToScreen1();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, 3, TimeUnit.SECONDS);

        Runnable runnable2 = new Runnable() {
            public void run() {
                addDataToScreen2();
            }
        };

        ScheduledExecutorService executor1 = Executors.newScheduledThreadPool(1);
        executor1.scheduleAtFixedRate(runnable2, 0, 3, TimeUnit.SECONDS);
    }

    private void addDataToScreen1() {
        //getTelemetryValuesSensors("DEVICE", deviceIdSensors);
        getTelemetryValuesState("DEVICE", deviceIdState);
    }

    private void addDataToScreen2() {
        //getTelemetryValuesSensors("DEVICE", deviceIdSensors);
        getTelemetryValuesGlobalPositon("DEVICE", deviceIdGlobal);
    }

    private void getTelemetryValuesState(String deviceType, String deviceIdState) {
        String URL = baseURL + "/plugins/telemetry/" + deviceType + "/" + deviceIdState + "/values/attributes/CLIENT_SCOPE";
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //State state = new State();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                if (i == 0) {
                                    State.getInstance().setArmed(Integer.parseInt(response.getJSONObject(i).getString("value")));
                                    //state.setArmed(response.getJSONObject(i).getString("value"));
//                                    armedCloud = Integer.parseInt(state.getArmed());
                                } else if (i == 1) {
                                    State.getInstance().setBattery(Integer.parseInt(response.getJSONObject(i).getString("value")));
                                    //state.setBattery(response.getJSONObject(i).getString("value"));
//                                    batteryCloud = Integer.parseInt(state.getBattery());
                                } else if (i == 2) {
                                    State.getInstance().setConnected(Integer.parseInt(response.getJSONObject(i).getString("value")));
                                    //state.setConnected(response.getJSONObject(i).getString("value"));
//                                    connectedCloud = Integer.parseInt(state.getConnected());
                                } else if (i == 3) {
                                    State.getInstance().setModeCloud(Integer.parseInt(response.getJSONObject(i).getString("value")));
                                    //state.setModeCloud(response.getJSONObject(i).getString("value"));
//                                    modeCloud = Integer.parseInt(state.getModeCloud());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("X-Authorization", "Bearer " + authTokenState);
                return headers;
            }

        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    private void getTelemetryValuesGlobalPositon(String deviceType, String deviceId) {
        String URL = baseURL + "/plugins/telemetry/" + deviceType + "/" + deviceId + "/values/attributes/CLIENT_SCOPE";

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //GlobalPosition globalPosition = new GlobalPosition();

                        for (int i = 0; i < response.length(); i++)
                        {
                            try {
                                if (i == 0) {
                                    GlobalPosition.getInstance().setAltitude(Float.parseFloat(response.getJSONObject(i).getString("value")));
                                    //globalPosition.setAltitude_Cloud(response.getJSONObject(i).getString("value"));
//                                    altitudeCloud = Float.parseFloat(globalPosition.getAltitude_Cloud()) / 100.0f;
                                }
                                else if (i == 1) {
                                    GlobalPosition.getInstance().setLatitude(Double.parseDouble(response.getJSONObject(i).getString("value")));
                                    //globalPosition.setLatitude_Cloud(response.getJSONObject(i).getString("value"));
                                    //latitudeCloud = Double.parseDouble(globalPosition.getLatitude_Cloud()) / 10000000.0f;
                                }
                                else if (i == 2) {
                                    GlobalPosition.getInstance().setLongitude(Double.parseDouble(response.getJSONObject(i).getString("value")));
                                    //globalPosition.setLongitude_Cloud(response.getJSONObject(i).getString("value"));
                                    //longitudeCloud = Double.parseDouble(globalPosition.getLongitude_Cloud()) / 10000000.0f;
                                }
                                else if (i == 3) {
                                    GlobalPosition.getInstance().setVx(Float.parseFloat(response.getJSONObject(i).getString("value")));
                                    //globalPosition.setVx_Cloud(response.getJSONObject(i).getString("value"));
                                    //VxCloud = Float.parseFloat(globalPosition.getVx_Cloud()) / 100.0f;
                                }
                                else if (i == 4) {
                                    GlobalPosition.getInstance().setVy(Float.parseFloat(response.getJSONObject(i).getString("value")));
                                    //globalPosition.setVy_Cloud(response.getJSONObject(i).getString("value"));
                                    //VyCloud = Float.parseFloat(globalPosition.getVy_Cloud()) / 100.0f;
                                }
                                else if (i == 5) {
                                    GlobalPosition.getInstance().setVz(Float.parseFloat(response.getJSONObject(i).getString("value")));
                                    //globalPosition.setVz_Cloud(response.getJSONObject(i).getString("value"));
                                    //VzCloud = Float.parseFloat(globalPosition.getVz_Cloud()) / 100.0f;
                                }
                                else if (i == 6) {
                                    GlobalPosition.getInstance().setYawrate(Float.parseFloat(response.getJSONObject(i).getString("value")));
                                    //globalPosition.setYawrate_Cloud(response.getJSONObject(i).getString("value"));
                                    //YawCloud = Float.parseFloat(globalPosition.getYawrate_Cloud()) / 100.0f;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("X-Authorization", "Bearer " + authTokenGlobalPosition);
                return headers;
            }

        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    @Override
    public void onClick(View view) {

    }
}
