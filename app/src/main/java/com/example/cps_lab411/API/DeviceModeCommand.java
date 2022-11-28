package com.example.cps_lab411.API;

import static com.example.cps_lab411.API.ModeSelectCommandDevice.idcommand;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class DeviceModeCommand {

    private static int id = 0;
    private String accessToken;
    // Thingsboard topic name
    String topic = "v1/devices/me/attributes";
    int qos = 0;
    String broker = "tcp://demo.thingsboard.io:1883";
    String clientId = "TB1";
    MemoryPersistence persistence = new MemoryPersistence();

    public void SendDataDeviceCommand(int commandSetMode ,int modeFlight) {

        String content = "{id:" + id + ",mode_id:"+ commandSetMode + ", param1:" + modeFlight +"}";
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
}
