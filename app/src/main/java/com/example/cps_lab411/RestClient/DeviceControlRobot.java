package com.example.cps_lab411.RestClient;

import com.example.cps_lab411.UavState.RobotArmParam;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class DeviceControlRobot {
    public static Boolean checkControlRobot = false;
    String Step1 = String.valueOf(RobotArmParam.getInstance().getServo1());
    String Step2 = String.valueOf(RobotArmParam.getInstance().getServo2());
    String Step3 = String.valueOf(RobotArmParam.getInstance().getServo3());
    String Step4 = String.valueOf(RobotArmParam.getInstance().getServo4());
    String Step5 = String.valueOf(RobotArmParam.getInstance().getServo5());


    private String accessToken;
    // Thingsboard topic name
    String topic = "v1/devices/me/attributes";
    //data to be send
    String content = "{Step1:"+ Step1 + ", Step2:" + Step2 + ", Step3:" + Step3 + ", Step4:" + Step4 + ",Step5:" + Step5 + "}";
    int qos = 0;
    String broker = "tcp://demo.thingsboard.io:1883";
    String clientId = "TB4";
    MemoryPersistence persistence = new MemoryPersistence();

    public void SendDataDevice() {
        if (checkControlRobot) {
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
        }
    }
}
