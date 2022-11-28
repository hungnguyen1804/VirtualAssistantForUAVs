package com.example.cps_lab411.API;

import com.example.cps_lab411.UavState.UavParam;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class DeviceManualControl {

    public static Boolean checkManualControl = false;
    String Vx = String.valueOf(UavParam.getInstance().getVx());
    String Vy = String.valueOf(UavParam.getInstance().getVy());
    String Vz = String.valueOf(UavParam.getInstance().getVz());
    String Yawrate = String.valueOf(UavParam.getInstance().getYawRate());

    private String accessToken;
    // Thingsboard topic name
    String topic = "v1/devices/me/attributes";
    //data to be send
    String content = "{Vx:"+ Vx + ", Vy:" + Vy + ", Vz:" + Vz + ", Yawrate:" + Yawrate + "}";
    int qos = 0;
    String broker = "tcp://demo.thingsboard.io:1883";
    String clientId = "TB2";
    MemoryPersistence persistence = new MemoryPersistence();


    public void SendDataDevice() {
        if (checkManualControl) {
            accessToken = "LRpdTZFesTgWTGHMr9a5";
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
        }
    }

}
