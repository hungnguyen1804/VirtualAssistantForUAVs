package com.example.cps_lab411.API;

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
import com.example.cps_lab411.UavState.UavParam;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GetDataDevice extends Dialog implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String baseURL = "https://demo.thingsboard.io:443/api";
    private String authTokenState;
    private String authTokenGlobalPosition;
    private String deviceIdState;
    private String deviceIdGlobal;
    private Context context;

    public static int batteryCloud = 0;
    public static int armedCloud = 0;
    public static int connectedCloud = -1;
    public static int modeCloud = 0;
    public static float altitudeCloud = 0;
    public static double latitudeCloud = 21.0064388;
    public static double longitudeCloud = 105.8428835;
    public static float VxCloud = 0;
    public static float VyCloud = 0;
    public static float VzCloud = 0;
    public static float YawCloud = 0;


    public GetDataDevice(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_get_data_state_global_position);
        //Token
        authTokenState = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmFpbmluZy5sYWI0MTFAZ21haWwuY29tIiwidXNlcklkIjoiN2FhZTNmMTAtMjE5Yi0xMWVjLWI0YTUtY2ZiMjg5YWYzOGQ5Iiwic2NvcGVzIjpbIlRFTkFOVF9BRE1JTiJdLCJzZXNzaW9uSWQiOiI5NDNjZjQ0Yy0xNWQ3LTRhNTMtYWEwZC0zY2UyMTVjZjcwMjkiLCJpc3MiOiJ0aGluZ3Nib2FyZC5pbyIsImlhdCI6MTY2ODcwNTI2NSwiZXhwIjoxNjcwNTA1MjY1LCJmaXJzdE5hbWUiOiI0MTEiLCJsYXN0TmFtZSI6ImxhYiIsImVuYWJsZWQiOnRydWUsInByaXZhY3lQb2xpY3lBY2NlcHRlZCI6dHJ1ZSwiaXNQdWJsaWMiOmZhbHNlLCJ0ZW5hbnRJZCI6IjdhMjU2MDAwLTIxOWItMTFlYy1iNGE1LWNmYjI4OWFmMzhkOSIsImN1c3RvbWVySWQiOiIxMzgxNDAwMC0xZGQyLTExYjItODA4MC04MDgwODA4MDgwODAifQ.JQ94qdjZtbtU2mTZ2dZ0zAsS6t-DiSZyCCmBY4lk1wacKjBWDrmiiq51l33ruRCleB2_LKLCz2DKaKXxA6Jr7g";
        authTokenGlobalPosition = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmFpbmluZy5sYWI0MTFAZ21haWwuY29tIiwidXNlcklkIjoiN2FhZTNmMTAtMjE5Yi0xMWVjLWI0YTUtY2ZiMjg5YWYzOGQ5Iiwic2NvcGVzIjpbIlRFTkFOVF9BRE1JTiJdLCJzZXNzaW9uSWQiOiI5NDNjZjQ0Yy0xNWQ3LTRhNTMtYWEwZC0zY2UyMTVjZjcwMjkiLCJpc3MiOiJ0aGluZ3Nib2FyZC5pbyIsImlhdCI6MTY2ODcwNTI2NSwiZXhwIjoxNjcwNTA1MjY1LCJmaXJzdE5hbWUiOiI0MTEiLCJsYXN0TmFtZSI6ImxhYiIsImVuYWJsZWQiOnRydWUsInByaXZhY3lQb2xpY3lBY2NlcHRlZCI6dHJ1ZSwiaXNQdWJsaWMiOmZhbHNlLCJ0ZW5hbnRJZCI6IjdhMjU2MDAwLTIxOWItMTFlYy1iNGE1LWNmYjI4OWFmMzhkOSIsImN1c3RvbWVySWQiOiIxMzgxNDAwMC0xZGQyLTExYjItODA4MC04MDgwODA4MDgwODAifQ.JQ94qdjZtbtU2mTZ2dZ0zAsS6t-DiSZyCCmBY4lk1wacKjBWDrmiiq51l33ruRCleB2_LKLCz2DKaKXxA6Jr7g";
        //DeviceId
        deviceIdState = "3ecb4dd0-4ebd-11ed-b827-c9be76c6f5d7";
        deviceIdGlobal = "fb322470-4ec3-11ed-b827-c9be76c6f5d7";


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
                        State state = new State();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                if (i == 0) {
                                    state.setArmed(response.getJSONObject(i).getString("value"));
                                    armedCloud = Integer.parseInt(state.getArmed());
                                } else if (i == 1) {
                                    state.setBattery(response.getJSONObject(i).getString("value"));
                                    batteryCloud = Integer.parseInt(state.getBattery());
                                } else if (i == 2) {
                                    state.setConnected(response.getJSONObject(i).getString("value"));
                                    connectedCloud = Integer.parseInt(state.getConnected());
                                } else if (i == 3) {
                                    state.setModeCloud(response.getJSONObject(i).getString("value"));
                                    modeCloud = Integer.parseInt(state.getModeCloud());
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
                        GlobalPosition globalPosition = new GlobalPosition();

                        for (int i = 0; i < response.length(); i++)
                        {
                            try {
                                if (i == 0) {
                                    globalPosition.setAltitude_Cloud(response.getJSONObject(i).getString("value"));
                                    altitudeCloud = Float.parseFloat(globalPosition.getAltitude_Cloud()) / 100.0f;
                                }
                                else if (i == 1) {
                                    globalPosition.setLatitude_Cloud(response.getJSONObject(i).getString("value"));
                                    latitudeCloud = Double.parseDouble(globalPosition.getLatitude_Cloud()) / 10000000.0f;
                                }
                                else if (i == 2) {
                                    globalPosition.setLongitude_Cloud(response.getJSONObject(i).getString("value"));
                                    longitudeCloud = Double.parseDouble(globalPosition.getLongitude_Cloud()) / 10000000.0f;
                                }
                                else if (i == 3) {
                                    globalPosition.setVx_Cloud(response.getJSONObject(i).getString("value"));
                                    VxCloud = Float.parseFloat(globalPosition.getVx_Cloud()) / 100.0f;
                                }
                                else if (i == 4) {
                                    globalPosition.setVy_Cloud(response.getJSONObject(i).getString("value"));
                                    VyCloud = Float.parseFloat(globalPosition.getVy_Cloud()) / 100.0f;
                                }
                                else if (i == 5) {
                                    globalPosition.setVz_Cloud(response.getJSONObject(i).getString("value"));
                                    VzCloud = Float.parseFloat(globalPosition.getVz_Cloud()) / 100.0f;
                                }
                                else if (i == 6) {
                                    globalPosition.setYawrate_Cloud(response.getJSONObject(i).getString("value"));
                                    YawCloud = Float.parseFloat(globalPosition.getYawrate_Cloud()) / 100.0f;
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
