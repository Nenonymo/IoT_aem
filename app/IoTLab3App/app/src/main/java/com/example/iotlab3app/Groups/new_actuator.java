package com.example.iotlab3app.Groups;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iotlab3app.Connection.ConnectionClass;
import com.example.iotlab3app.Connection.SQLstuff;
import com.example.iotlab3app.Login.LoginActivity;
import com.example.iotlab3app.R;

import java.sql.ResultSet;
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
            new add_new_actuator().execute();
        });

    }

    @SuppressLint("StaticFieldLeak")
    public class add_new_actuator extends AsyncTask<String, String, String> {
        String z = null;
        Boolean isSuccess = false;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                if(nickname.getText().length() > 0 && location.getText().length() > 0 && ID.getText().length() > 0 && type.getText().length() > 0){
                    ResultSet rs = SQLstuff.runSQL("CALL NewActuator(" + SQLstuff.getHouse() + ", '" + nickname.getText() + "', '" + ID.getText() + "', '" + location.getText() + "', '" + type.getText() + "');");

                    Toast.makeText(new_actuator.this, "New actuator added", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(new_actuator.this, ActuatorList.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(new_actuator.this, "Please insert text in all fields", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            return z;
        }
    }


}