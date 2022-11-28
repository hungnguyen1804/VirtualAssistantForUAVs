package com.example.cps_lab411.API;

import static com.example.cps_lab411.API.DeviceManualControl.checkManualControl;
import static com.example.cps_lab411.API.DeviceControlRobot.checkControlRobot;
import static com.example.cps_lab411.MainActivity.modeDevice;
import static com.example.cps_lab411.MapFragment.valueAlitude;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.cps_lab411.R;
import com.google.android.gms.common.util.IOUtils;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ModeSelectCommandDevice extends Dialog implements View.OnClickListener {

    public static final int MANUAL = 19;
    public static final int POSITION = 20;
    public static final int OFF_BOARD = 21;
    public static final int TAKE_OFF = 22;
    public static final int LAND = 24;
    public static final int HOLD = 23;
    private String mode;
    private String modeListCommand;
    private static Boolean checkCommand = false;
    public static int idcommand = 0;
    private EditText mParam1, mParam2, mParam3, mParam4;
    private Spinner spnCategory;
    private CategoryAdapter categoryAdapter;
    private  Button btn_action;

    public static String altitudeTest;

    private Context context;
    String param1;
    private String accessToken;
    // Thingsboard topic name
    String topic = "v1/devices/me/attributes";
    int qos = 0;
    String broker = "tcp://demo.thingsboard.io:1883";
    String clientId = "TB1";
    MemoryPersistence persistence = new MemoryPersistence();

    public ModeSelectCommandDevice(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_submit_data);

        mParam1 = findViewById(R.id.et_param1);
        mParam2 = findViewById(R.id.et_param2);
        mParam3 = findViewById(R.id.et_param3);
        mParam4 = findViewById(R.id.et_param4);
        btn_action = findViewById(R.id.btn_action);

        spnCategory = findViewById(R.id.spn_category);
        categoryAdapter = new CategoryAdapter(context, R.layout.item_selected_mode, getListCategory());
        spnCategory.setAdapter(categoryAdapter);

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mode = categoryAdapter.getItem(i).getSelected();
                if (modeDevice == TAKE_OFF) {
                    if (Math.round(valueAlitude * 100.0) / 100.0 != 0) {
                        param1 = String.valueOf(Math.round(valueAlitude * 100.0) / 100.0);
                        mParam1.setText(param1);
                    }
                    else {
                        param1 = altitudeTest;
                        mParam1.setText(param1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Integer.parseInt(mode))
                {
                    case 1:
                        checkCommand = true;
                        SendDataDevice();
                        break;
                    case 2:
                        checkManualControl = true;
                        break;
                    case 3:
//                        checkMsgWaypoint = true;
//                        DeviceMsgWaypoint msgWaypoint = new DeviceMsgWaypoint();
//                        msgWaypoint.SendDataDevice();
                        break;
                    case 4:
                        checkControlRobot = true;
                        break;
                    default:
                        Toast.makeText(context, "Please select your device!", Toast.LENGTH_SHORT).show();
                        break;
                }
                dismiss();
            }
        });
    }

    public void SendDataDevice() {

        String content = "{id:" + idcommand + ",mode_id:"+ modeDevice + ", param1:" + mParam1.getText().toString() + "," +
                " param2:" + mParam2.getText().toString() + ", param3:" + mParam3.getText().toString()
                + ",param4:" + mParam4.getText().toString() + "}";
        if (checkCommand) {
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
            System.out.println("please get the token from thingsboard device");
            connOpts.setUserName(accessToken);
            System.out.println("Connecting to broker: " + broker);
            try {
                sampleClient.connect(connOpts);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            System.out.println("Connected to thingsboard broker");
            System.out.println("Publishing message:" + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            try {
                sampleClient.publish(topic, message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            idcommand++;
        }
    }


    @Override
    public void onClick(View view) {

    }

    private List<Category> getListCategory()
    {
        if (modeDevice == TAKE_OFF) {
            modeListCommand = "Take Off";
        }
        else if (modeDevice == LAND) {
            modeListCommand = "Land";
        }

        List<Category> list = new ArrayList<>();
        list.add(new Category("Command" + ": " + modeListCommand,"1"));
        list.add(new Category("Manual Control", "2"));
        list.add(new Category("Msg Waypoint", "3"));
        list.add(new Category("Control Robot", "4"));
        return list;
    }

}
