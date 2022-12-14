package com.example.iotlab3app.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iotlab3app.Connection.SQLstuff;
import com.example.iotlab3app.R;

import java.sql.SQLException;

public class ResetPassActivity extends AppCompatActivity {
    private EditText userName, newPass;
    private Button   cancelBtn, resBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        userName = (EditText)findViewById(R.id.userName);
        newPass = (EditText) findViewById(R.id.newPass);
        cancelBtn = (Button) findViewById(R.id.cancel_button);
        resBtn = (Button) findViewById(R.id.resPassBtn);

        cancelBtn.setOnClickListener(v -> finish());

        resBtn.setOnClickListener(v -> { //TODO: SQL Code for resetting password.
            try {
                SQLstuff.runSQL("CALL ChangeUserPassword ('" + userName.getText() + "', '" + newPass.getText() + "');");
                Toast.makeText(this, "Password changed!", Toast.LENGTH_SHORT).show();
                finish();
            } catch (SQLException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}