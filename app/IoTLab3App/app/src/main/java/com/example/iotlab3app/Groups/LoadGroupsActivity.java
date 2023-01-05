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

// A class that manages all groups that belongs to a house with CRD operations

public class LoadGroupsActivity extends AppCompatActivity {

    TextView textview;
    Button loadGroupsBtn, createGroupBtn, deleteGroupBtn, goToGroupBtn;
    Spinner group_spinner;
    EditText nickname, description;

    private static String pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_groups);

        group_spinner = (Spinner) findViewById(R.id.group_spinner);
        loadGroupsBtn = (Button) findViewById(R.id.loadGroupsBtn);
        createGroupBtn = (Button) findViewById(R.id.createGroupBtn);
        deleteGroupBtn = (Button) findViewById(R.id.deleteGroupBtn);
        goToGroupBtn = (Button) findViewById(R.id.goToGroupBtn);

        nickname = (EditText) findViewById(R.id.nickname);
        description = (EditText) findViewById(R.id.description);

        createGroupBtn.setOnClickListener(v -> new checkGroups().execute("CreateGroup"));
        loadGroupsBtn.setOnClickListener(v -> new checkGroups().execute("LoadExistingGroups"));
        deleteGroupBtn.setOnClickListener(v -> new checkGroups().execute("DeleteSelectedGroup"));

        goToGroupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoadGroupsActivity.this, ViewGroupActivity.class);
            startActivity(intent);
        });


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

    private class checkGroups extends AsyncTask<String, String, String> {

        String n = null;
        Boolean onSuccess = false;
        Toast toast = null;

        List<String> groupData = new ArrayList<>();

        @Override
        protected void onPreExecute() {

            toast = Toast.makeText(LoadGroupsActivity.this,"Check Internet Connection",Toast.LENGTH_LONG);

        }

        @Override
        protected String doInBackground(String... strings) {

            if(SQLstuff.getCon() == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoadGroupsActivity.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
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
                    } else if (loadExistingGroups.equals("CreateGroup")){
                        if(nickname.getText().length() > 0 && description.getText().length() > 0) {
                            sql = "CALL NewGroup('" + nickname.getText() + "', '" + houseid + "', '" + description.getText() + "');";
                            nickname.setText("");
                            description.setText("");
                            System.out.println("New Group Created");
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoadGroupsActivity.this,"Please enter a nickname and a description",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else{
                        String groupid = SQLstuff.getGroup();
                        System.out.println("Variable groupid: " + groupid);
                        sql = "CALL RemoveGroup('" + groupid + "');";
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
