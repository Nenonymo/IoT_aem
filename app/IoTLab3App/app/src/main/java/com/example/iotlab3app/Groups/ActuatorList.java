package com.example.iotlab3app.Groups;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.iotlab3app.Connection.SQLstuff;
import com.example.iotlab3app.R;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
            ResultSet items = SQLstuff.runSQL("CALL GetHouseActuatorsMin("+ SQLstuff.getHouse()+ ");");

            assert items != null;
            String[] nickNameArray = (String[]) items.getString("Nickname");
            Integer[] aidArray = (Integer[]) items.getArray("Address").getArray();

            System.out.println("nickNameArray = " + Arrays.toString(nickNameArray));
            System.out.println("aidArray = " + Arrays.toString(aidArray));

            for (int i = 0; i < nickNameArray.length; i++) {
                itemMap.put(aidArray[i], nickNameArray[i]);
            }

            listItems = itemMap.values().toArray(listItems);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, listItems);

        ListView listView = (ListView) findViewById(R.id.actuator_list);
        listView.setAdapter(adapter);

    }
}