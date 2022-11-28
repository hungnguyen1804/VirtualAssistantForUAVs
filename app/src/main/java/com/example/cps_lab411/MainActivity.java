package com.example.cps_lab411;

//import static com.example.cps_lab411.RestClient.GetMsgStateGlobalPositionDevice.armedCloud;
//import static com.example.cps_lab411.RestClient.GetMsgStateGlobalPositionDevice.batteryCloud;
//import static com.example.cps_lab411.RestClient.GetMsgStateGlobalPositionDevice.connectedCloud;
//import static com.example.cps_lab411.RestClient.GetMsgStateGlobalPositionDevice.modeCloud;
import static com.example.cps_lab411.RestClient.ModeSelectCommandDevice.HOLD;
import static com.example.cps_lab411.RestClient.ModeSelectCommandDevice.LAND;
import static com.example.cps_lab411.RestClient.ModeSelectCommandDevice.MANUAL;
import static com.example.cps_lab411.RestClient.ModeSelectCommandDevice.OFF_BOARD;
import static com.example.cps_lab411.RestClient.ModeSelectCommandDevice.POSITION;
import static com.example.cps_lab411.RestClient.ModeSelectCommandDevice.TAKE_OFF;
import static com.example.cps_lab411.RestClient.ModeSelectCommandDevice.altitudeTest;
import static com.example.cps_lab411.MapFragment.checked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alan.alansdk.AlanCallback;
import com.alan.alansdk.AlanConfig;
import com.alan.alansdk.button.AlanButton;
import com.alan.alansdk.events.EventCommand;
import com.example.cps_lab411.RestClient.DeviceModeCommand;
import com.example.cps_lab411.RestClient.GetMsgStateGlobalPositionDevice;
import com.example.cps_lab411.RestClient.State;
import com.example.cps_lab411.RestClient.Weather.Weather;
import com.example.cps_lab411.RestClient.Weather.WeatherData;
import com.example.cps_lab411.Category.CategoryFlightModeAdapter;
import com.example.cps_lab411.Category.CategoryMode;
import com.example.cps_lab411.Communication.EncodeData;
import com.example.cps_lab411.EvenBus.ConntectHandleEvenbus;
import com.example.cps_lab411.EvenBus.UDPMessageEvenBus;
import com.example.cps_lab411.Communication.UDP.ManualControlThread;
import com.example.cps_lab411.UavState.UavMode;
import com.example.cps_lab411.UavState.UavParam;
import com.google.android.material.navigation.NavigationView;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AlanButton alanButton;
    private int commandSetMode = 26;
    private int commandArmDisarm = 23;
    private static boolean checkWeather = false;
    public static boolean checkGotoChargingStation = false;
    public static boolean checkStationCharging = false;
    public static int modeDevice = 0;
    private DrawerLayout mDrawerLayout;
    ImageView mSensorImV;
    public static  TextView mArmCheck, mFlightMode, mTvUAVBattery;
    private static ImageView mUAVBattery;
    private static ImageView mImvConnection, mImvSwControlMode, mImSwCloudThingsboard, mImSwOnCloud;
    private Spinner spnFlightMode;
    private CategoryFlightModeAdapter categoryFlightModeAdapter;
    private EncodeData encodeData = new EncodeData();
    private ManualControlThread manualControlThread = null;
    private EditText mParam1, mParam2, mParam3, mParam4;


    ImageView cockpit1, cockpit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity();
        // Set up the Alan button
        AlanConfig config = AlanConfig.builder().setProjectId("66787a51a6e0c931fd225afba233c9522e956eca572e1d8b807a3e2338fdd0dc/stage").build();
        alanButton = findViewById(R.id.alan_button);
        alanButton.initWithConfig(config);

        AlanCallback alanCallback = new AlanCallback() {
            /// Handle commands from Alan Studio
            @Override
            public void onCommand(final EventCommand eventCommand) {
                try {
                    JSONObject command = eventCommand.getData();
                    JSONObject data = command.getJSONObject("data");
                    Log.d("JSONObject", "Basic Object: " + data);
                    String commandName = data.getString("command");
                    Log.d("JSONObject", "Data Object: " + commandName);
                    executeCommand(commandName, data);
                } catch (JSONException e) {
                    Log.e("AlanButton", e.getMessage());
                }
            }
        };

        alanButton.registerCallback(alanCallback);

        mParam1 = findViewById(R.id.et_param1);
        mParam2 = findViewById(R.id.et_param2);
        mParam3 = findViewById(R.id.et_param3);
        mParam4 = findViewById(R.id.et_param4);

        mSensorImV = findViewById(R.id.sensor_db);
        mArmCheck = findViewById(R.id.tv_armCheck);
        mUAVBattery = findViewById(R.id.batterry_ic);
        mFlightMode = findViewById(R.id.tv_flightmode);
        mImvConnection = findViewById(R.id.flightmode_ic);
        // Add menu
        mTvUAVBattery = findViewById(R.id.batteryState);
        mImvSwControlMode = findViewById(R.id.imageSwControlMode);
        mImSwCloudThingsboard = findViewById(R.id.imageSwCloud);
//        mImSwOnCloud = findViewById(R.id.imageSwOnCloudDisplay);

        //Init Connection Status = false
        DataHolder.getInstance().setConnectionStatus(false);

        // Add Custom Toolbar
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().hide();

        cockpit1 = findViewById(R.id.cockpit_layout1);
        cockpit2 = findViewById(R.id.cockpit_layout2);

        // Add Drawer Layout
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.lab411_logo4);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set Map Fragment as Default Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.conn_frame, new ConnFragment(), "con").commit();

        mSensorImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Sensor Clicked");
            }
        });

        mArmCheck.setOnClickListener(view -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Arming Vehicle?");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Yes",
                    (dialog, id) -> {
                        DeviceModeCommand deviceModeCommand = new DeviceModeCommand();
                        DataHolder.getInstance().setSendArmCommand(true);
                        if (UavParam.getInstance().getAimMode() == 1) {
                            encodeData.SendCommandArmDisarm((byte) 0);
                        } else {
                            encodeData.SendCommandArmDisarm((byte) 1);
                        }
                        if (State.getInstance().getArmed() == 1) {
                            deviceModeCommand.SendDataDeviceCommand(commandArmDisarm ,0);
                        }
                        else {
                            deviceModeCommand.SendDataDeviceCommand(commandArmDisarm ,1);
                        }
                        dialog.cancel();
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        });

        spnFlightMode = findViewById(R.id.spn_flightMode);
        categoryFlightModeAdapter = new CategoryFlightModeAdapter(this, R.layout.item_selected, getListCategory());
        spnFlightMode.setAdapter(categoryFlightModeAdapter);
        spnFlightMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Send device command data to thingsboard
                modeDevice = categoryFlightModeAdapter.getItem(position).getModeCommandID();
                int mode_define = categoryFlightModeAdapter.getItem(position).getFlightModeID();
                DeviceModeCommand deviceModeCommand = new DeviceModeCommand();
                deviceModeCommand.SendDataDeviceCommand(commandSetMode ,mode_define);
                setModeStatus(State.getInstance().getModeCloud(), State.getInstance().getConnected());
                if (DataHolder.getInstance().getConnectionStatus()) {
                    int flightMode = categoryFlightModeAdapter.getItem(position).getFlightModeID();
                    encodeData.SendCommandSetMode(flightMode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mImvSwControlMode.setOnClickListener(view -> {
            int currentMode = DataHolder.getInstance().getControlMode();
            if(currentMode == 1) {
                //Set UAV control Mode
                DataHolder.getInstance().setControlMode(0);
                mImvSwControlMode.setImageResource(R.drawable.takeoff);

            } else {
                //Set Robot control Mode
                DataHolder.getInstance().setControlMode(1);
                mImvSwControlMode.setImageResource(R.drawable.robotic_arm);
            }
        });

        mImSwCloudThingsboard.setOnClickListener(view -> {
            int currentMode = DataHolder.getInstance().getCloudMode();
            if (currentMode == 1) {
                checked = false;
                //Turn off data thingsboard
                DataHolder.getInstance().setCloudMode(0);
                mImSwCloudThingsboard.setImageResource(R.drawable.cloud_off);
            }
            else {
                checked = true;
                DataHolder.getInstance().setCloudMode(1);
                mImSwCloudThingsboard.setImageResource(R.drawable.iot_cloud);
                GetMsgStateGlobalPositionDevice dialog = new GetMsgStateGlobalPositionDevice(this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                dialog.hide();
                WeatherData weatherData = new WeatherData(this);
                weatherData.show();
                weatherData.hide();
            }
        });

    }

    public void executeCommand(String commandName, JSONObject data) {
        WeatherData weatherData = new WeatherData(MainActivity.this);
        if (commandName.equals("battery_percent")) {
            if (State.getInstance().getBattery() < 20) {
                alanButton.playText("battery low, need to charging station");
            }
            alanButton.playText(String.valueOf(State.getInstance().getBattery()) + "percent");
        }

        if (commandName.equals("weather")) {
            if (Integer.parseInt(Weather.getInstance().getTemperature()) != 0) {
                weatherData.show();
                if (Weather.getInstance().getStatus().contains("rain") == false) {
                    checkWeather = true;
                }
                alanButton.playText(Weather.getInstance().getNameCity() + "status " + Weather.getInstance().getStatus() + ", temperature"  + Weather.getInstance().getTemperature() + "humidity " +
                        Weather.getInstance().getHumidity() + ", cloud " + Weather.getInstance().getCloud() + ", win " + Weather.getInstance().getWind() + "m/s");
            }
            else {
                alanButton.playText("unknown");
            }
        }

        if (commandName.equals("condition_weather")) {
            if (Integer.parseInt(Weather.getInstance().getTemperature()) > 15 && Integer.parseInt(Weather.getInstance().getTemperature()) < 40
                    && Weather.getInstance().getStatus().contains("rain") == false) {
                alanButton.playText("uav can take off now");
            }
            else {
                alanButton.playText("wait until the weather conditions get better");
            }
        }

        if (commandName.equals("take_off")) {
                if (Integer.parseInt(Weather.getInstance().getTemperature()) > 15 && Integer.parseInt(Weather.getInstance().getTemperature()) < 40
                        && Weather.getInstance().getStatus().contains("rain") == false) {
                    modeDevice = TAKE_OFF;
                    try {
                        altitudeTest = data.getString("alti");
                        SendCommandTakeOffAssistant();
                        alanButton.playText("OK! UAV take off with altitude is " + altitudeTest + " meter");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    alanButton.playText("can not fly now, please wait until the rain stop");
                }
        }

        if (commandName.equals("landing")) {
            modeDevice = LAND;
            altitudeTest = "0";
            SendCommandTakeOffAssistant();
            alanButton.playText("OK! UAV is landing");
        }

        if (commandName.equals("date")) {
            alanButton.playText(Weather.getInstance().getDate());
        }

        if (commandName.equals("station_charging")) {
            checkStationCharging = true;
            alanButton.playText("found the nearest charging station");
        }
        if (commandName.equals("go_to_charging_station")) {
            checkGotoChargingStation = true;
            alanButton.playText("OK, flying to charging station");
        }
    }


    public void SendCommandTakeOffAssistant() {
        int id = 0;
        String accessToken;
        // Thingsboard topic name
        String topic = "v1/devices/me/attributes";
        int qos = 0;
        String broker = "tcp://demo.thingsboard.io:1883";
        String clientId = "TB1";
        MemoryPersistence persistence = new MemoryPersistence();

        String content = "{id:" + id + ",mode_id:"+ modeDevice + ", param1:" + altitudeTest + "}";
        accessToken = "zb4BF2iuPW1uXCKSM3Rr";
        MqttClient sampleClient = null;
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setKeepAliveInterval(60);
        connOpts.setUserName(accessToken);
        try {
            sampleClient.connect(connOpts);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        try {
            sampleClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        id++;
    }




    private List<CategoryMode> getListCategory() {
        List<CategoryMode> list = new ArrayList<>();
        list.add(new CategoryMode("Manual", 0, MANUAL));
        list.add(new CategoryMode("Position", 1, POSITION));
        list.add(new CategoryMode("OffBoard", 2, OFF_BOARD));
        list.add(new CategoryMode("Land", 3, LAND));
        list.add(new CategoryMode("Hold", 4, HOLD));

        return list;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EventBus.getDefault().register(this);
        getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("con")).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new MapFragment(), "map").commit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void hideLayoutVR() {
        if (getSupportFragmentManager().findFragmentByTag("minstream1") != null || getSupportFragmentManager().findFragmentByTag("maxstream1") != null) {
            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("minmap1")).commit();
            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("minmap2")).commit();
            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("maxstream1")).commit();
            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("maxstream2")).commit();
            cockpit1.setVisibility(View.INVISIBLE);
            cockpit2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_map) {
            hideLayoutVR();
            if (getSupportFragmentManager().findFragmentByTag("map") != null) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment exists, show it.
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("map")).commit();
            } else {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment does not exist, add it to fragment manager.
                getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new MapFragment(), "map").commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("con") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("con")).commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("set") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("set")).commit();
            }

        }



         else if (id == R.id.nav_con) {
            hideLayoutVR();

            if (getSupportFragmentManager().findFragmentByTag("con") != null) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment exists, show it.
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("con")).commit();
            } else {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //if the fragment does not exist, add it to fragment manager.
                getSupportFragmentManager().beginTransaction().add(R.id.conn_frame, new ConnFragment(), "con").commit();
            }

            if (getSupportFragmentManager().findFragmentByTag("map") != null) {
                //if the other fragment is visible, hide it.
                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("map")).commit();
            }

//            if (getSupportFragmentManager().findFragmentByTag("set") != null) {
//                //if the other fragment is visible, hide it.
//                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("set")).commit();
//            }

        }
//         else if (id == R.id.nav_set) {
//            hideLayoutVR();
//            if (getSupportFragmentManager().findFragmentByTag("set") != null) {
//                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                //if the fragment exists, show it.
//                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("set")).commit();
//            } else {
//                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                //if the fragment does not exist, add it to fragment manager.
//                getSupportFragmentManager().beginTransaction().add(R.id.set_frame, new SettingFragment(), "set").commit();
//            }
//
//            if (getSupportFragmentManager().findFragmentByTag("map") != null) {
//                //if the other fragment is visible, hide it.
//                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("map")).commit();
//            }
//
//            if (getSupportFragmentManager().findFragmentByTag("con") != null) {
//                //if the other fragment is visible, hide it.
//                getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag("con")).commit();
//            }
//        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onBackPressed() {
        // Close DrawerBar when touched outside
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public static void setArmDisarmStatus(int armStatus) {
        if (armStatus == 1) {
            mArmCheck.setText(R.string.arming);

        } else  {
            mArmCheck.setText(R.string.disarm);
        }
    }

    public static void setBatteryStatus(int batteryStatus, int connectionStatus) {
        if (connectionStatus == 1 && batteryStatus < 101 && batteryStatus > 0) {
            mTvUAVBattery.setText(String.valueOf(batteryStatus));
            if (batteryStatus > 90 && batteryStatus < 101) {
                mUAVBattery.setImageResource(R.drawable.battery2_full);
            } else if (batteryStatus > 70) {
                mUAVBattery.setImageResource(R.drawable.battery2_level);
            } else if (batteryStatus > 40) {
                mUAVBattery.setImageResource(R.drawable.battery2_medium);
            } else if (batteryStatus > 20) {
                mUAVBattery.setImageResource(R.drawable.battery2_low);
            } else {
                mUAVBattery.setImageResource(R.drawable.battery2_empty);
            }

        }
    }

    public static void setModeStatus(int mode, int connectionStatus) {
        UavMode uavMode = UavMode.getUavMode(mode);
        if (connectionStatus == 1) {
            mImvConnection.setImageResource(R.drawable.ic_baseline_check_24);
            switch (uavMode) {
                case Manual: {
                    mFlightMode.setText(R.string.manual);
                    break;
                }
                case Position: {
                    mFlightMode.setText(R.string.position);
                    break;
                }
                case OffBoard: {
                    mFlightMode.setText(R.string.offboard);
                    break;
                }
                case Hold: {
                    mFlightMode.setText(R.string.hold);
                    break;
                }
                case TakeOff: {
                    mFlightMode.setText(R.string.takeoff);
                    break;
                }
                case Land: {
                    mFlightMode.setText(R.string.land);
                    break;
                }
                default: {
                    mFlightMode.setText(R.string.connected);
                    break;
                }
            }
        }
        else {
            onDisconnected();
        }
    }

    public static void onDisconnected() {
        mImvConnection.setImageResource(R.drawable.ic_baseline_uncheck_24);
        mFlightMode.setText(R.string.disconnect);
        mArmCheck.setText(R.string.disarm);
        mUAVBattery.setImageResource(R.drawable.battery2_empty);
        mTvUAVBattery.setText("0");
    }

    public void onConnected() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUDPMessage1Change(UDPMessageEvenBus UDP) {
        setArmDisarmStatus(UDP.getArm());
        setBatteryStatus(UDP.getBatteryRemaining(), UDP.getConnected());
        setModeStatus(UDP.getMode(), UDP.getConnected());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConntectEvenBus(ConntectHandleEvenbus connected) {
        if(!connected.getConntectionState()) {
            onDisconnected();
        } else {
            onConnected();
        }
    }
}