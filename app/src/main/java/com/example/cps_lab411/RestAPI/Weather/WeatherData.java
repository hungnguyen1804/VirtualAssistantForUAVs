package com.example.cps_lab411.RestAPI.Weather;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cps_lab411.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherData extends Dialog implements View.OnClickListener{
    public static String temperature = "0";
    public static String status = "0";

    private EditText et_city;
    private TextView tv_city, lonlat, statusWeather, temp, hum, cloud, wind;
    private ImageView img_statusWeather;

    private static final String baseURL = "https://api.openweathermap.org/data/2.5/weather?q=Hanoi&appid=36c967526d6baa50490c7ff1ce49af04";
    public WeatherData(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_weather);

        tv_city = findViewById(R.id.tv_nameCity);
        temp = findViewById(R.id.tv_tempDisplay);
        img_statusWeather = findViewById(R.id.img_iconWeather);
        hum = findViewById(R.id.tv_humidity);
        cloud = findViewById(R.id.tv_cloud);
        wind = findViewById(R.id.tv_wind);
        lonlat = findViewById(R.id.tv_coordinatorLocation);
        statusWeather = findViewById(R.id.tv_statusWeather);
        addDataToScreen();

    }

    private void addDataToScreen() {
        getDataWeahterValues();
    }

    private void getDataWeahterValues() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String URL = baseURL;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            Weather.getInstance().setNameCity(jsonObject.getString("name"));
                            tv_city.setText(Weather.getInstance().getNameCity());
                            //Ngày tháng năm thứ
                            long l = Long.valueOf(day);
                            Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
                            String Day = simpleDateFormat.format(date);
                            Weather.getInstance().setDate(Day);

                            JSONObject jsonObjectLocation = jsonObject.getJSONObject("coord");
                            Weather.getInstance().setLati(jsonObjectLocation.getString("lat"));
                            Weather.getInstance().setLon(jsonObjectLocation.getString("lon"));
                            lonlat.setText("Lon:" + Weather.getInstance().getLon() + " " + "Lat:" + Weather.getInstance().getLati());

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            Weather.getInstance().setStatus(jsonObjectWeather.getString("main"));
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.with(getContext()).load("http://openweathermap.org/img/w/" + icon +".png").into(img_statusWeather);
                            statusWeather.setText(Weather.getInstance().getStatus());

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String tempera = jsonObjectMain.getString("temp");
                            Weather.getInstance().setHumidity(jsonObjectMain.getString("humidity"));
                            Double a = Double.valueOf(tempera);
                            Weather.getInstance().setTemperature(String.valueOf(a.intValue() - 273));
                            temp.setText(Weather.getInstance().getTemperature() + "°C");
                            hum.setText(Weather.getInstance().getHumidity() + "%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String windy = jsonObjectWind.getString("speed");
                            Weather.getInstance().setWind(windy);
                            wind.setText(windy + "m/s");

                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String cloudy = jsonObjectClouds.getString("all");
                            cloud.setText(cloudy + "%");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {

    }
}
