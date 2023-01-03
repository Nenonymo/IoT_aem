package com.example.iotlab3app.Groups;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.iotlab3app.Connection.SQLstuff;
import com.example.iotlab3app.R;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ActuatorList extends AppCompatActivity {

    String[] listItems;
    Map<Integer, String> itemMap;
    String[] mobileArray = {"Main Hall","Bedroom"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actuator_list);
        listItems = mobileArray;
        try {

            new checkActuators().execute();

            listItems = itemMap.values().toArray(listItems);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.activity_listview, listItems);

            ListView listView = (ListView) findViewById(R.id.actuator_list);
            listView.setAdapter(adapter);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }




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
                n = "On Internet Connection";
            }

            else {
                try {

                    try {
                        ResultSet items = SQLstuff.runSQL("CALL GetHouseActuatorsMin(5);");

                        assert items != null;
                        itemMap.put(items.getInt("Address"), items.getString("Nickname"));
                        System.out.println(itemMap.values());
                        while (items.next()){
                            itemMap.put(items.getInt("Address"), items.getString("Nickname"));
                        }
                        System.out.println("Rows: " + itemMap);
                    } catch (SQLException e) {
                        System.out.println(e);
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

        }
    }
}