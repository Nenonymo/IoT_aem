package com.example.iotlab3app.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iotlab3app.Connection.ConnectionClass;
import com.example.iotlab3app.Connection.Pistuff;
import com.example.iotlab3app.Connection.SQLstuff;
import com.example.iotlab3app.Groups.ActuatorList;
import com.example.iotlab3app.Groups.CreateHouseActivity;
import com.example.iotlab3app.MainActivity;
import com.example.iotlab3app.R;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    private EditText usernamelogin,passwordlogin;
    private Button loginbtn,regbtn, resPass;
    public static String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernamelogin = (EditText)findViewById(R.id.usernamelogin);
        passwordlogin = (EditText)findViewById(R.id.passwordlogin);
        loginbtn = (Button)findViewById(R.id.loginbtn);
        regbtn = (Button)findViewById(R.id.regbtn);
        resPass = (Button)findViewById(R.id.resPassBtn);

        SQLstuff.setCon(SQLstuff.connectionClass(ConnectionClass.un.toString(),ConnectionClass.pass.toString(),ConnectionClass.db.toString(),ConnectionClass.ip.toString()));

        loginbtn.setOnClickListener(v -> new checkLogin().execute("Login"));

        regbtn.setOnClickListener(v -> {
            new checkLogin().execute("Register");
            /*Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();*/
        });

        resPass.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPassActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class checkLogin extends AsyncTask<String, String, String> {
        String z = null;
        Boolean isSuccess = false;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

        }

        @Override
        protected String doInBackground(String... strings) {

            if(SQLstuff.getCon() == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                while(SQLstuff.getCon() == null){
                    SQLstuff.setCon(SQLstuff.connectionClass(ConnectionClass.un.toString(),ConnectionClass.pass.toString(),ConnectionClass.db.toString(),ConnectionClass.ip.toString()));
                }
                z = "On Internet Connection";
            }
            else {
                try {
                    String login = strings[0];
                    String sql = "";
                    SQLException error = null;
                    String toastText = "";
                    ResultSet rs = null;
                    if (login.equals("Login")) {
                        sql = "CALL TestLogin ('" + usernamelogin.getText() + "', '" + passwordlogin.getText() + "');";
                    } else {
                        sql = "CALL NewUser ('" + usernamelogin.getText() + "', '" + passwordlogin.getText() + "');";
                    }
                    try {
                        rs = SQLstuff.runSQL(sql);
                    } catch (SQLException e) {
                        error = e;
                    }

                    if (error != null){
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "User already exist", Toast.LENGTH_SHORT).show());
                    }else if (!login.equals("Login")) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "New User Created", Toast.LENGTH_SHORT).show());
                    } else {
                        assert rs != null;
                        if (rs.getBoolean("ValidAuth")) {
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show());
                            SQLstuff.setUsername(usernamelogin.getText().toString());
                            Intent intent = new Intent(LoginActivity.this, CreateHouseActivity.class);
                            startActivity(intent);
                            z = "Success";
                            username = usernamelogin.getText().toString();

                        } else {
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Check email or password", Toast.LENGTH_LONG).show());

                            z = "Failed";
                            usernamelogin.setText("");
                        }
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    Log.e("SQL Error : ", e.getMessage());
                }
            }

            return z;
        }
    }


}