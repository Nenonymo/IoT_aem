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

public class LoadHouseUsersActivity extends AppCompatActivity {
    Spinner user_spinner;
    Button loadUsersBtn, deleteUserBtn, addUserBtn;
    EditText user_id, user_permission;

    private static String pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_house_users);

        user_spinner = (Spinner) findViewById(R.id.user_spinner);
        loadUsersBtn = (Button) findViewById(R.id.loadUsersBtn);
        addUserBtn = (Button) findViewById(R.id.addUserBtn);
        deleteUserBtn = (Button) findViewById(R.id.deleteUserBtn);

        user_id = (EditText) findViewById(R.id.user_id);
        user_permission = (EditText) findViewById(R.id.user_permission);

        loadUsersBtn.setOnClickListener(v -> new LoadHouseUsersActivity.checkUsers().execute("LoadUsers"));
        addUserBtn.setOnClickListener(v -> new LoadHouseUsersActivity.checkUsers().execute("AddUser"));
        deleteUserBtn.setOnClickListener(v -> new LoadHouseUsersActivity.checkUsers().execute("DeleteUser"));


    }

    public void renderSpinner(List<String> data) {

        System.out.println("renderSpinner method called!");
        System.out.println("Data: " + data);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println("List count: " + adapter.getCount());
        user_spinner.setAdapter(adapter);
        System.out.println("adapter set");

        user_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = parent.getItemAtPosition(position).toString();
                String[] posArray = pos.split(" ");
                String idpos = posArray[1];
                System.out.println("GroupID: " + idpos);
                SQLstuff.setSelectedUser(idpos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private class checkUsers extends AsyncTask<String, String, String> {

        String n = null;
        Boolean onSuccess = false;
        Toast toast = null;

        List<String> userData = new ArrayList<>();

        @Override
        protected void onPreExecute() {

            toast = Toast.makeText(LoadHouseUsersActivity.this,"Check Internet Connection",Toast.LENGTH_LONG);

        }

        @Override
        protected String doInBackground(String... strings) {

            if(SQLstuff.getCon() == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoadHouseUsersActivity.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                n = "On Internet Connection";
            }

            else {
                String userOptions = strings[0];
                String sql = "";
                SQLException error = null;
                String toastText = "";
                ResultSet rs = null;

                try {

                    String houseid = SQLstuff.getHouse();
                    String selectedUserID = SQLstuff.getSelectedUserID();
                    System.out.println("HouseID is: " + houseid);

                    if (userOptions.equals("LoadUsers")) {
                        sql = "CALL GetHouseUsers('" + houseid + "');";
                        System.out.println("House's Users Loaded");
                    } else if (userOptions.equals("AddUser")){
                        int permission = 0;
                        if (user_id.getText().length() > 0 && user_permission.getText().length() > 0){
                            permission = Integer.parseInt(String.valueOf(user_permission.getText()));
                        } if(permission >= 1 && permission <= 4){
                            sql = "CALL AddUserToHouse('" + user_id.getText() + "', '" + houseid + "', '" + user_permission.getText() + "');";
                            user_id.setText("");
                            user_permission.setText("");
                            System.out.println("New User Added to House");
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoadHouseUsersActivity.this,"Please enter a userID and a permission from 1-4",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else{
                        // ** WILL NEED TO CHECK ADMIN CREDENTIALS BELOW, CHANGING LATER! **
                        String userid = SQLstuff.getUsername();
                        System.out.println("Variable groupid: " + userid);
                        sql = "CALL RemoveHouseUser('" + selectedUserID + "', '" + userid + "', '" + houseid + "');";
                    }
                    try {
                        rs = SQLstuff.runSQL(sql);
                        assert rs != null;
                        if(rs != null){
                            String firstRow = "UserID: " + rs.getString("UserID") + " " + "Permission: " + rs.getString("Permission");
                            userData.add(firstRow);
                            while (rs.next()) {
                                String row = "UserID: " + rs.getString("UserID") + " " + "Permission: " + rs.getString("Permission");
                                userData.add(row);
                            }
                            System.out.println("Rows: " + userData);
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
            renderSpinner(userData);


        }




    }



}
