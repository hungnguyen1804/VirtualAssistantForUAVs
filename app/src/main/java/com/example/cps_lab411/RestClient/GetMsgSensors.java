package com.example.cps_lab411.RestClient;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.cps_lab411.MainActivity;
import com.example.cps_lab411.R;

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

public class GetMsgSensors extends Dialog implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String baseURL = "https://demo.thingsboard.io:443/api";
    private String authTokenSensors;
    private String deviceIdSensors;
    private ListView listView;
    private Context context;

    //test
    private TextView tv_battery, tv_connected, tv_mode, tv_armed;

    public GetMsgSensors(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_get_data);
        listView = findViewById(R.id.lv_displaySensor);
        listView.setSelector(R.color.white);

        //Token
        authTokenSensors = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsaW5oLm5uMjgwMzk5QGdtYWlsLmNvbSIsInVzZXJJZCI6ImJiYWExZTUwLWQ2NTMtMTFlYi05MzgxLWFiMmExYThkYWFmMCIsInNjb3BlcyI6WyJURU5BTlRfQURNSU4iXSwic2Vzc2lvbklkIjoiOTczYTE2MWItMzdlNi00MWU1LWFkZGEtZmNkZWRkNTVkNGVmIiwiaXNzIjoidGhpbmdzYm9hcmQuaW8iLCJpYXQiOjE2Njg3MDQ3NTcsImV4cCI6MTY3MDUwNDc1NywiZmlyc3ROYW1lIjoiTmd1eWVuIiwibGFzdE5hbWUiOiJOaGF0IExpbmgiLCJlbmFibGVkIjp0cnVlLCJwcml2YWN5UG9saWN5QWNjZXB0ZWQiOnRydWUsImlzUHVibGljIjpmYWxzZSwidGVuYW50SWQiOiJiYTgyOGU0MC1kNjUzLTExZWItOTM4MS1hYjJhMWE4ZGFhZjAiLCJjdXN0b21lcklkIjoiMTM4MTQwMDAtMWRkMi0xMWIyLTgwODAtODA4MDgwODA4MDgwIn0.eNi61-pOs95BivoCHVem9l4dvFYgK0qPypR3CJ9alJBdVmnvY6_PQvZ8yTFHu_9ArrqaRrtU4EYAOIaK8uVRlQ";
        //DeviceId
        deviceIdSensors = "1c92fbe0-31e5-11ed-baa6-316745deef35";

        Runnable sendData = new Runnable() {
            public void run() {
                addDataToScreen();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(sendData, 0, 3, TimeUnit.SECONDS);
    }

    private void addDataToScreen() {
        getTelemetryValuesSensors("DEVICE", deviceIdSensors);
    }


    private void getTelemetryValuesSensors(String deviceType, String deviceId) {
        String URL = baseURL + "/plugins/telemetry/" + deviceType + "/" + deviceId + "/values/attributes/CLIENT_SCOPE";

        final List<DataSensorModel> dataSensorModels = new ArrayList<>();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        State state = new State();

                        for (int i = 0; i < response.length(); i++)
                        {
                            DataSensorModel dataSensorModel = new DataSensorModel();
                            try {
                                JSONObject data = response.getJSONObject(i);
                                dataSensorModel.setSensorName(data.getString("key"));
                                dataSensorModel.setSensorValue(data.getString("value"));
                                dataSensorModels.add(dataSensorModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, dataSensorModels);
                        listView.setAdapter(arrayAdapter);
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
                headers.put("X-Authorization", "Bearer " + authTokenSensors);
                return headers;
            }
            
        };
        VolleyController.getInstance(context).addToQueue(jsonObjReq);
    }

    @Override
    public void onClick(View view) {

    }
}
