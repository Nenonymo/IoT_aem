package com.example.iotlab3app.Groups;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iotlab3app.Connection.SQLstuff;
import com.example.iotlab3app.R;

import java.sql.SQLException;

public class new_actuator extends AppCompatActivity {

    EditText nickname, ID, location, type;
    Button addAct, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_actuator);

        nickname = (EditText)findViewById(R.id.new_actuator_nickname);
        ID = (EditText)findViewById(R.id.new_actuator_aID);
        location = (EditText)findViewById(R.id.new_actuator_location);
        type = (EditText)findViewById(R.id.new_actuator_type);

        addAct = (Button) findViewById(R.id.new_actuator_add);
        cancel = (Button) findViewById(R.id.new_actuator_cancel);

        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActuatorList.class);
            startActivity(intent);
        });

        addAct.setOnClickListener(v -> {
            try {
                SQLstuff.runSQL("CALL NewActuator(" + SQLstuff.getHouse() +
                        ", '" + nickname.getText() + "', '" + ID.getText() +
                        "', '" + location.getText() + "', '" + type.getText() + "');");

                Toast.makeText(this, "New actuator added", Toast.LENGTH_SHORT).show();

                cancel.callOnClick();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }
}