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


import com.example.iotlab3app.Connection.SQLstuff;


import java.util.ArrayList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;


// A class for either loading an existing house or creating a new house

public class CreateHouseActivity extends AppCompatActivity {
    Button createBtn, loadBtn, goToHouseBtn;
    private EditText nickname, category;
    private Spinner houseSpinner;
    public static List<String> data = new ArrayList<>();

    String usernameValue = SQLstuff.getUsername();
    public static String userid;
    public static String pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_house);

        nickname = (EditText) findViewById(R.id.nickname);
        category = (EditText) findViewById(R.id.category);

        createBtn = (Button) findViewById(R.id.createBtn);
        loadBtn = (Button) findViewById(R.id.loadBtn);
        goToHouseBtn = (Button) findViewById(R.id.goToHouseBtn);

        createBtn.setOnClickListener(v -> new checkHouse().execute("CreateHouse"));
        loadBtn.setOnClickListener(v -> new checkHouse().execute("LoadExistingHouse"));
        goToHouseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CreateHouseActivity.this, ViewHouseActivity.class);
            startActivity(intent);
        });

        houseSpinner = (Spinner) findViewById(R.id.houseSpinner);

    }

        public void renderSpinner(List<String> data) {

        System.out.println("renderSpinner method called!");
        System.out.println("Data: " + data);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            System.out.println(adapter.getCount());
            houseSpinner.setAdapter(adapter);
            System.out.println("adapter set");

            houseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pos = parent.getItemAtPosition(position).toString();
                    String[] posArray = pos.split(" ");
                    String idpos = posArray[1];
                    System.out.println("idpos: " + posArray[1]);
                    SQLstuff.setHouse(idpos);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        private class checkHouse extends AsyncTask<String, String, String> {

            String n = null;
            Boolean onSuccess = false;
            Toast toast = null;

        @Override
            protected void onPreExecute() {

            toast = Toast.makeText(CreateHouseActivity.this,"Check Internet Connection",Toast.LENGTH_LONG);

        }

        @Override
            protected String doInBackground(String... strings) {

            if(SQLstuff.getCon() == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CreateHouseActivity.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                n = "On Internet Connection";
            }

            else {
                String loadExistingHouse = strings[0];
                String sql = "";
                SQLException error = null;
                String toastText = "";
                ResultSet rs = null;

                try {
                    userid = usernameValue;

                    if (loadExistingHouse.equals("LoadExistingHouse")) {
                        sql = "CALL GetUserHouses ('" + userid + "');";
                        System.out.println("User's Houses Loaded");
                    } else {
                        sql = "CALL NewHouseByUser ('" + userid + "', '" + nickname.getText() + "', '" + category.getText() + "');";
                        System.out.println("New House Created");
                    }
                    try {
                        rs = SQLstuff.runSQL(sql);
                        assert rs != null;
                        if(rs != null){
                            String firstRow = "HouseID: " + rs.getString("HouseID") + " " + "Nickname: " + rs.getString("Nickname");
                            data.add(firstRow);
                            while (rs.next()) {
                                String row = "HouseID: " + rs.getString("HouseID") + " " + "Nickname: " + rs.getString("Nickname");
                                data.add(row);
                            }
                            System.out.println("Rows: " + data);
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
            renderSpinner(data);


        }




    }



}