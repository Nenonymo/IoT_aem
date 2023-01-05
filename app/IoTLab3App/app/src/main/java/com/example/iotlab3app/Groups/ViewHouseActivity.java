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


import java.util.ArrayList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;


// A class for two buttons to go to either Actuators page or
// to Groups page

public class ViewHouseActivity extends AppCompatActivity {
    TextView textview;
    Button groupsBtn, actuatorsBtn;

    String house = CreateHouseActivity.pos;
    String houseDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house);

        textview = (TextView) findViewById(R.id.text_view);

        groupsBtn = (Button) findViewById(R.id.groupsBtn);
        actuatorsBtn = (Button) findViewById(R.id.actuatorsBtn);

        actuatorsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ViewHouseActivity.this, ActuatorList.class);
            startActivity(intent);
        });

        groupsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ViewHouseActivity.this, LoadGroupsActivity.class);
            startActivity(intent);
        });


        textview.setText(house);


    }
}
