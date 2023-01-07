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
    String[] listItems;
    Map<String, Boolean> actuatorONorOFF = new HashMap<>();
    Map<String, String> itemMap = new HashMap<>();
    String[] mobileArray = {
            "Main Hall","Hufflepuff commonroom",
            "Ravenclaw commonroom","Slytherin commonroom",
            "Gryffindoor commonroom", "3rd floor library"};


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = (TextView) findViewById(R.id.actuator_List_Title);
        setContentView(R.layout.activity_actuator_list);

        Button new_actuator = (Button) findViewById(R.id.new_actuator);
        new_actuator.setOnClickListener(v -> {
            Intent intent = new Intent(this, new_actuator.class);
            startActivity(intent);
        });

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
                    //Pistuff.actuatorOff(itemMap.get(item));
                    actuatorONorOFF.put(actuatorID, false);
                    System.out.println(item + " OFF: " + actuatorID);
                }
                if (Boolean.FALSE.equals(ONorOFF)){
                    //Pistuff.actuatorOn(itemMap.get(item));
                    actuatorONorOFF.put(actuatorID, true);
                    System.out.println(item + " ON: " + actuatorID);
                }
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
}