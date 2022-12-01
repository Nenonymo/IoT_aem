package com.example.iotlab3app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

//import org.eclipse.paho.android.service.MqttAndroidClient;
import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.*;

public class MainActivity extends AppCompatActivity {
    private TextView txv_rgb;
    public TextView txv_light;
    public TextView txv_proximity;
    private Button btn_color;

    private MqttAndroidClient client;
    private static final String SERVER_URI = "tcp://test.mosquitto.org:1883";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txv_rgb = (TextView) findViewById(R.id.txv_rgbValue);
        txv_light = (TextView) findViewById(R.id.txv_lightValue);
        txv_proximity = (TextView) findViewById(R.id.txv_proximityValue);
        btn_color = (Button) findViewById(R.id.btnColor);
        System.err.println("test1");
        connect();
        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    System.out.println("Reconnected to : " + serverURI);
                    // Re-subscribe as we lost it due to new session
                    subscribe("IoTLab");
                } else {
                    System.out.println("Connected to: " + serverURI);
                    subscribe("IoTLab");
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String newMessage = new String(message.getPayload());
                System.out.println("Incoming message: " + newMessage);
                /* add code here to interact with elements
                (text views, buttons)using data from newMessage*/
                System.err.println("Message arrived: " + newMessage);
                String[] messages = newMessage.split("/");
                System.out.println(messages[0]);
                txv_light.setText(messages[0]);
                txv_proximity.setText(messages[1]);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
    }


    private void connect() {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), SERVER_URI, clientId, Ack.AUTO_ACK);
        //try {
        System.err.println("test2");
        IMqttToken token = client.connect();
        System.err.println("test3");
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // We are connected
                Log.d(TAG, "onSuccess");
                System.out.println(TAG + "Success. Connected to " + SERVER_URI);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                // Something went wrong e.g. connection timeout or firewallproblems
                Log.d(TAG, "onFailure");
                System.out.println(TAG + "Oh no! Failed to connect to " + SERVER_URI);
            }
        });/*
        } catch (MqttException e) {
            e.printStackTrace();*/
    }


    private void subscribe(String topicToSubscribe) {
        final String topic = topicToSubscribe;
        int qos = 1;
        IMqttToken subToken = client.subscribe(topic, qos);
        subToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Subscription successful to topic: " + topic);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.out.println("Failed to subscribe to topic: " + topic);
                // The subscription could not be performed, maybe the user was not
                // authorized to subscribe on the specified topic e.g. using wildcards
            }
        });
    }
}
