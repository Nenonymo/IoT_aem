package com.example.iotlab3app.Connection;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLstuff {


    public static Connection getCon() {
        return con;
    }

    public static void setCon(Connection con) {
        SQLstuff.con = con;
    }

    @SuppressLint("NewApi")
    public static Connection connectionClass(String user, String password, String database, String server){
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

    @Nullable
    public static ResultSet runSQL(String sql) throws SQLException {
        ResultSet rs = null;
        Statement stmt = SQLstuff.getCon().createStatement();
        rs = stmt.executeQuery(sql);
        rs.first();

        return rs;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SQLstuff.username = username;
    }

    public static String getHouse() {
        return house;
    }

    public static void setHouse(String house) {
        SQLstuff.house = house;
    }

    private static Connection con;
    private static String username;
    private static String house = "5"; // TODO: REMOVE DEFAULT VALUE;
    private static String houseName = "Hogwarts"; // TODO: REMOVE DEFAULT VALUE;

}
