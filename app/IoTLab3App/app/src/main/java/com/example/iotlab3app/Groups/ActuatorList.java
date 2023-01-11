package com.example.iotlab3app.Groups;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iotlab3app.Connection.ConnectionClass;
import com.example.iotlab3app.Connection.Pistuff;
import com.example.iotlab3app.Connection.SQLstuff;
import com.example.iotlab3app.Login.LoginActivity;
import com.example.iotlab3app.Login.ResetPassActivity;
import com.example.iotlab3app.R;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActuatorList extends AppCompatActivity {

    TextView title;
    Spinner group_spinner;

    private static String pos;


    Button new_actuator, turn_on, turn_off, load_group_actuators;

    String[] listItems;
    Map<String, Boolean> actuatorONorOFF = new HashMap<>();
    Map<String, String> itemMap = new HashMap<>();
    String[] mobileArray = {};


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = (TextView) findViewById(R.id.actuator_List_Title);
        setContentView(R.layout.activity_actuator_list);

        group_spinner = (Spinner) findViewById(R.id.group_spinner);

        turn_on = (Button) findViewById(R.id.turn_on);
        turn_off = (Button) findViewById(R.id.turn_off);
        load_group_actuators = (Button) findViewById(R.id.load_group_actuators);

        new_actuator = (Button) findViewById(R.id.new_actuator);
        new_actuator.setOnClickListener(v -> {
            Intent intent = new Intent(this, new_actuator.class);
            startActivity(intent);
        });

        load_group_actuators.setOnClickListener(v -> new checkGroups().execute("LoadExistingGroups"));

        turn_on.setOnClickListener(v -> turnOnOrOff("On"));
        turn_off.setOnClickListener(v -> turnOnOrOff("Off"));

        listItems = mobileArray;
        try {
            System.out.println("before async");
            new checkActuators().execute();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void fillList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, listItems);

        ListView listView = (ListView) findViewById(R.id.actuator_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);
                String actuatorID = itemMap.get(item);
                Boolean ONorOFF = actuatorONorOFF.get(actuatorID);
                if (Boolean.TRUE.equals(ONorOFF)){
                    Pistuff.actuatorOff(itemMap.get(item));
                    actuatorONorOFF.put(actuatorID, false);
                    System.out.println(item + " OFF: " + actuatorID);
                }
                if (Boolean.FALSE.equals(ONorOFF)){
                    Pistuff.actuatorOn(itemMap.get(item));
                    actuatorONorOFF.put(actuatorID, true);
                    System.out.println(item + " ON: " + actuatorID);
                }
            }
        });
    }

    public void turnOnOrOff (String input){
        ResultSet rs;
        String sql;

        List<String> groupData = new ArrayList<>();

        String groupid = SQLstuff.getGroup();
        sql = "CALL GetGroupMembers('" + groupid + "');";
        System.out.println("House's Groups Loaded");

        try {
            rs = SQLstuff.runSQL(sql);
            assert rs != null;
            if(rs != null){
                String firstRow = rs.getString(1);
                groupData.add(firstRow);
                while (rs.next()) {
                    String row = rs.getString(1);
                    groupData.add(row);
                }
                System.out.println(groupData);
            }
            else {
                System.out.println("Empty");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        if(input.equals("On")){

            Pistuff.run("bash actuate.sh -v 1 " + String.join(" ", groupData));
            System.out.println(String.join(" ", groupData));

        } else {
            Pistuff.run("bash actuate.sh -v 0 " + String.join(" ", groupData));
            System.out.println(String.join(" ", groupData));

        }

    }

    public void renderSpinner(List<String> data) {

        System.out.println("renderSpinner method called!");
        System.out.println("Data: " + data);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println("List count: " + adapter.getCount());
        group_spinner.setAdapter(adapter);
        System.out.println("adapter set");

        group_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = parent.getItemAtPosition(position).toString();
                String[] posArray = pos.split(" ");
                String idpos = posArray[1];
                System.out.println("GroupID: " + idpos);
                SQLstuff.setGroup(idpos);
                SQLstuff.setAllGroupInfo(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private class checkActuators extends AsyncTask<String, String, String> {

        String n = null;
        Boolean onSuccess = false;
        List<String> data = new ArrayList<>();

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {

            if(SQLstuff.getCon() == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActuatorList.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                while(SQLstuff.getCon() == null){
                    SQLstuff.setCon(SQLstuff.connectionClass(ConnectionClass.un.toString(),ConnectionClass.pass.toString(),ConnectionClass.db.toString(),ConnectionClass.ip.toString()));
                }
                n = "On Internet Connection";
            }
            try {

                try {
                    ResultSet items = SQLstuff.runSQL("CALL GetHouseActuatorsMin(" + SQLstuff.getHouse() + ");");
                    System.out.println("after CALL");
                    System.out.println("1: " + items.getInt(2));
                    System.out.println("2: " + items.getString(3));

                    System.out.println(items.toString());

                    if (items == null){
                        System.out.println("bruh");
                    }
                    assert items != null;
                    itemMap.put(items.getString(3), items.getString(2));
                    actuatorONorOFF.put(items.getString(2), false);
                    System.out.println(itemMap.values());
                    while (items.next()){
                        itemMap.put(items.getString(3), items.getString(2));
                        actuatorONorOFF.put(items.getString(2), false);
                        System.out.println("In While");
                    }
                    listItems = itemMap.keySet().toArray(new String[0]);
                    System.out.println("Rows: " + listItems);
                } catch (SQLException e) {
                    System.out.println(e);
                }

            }catch (Exception e){
                onSuccess = false;
                Log.e("SQL Error : ", e.getMessage());
            }

            return n;
        }

        @Override
        protected void onPostExecute(String s) {
            fillList();
        }
    }

    private class checkGroups extends AsyncTask<String, String, String> {

        String n = null;
        Boolean onSuccess = false;
        Toast toast = null;

        List<String> groupData = new ArrayList<>();

        @Override
        protected void onPreExecute() {

            toast = Toast.makeText(ActuatorList.this,"Check Internet Connection",Toast.LENGTH_LONG);

        }

        @Override
        protected String doInBackground(String... strings) {

            if(SQLstuff.getCon() == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActuatorList.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                n = "On Internet Connection";
            }

            else {
                String loadExistingGroups = strings[0];
                String sql = "";
                SQLException error = null;
                String toastText = "";
                ResultSet rs = null;

                try {

                    String houseid = SQLstuff.getHouse();
                    System.out.println("HouseID is: " + houseid);

                    if (loadExistingGroups.equals("LoadExistingGroups")) {
                        sql = "CALL GetHouseGroups('" + houseid + "');";
                        System.out.println("House's Groups Loaded");
                    }  else{
                        System.out.println("Alt 2");
                    }
                    try {
                        rs = SQLstuff.runSQL(sql);
                        assert rs != null;
                        if(rs != null){
                            String firstRow = "GroupID: " + rs.getString("GroupID") + " " + "Nickname: " + rs.getString("Nickname");
                            groupData.add(firstRow);
                            while (rs.next()) {
                                String row = "GroupID: " + rs.getString("GroupID") + " " + "Nickname: " + rs.getString("Nickname");
                                groupData.add(row);
                            }
                            System.out.println("Rows: " + groupData);
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
            renderSpinner(groupData);

        }




    }
}