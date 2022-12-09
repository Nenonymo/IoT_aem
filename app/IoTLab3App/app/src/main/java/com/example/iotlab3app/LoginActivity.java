package com.example.iotlab3app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iotlab3app.Connection.ConnectionClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    EditText usernamelogin,passwordlogin;
    Button loginbtn,regbtn;

    Connection con;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernamelogin = (EditText)findViewById(R.id.usernamelogin);
        passwordlogin = (EditText)findViewById(R.id.passwordlogin);
        loginbtn = (Button)findViewById(R.id.loginbtn);
        regbtn = (Button)findViewById(R.id.regbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginActivity.checkLogin().execute("Login");
            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginActivity.checkLogin().execute("Register");
                /*Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();*/
            }
        });
    }

    public class checkLogin extends AsyncTask<String, String, String> {

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
            con = connectionClass(ConnectionClass.un.toString(),ConnectionClass.pass.toString(),ConnectionClass.db.toString(),ConnectionClass.ip.toString());
            if(con == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                z = "On Internet Connection";
            }
            else {
                try {
                    String sql = "";
                    String toastText = "";
                    if(strings[0].equals("Login")) {
                        sql = "CALL TestLogin ('" + usernamelogin.getText() + "', '" + passwordlogin.getText() + "');";
                        toastText="Login Success";
                    }else {
                        sql = "CALL NewUser ('" + usernamelogin.getText() + "', '" + passwordlogin.getText() + "');";
                    }
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    rs.first();
                    if (rs.get){
                        Toast.makeText(LoginActivity.this, "New User Created", Toast.LENGTH_SHORT).show();
                    }

                    System.err.println("SQL Result: " + rs.getBoolean("ValidAuth"));

                    if (rs.getBoolean("ValidAuth")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                            }
                        });
                        z = "Success";

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Check email or password", Toast.LENGTH_LONG).show();
                            }
                        });

                        usernamelogin.setText("");
                        passwordlogin.setText("");
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    Log.e("SQL Error : ", e.getMessage());
                }
            }
            return z;
        }
    }

    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connectionURL = "jdbc:mysql://" + server+"/" + database/* + ";user=" + user + ";password=" + password + ";"*/;

            connection = DriverManager.getConnection(connectionURL, user, password);
        }catch (Exception e){
            Log.e("SQL Connection Error : ", e.getMessage());
        }

        return connection;
    }
}