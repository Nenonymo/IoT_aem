package com.example.iotlab3app.Connection;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class Pistuff {
    static String hostname = "192.168.1.137";
    static String username = "pi";
    static String password = "student123";

    public static String run (String command) {

        System.err.println("SSH test");
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection conn = new Connection(hostname);//init connection
            conn.connect();//start connection to the hostname
            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            Session sess = conn.openSession();
            sess.execCommand(command);
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            System.err.println("test2");
            //reads text
            String returnValue = null;
            while (true){String line = br.readLine();// read line
                if (line == null)
                    break;
                System.out.println(line);
                returnValue = line;

            }/* Show exit status, if available (otherwise "null") */
            System.out.println("ExitCode: " + sess.getExitStatus());
            sess.close();// Close this session
            conn.close();
            return returnValue;
        } catch (IOException e){
            e.printStackTrace(System.err);
            System.exit(2);
        }
        return null;
    }

    public static void actuatorOn(String aId){
        run("tdtool --on " + aId);
    }

    public static void actuatorOff(String aId){
        run("tdtool --off " + aId);
    }

    public static void allActuatorsOn(){
        run("bash AllOnOrOff on");
    }

    public static void allActuatorsOff(){
        run("bash AllOnOrOff off");
    }

    public static void fetchTempValues() {run("bash getTemp");}

    public static void setAllTempValues(String command){run("bash getTemp");}
    public static String getAllTempValues(){return allTempValues;}

    private static String allTempValues;
}
