package com.example.iotlab3app.Groups;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iotlab3app.Login.LoginActivity;
import com.example.iotlab3app.R;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.TextView;


import com.example.iotlab3app.Connection.SQLstuff;


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;


// A class that manages a specific group that the user chooses

public class ViewGroupActivity extends AppCompatActivity{

    TextView textview;
    Button loadGroupActuatorsBtn, loadHouseActuatorsBtn, deleteActuatorBtn, addActuatorBtn, deleteHouseActBtn;
    Spinner group_actuator_spinner, house_actuator_spinner;

    String group = SQLstuff.getGroup();
    String groupInfo = SQLstuff.getAllGroupInfo();

    private static String pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        textview = (TextView) findViewById(R.id.text_view_group);
        group_actuator_spinner = (Spinner) findViewById(R.id.group_actuator_spinner);
        house_actuator_spinner = (Spinner) findViewById(R.id.house_actuator_spinner);


        loadGroupActuatorsBtn = (Button) findViewById(R.id.loadGroupActuatorsBtn);
        loadHouseActuatorsBtn = (Button) findViewById(R.id.loadHouseActuatorsBtn);
        deleteActuatorBtn = (Button) findViewById(R.id.deleteActuatorBtn);
        addActuatorBtn = (Button) findViewById(R.id.addActuatorBtn);
        deleteHouseActBtn = (Button) findViewById(R.id.deleteHouseActBtn);

        textview.setText(groupInfo);

        loadGroupActuatorsBtn.setOnClickListener(v -> new checkGroupActuators().execute("LoadGroupActuators"));
        deleteActuatorBtn.setOnClickListener(v -> new checkGroupActuators().execute("DeleteActuator"));
        loadHouseActuatorsBtn.setOnClickListener(v -> new checkGroupActuators().execute("LoadHouseActuators"));
        addActuatorBtn.setOnClickListener(v -> new checkGroupActuators().execute("AddActuator"));
        deleteHouseActBtn.setOnClickListener(v -> new checkGroupActuators().execute("DeleteHouseActuator"));

    }

    public void renderGroupActuatorSpinner(List<String> data) {

        System.out.println("renderSpinner method called!");
        System.out.println("Data: " + data);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println("List count: " + adapter.getCount());
        group_actuator_spinner.setAdapter(adapter);
        System.out.println("adapter set");

        group_actuator_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = parent.getItemAtPosition(position).toString();
                String[] posArray = pos.split(" ");
                String idpos = posArray[1];
                System.out.println("ActID: " + idpos);
                SQLstuff.setGroupActuator(idpos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void renderHouseActuatorSpinner(List<String> data) {

        System.out.println("renderSpinner method called!");
        System.out.println("Data: " + data);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println("List count: " + adapter.getCount());
        house_actuator_spinner.setAdapter(adapter);
        System.out.println("adapter set");

        house_actuator_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = parent.getItemAtPosition(position).toString();
                String[] posArray = pos.split(" ");
                String idpos = posArray[1];
                System.out.println("ActID: " + idpos);
                SQLstuff.setAddHouseActuator(idpos);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private class checkGroupActuators extends AsyncTask<String, String, String> {

        String n = null;
        Boolean onSuccess = false;
        Toast toast = null;
        String actuatorOption;


        List<String> groupActuatorData = new ArrayList<>();

        @Override
        protected void onPreExecute() {

            toast = Toast.makeText(ViewGroupActivity.this,"Check Internet Connection",Toast.LENGTH_LONG);

        }

        @Override
        protected String doInBackground(String... strings) {

            if(SQLstuff.getCon() == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ViewGroupActivity.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                n = "On Internet Connection";
            }

            else {
                actuatorOption = strings[0];
                String sql = "";
                SQLException error = null;
                String toastText = "";
                ResultSet rs = null;

                try {

                    String houseid = SQLstuff.getHouse();
                    String groupid = SQLstuff.getGroup();
                    String actid = SQLstuff.getGroupActuator();
                    String actAddID = SQLstuff.getAddHouseActuator();

                    if (actuatorOption.equals("LoadGroupActuators")) {
                        sql = "CALL GetGroupMembers('" + groupid + "');";
                        System.out.println("House's GroupActuators Loaded");
                    } else if (actuatorOption.equals("AddActuator")){
                        if(actAddID != null) {
                            System.out.println("gruppid: " + groupid + "actid: " + actAddID);
                            sql = "CALL AddActuatorToGroup('" + groupid + "', '" + actAddID + "');";
                            System.out.println("New Actuator Added To Group");
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ViewGroupActivity.this,"Please select a house actuator!",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else if (actuatorOption.equals("LoadHouseActuators")) {
                        sql = "CALL GetHouseActuators('" + houseid + "');";
                        System.out.println("House's Actuators Loaded");
                    }

                    else if (actuatorOption.equals("DeleteActuator")){

                        sql = "CALL RemoveActuatorFromGroup('" + groupid + "', '" + actid + "');";
                        System.out.println("here: " + actid);
                    }
                    else if(actuatorOption.equals("DeleteHouseActuator")){
                        sql = "CALL RemoveActuator('" + actAddID + "');";
                    }
                    try {
                        rs = SQLstuff.runSQL(sql);
                        assert rs != null;
                        if(rs != null){
                            String firstRow = "ActID: " + rs.getString("Actuator.ActID") + " " + "Nickname: " + rs.getString("Nickname") + " " + "Address: " + rs.getString("Address");
                            groupActuatorData.add(firstRow);
                            while (rs.next()) {
                                String row = "ActID: " + rs.getString("Actuator.ActID") + " " + "Nickname: " + rs.getString("Nickname") + " " + "Address: " + rs.getString("Address");
                                groupActuatorData.add(row);
                            }
                            System.out.println("Rows: " + groupActuatorData);
                        }
                        else {
                            System.out.println("Empty");
                        }
                    } catch (SQLException e) {
                        error = e;
                        System.out.println(error);
                    }

                }catch (Exception e){
                    onSuccess = false;
                    Log.e("SQL Error : ", e.getMessage());
                }

            }

            return n;
        }

        @Override
        protected void onPostExecute(String s) {
            if(actuatorOption.equals("LoadGroupActuators")) {
                renderGroupActuatorSpinner(groupActuatorData);
            } else if(actuatorOption.equals("LoadHouseActuators")) {
                renderHouseActuatorSpinner(groupActuatorData);
            } else if(actuatorOption.equals("DeleteActuator") || actuatorOption.equals("AddActuator") || actuatorOption.equals("DeleteHouseActuator") ){
                group_actuator_spinner.setAdapter(null);
                house_actuator_spinner.setAdapter(null);
            }
        }




    }



}
