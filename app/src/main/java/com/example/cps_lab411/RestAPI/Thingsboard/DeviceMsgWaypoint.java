package com.example.cps_lab411.RestAPI.Thingsboard;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class DeviceMsgWaypoint {

    static int wid = 0;
    private String accessToken;
    // Thingsboard topic name
    String topic = "v1/devices/me/attributes";
    int qos = 0;
    String broker = "tcp://thingsboard.cloud";
    String clientId = "TB3";
    MemoryPersistence persistence = new MemoryPersistence();

    public void SendDataDevice(float lat, float lon, float alt) {

        String content = "{wpid:"+ String.valueOf(wid) + ", latitude:" + String.valueOf(lat) + ", longitude:" + String.valueOf(lon) + ", altitude:" + String.valueOf(alt) + "}";
        accessToken = "u7JyZ2q9QayL07XmsTIA";
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
        wid++;
    }
}
