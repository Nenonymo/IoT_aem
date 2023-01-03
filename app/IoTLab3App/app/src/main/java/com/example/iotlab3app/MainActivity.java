package com.example.iotlab3app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

//import org.eclipse.paho.android.service.MqttAndroidClient;
//import com.example.iotlab3app.Connection.login.LoginActivity;

import com.example.iotlab3app.Connection.Pistuff;
import com.example.iotlab3app.Connection.SQLstuff;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {
    String command2 = null;
    TextView txv_temp_indoor = null;
    Switch lightToggle= null;
    Button btnUpdateTemp = null;
    TextView switchtext = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txv_temp_indoor = (TextView) findViewById(R.id.indoorTempShow);
        txv_temp_indoor.setText("the fetched indoor temp value");

        switchtext = (TextView) findViewById(R.id.outdoorLightShow);

        lightToggle= (Switch) findViewById(R.id.btnToggle);
        lightToggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // below you write code to change switch status and action to take
                        if (isChecked) { //do something if checked
                            //command2 = "python3 Lab2/turnOn.py";
                            switchtext.setText("On");
                            switchtext.setTextColor(Color.GREEN);
                            Pistuff.allActuatorsOn();
                        } else {    // to do something if not checked
                            //command2 = "python3 Lab2/turnOff.py";
                            switchtext.setText("Off");
                            switchtext.setTextColor(Color.RED);
                            Pistuff.allActuatorsOff();
                        }
                    }});

        btnUpdateTemp = (Button) findViewById(R.id.btnUpdateTemp);
        btnUpdateTemp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txv_temp_indoor.setText(Pistuff.run("python getTemp.py"));
            }
        });
    }
}
