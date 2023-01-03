package com.example.iotlab3app.Groups;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.iotlab3app.Connection.SQLstuff;
import com.example.iotlab3app.R;

import java.util.ArrayList;

public class ActuatorList extends AppCompatActivity {

    ArrayList<String> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLstuff.runSQL("CALL GetHouse");

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, listItems);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
    }
}