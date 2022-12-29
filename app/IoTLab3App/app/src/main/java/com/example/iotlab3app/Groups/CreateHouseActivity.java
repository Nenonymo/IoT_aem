package com.example.iotlab3app.Groups;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iotlab3app.Login.LoginActivity;
import com.example.iotlab3app.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iotlab3app.Connection.ConnectionClass;
import com.example.iotlab3app.Connection.SQLstuff;
import com.example.iotlab3app.Groups.CreateHouseActivity;
import com.example.iotlab3app.MainActivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// A class for either loading an existing house or creating a new house

public class CreateHouseActivity extends AppCompatActivity {
    private Button createBtn, loadBtn;
    private EditText nickname, category;

    String usernameValue = LoginActivity.username;
    public static String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_house);

        nickname = (EditText) findViewById(R.id.nickname);
        category = (EditText) findViewById(R.id.category);

        createBtn = (Button) findViewById(R.id.createBtn);
        loadBtn = (Button) findViewById(R.id.loadBtn);

        createBtn.setOnClickListener(v -> new checkHouse().execute("CreateHouse"));
        loadBtn.setOnClickListener(v -> new checkHouse().execute("LoadExistingHouse"));

    }

        private class checkHouse extends AsyncTask<String, String, String> {

            String n = null;
            Boolean onSuccess = false;

        @Override
            protected void onPreExecute() {

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
                String sql2 = "";
                SQLException error = null;
                String toastText = "";
                ResultSet rs = null;

                try {
                    userid = usernameValue;

                    if (loadExistingHouse.equals("LoadExistingHouse")) {
                        sql = "CALL GetUserHouses ('" + userid + "');";
                        System.out.println("USER_ID: " + userid);
                    } else {
                        sql = "CALL NewHouseByUser ('" + userid + "', '" + nickname.getText() + "', '" + category.getText() + "');";
                        System.out.println("New House Created");
                    }
                    try {
                        rs = SQLstuff.runSQL(sql);
                        rs = SQLstuff.runSQL(sql);
                        System.out.println("USER_ID: " + userid);
                        System.out.println("USER_HOUSES: " + rs);
                        System.out.println("USER_NEW_HOUSE: " + rs);
                        assert rs != null;
                        if(rs != null){
                            String uid = rs.getString("user_id");
                            System.out.println("USERID: " + uid);
                        }
                        else {
                            System.out.println("Empty");
                        }
                        String uid = rs.getString("user_id");
                        System.out.println("USERID: " + uid);
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

        }




    }



}