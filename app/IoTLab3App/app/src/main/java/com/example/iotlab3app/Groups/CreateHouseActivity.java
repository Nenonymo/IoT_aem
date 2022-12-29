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
import android.widget.ListView;

import com.example.iotlab3app.Connection.ConnectionClass;
import com.example.iotlab3app.Connection.SQLstuff;
import com.example.iotlab3app.Groups.CreateHouseActivity;
import com.example.iotlab3app.MainActivity;

import java.util.ArrayList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// A class for either loading an existing house or creating a new house

public class CreateHouseActivity extends AppCompatActivity {
    private Button createBtn, loadBtn;
    private EditText nickname, category;
    private ListView listView;

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

        listView = (ListView) findViewById(R.id.list_view);

        createBtn.setOnClickListener(v -> new checkHouse().execute("CreateHouse"));
        loadBtn.setOnClickListener(v -> new checkHouse().execute("LoadExistingHouse"));

    }

        private class checkHouse extends AsyncTask<String, String, String> {

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
                        System.out.println("USER_HOUSES: " + sql);
                    } else {
                        sql = "CALL NewHouseByUser ('" + userid + "', '" + nickname.getText() + "', '" + category.getText() + "');";
                        System.out.println("New House Created");
                    }
                    try {
                        rs = SQLstuff.runSQL(sql);
                        assert rs != null;
                        if(rs != null){
                            String firstrow = rs.getString("UserID") + "," + rs.getString("Permission") + "," + rs.getString("HouseID") + "," + rs.getString("Nickname") + "," + rs.getString("Category");
                            data.add(firstrow);
                            while (rs.next()) {
                                String row = rs.getString("UserID") + "," + rs.getString("Permission") + "," + rs.getString("HouseID") + "," + rs.getString("Nickname") + "," + rs.getString("Category");
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

        }




    }



}