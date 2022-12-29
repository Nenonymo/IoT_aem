package com.example.iotlab3app.Groups;

import androidx.appcompat.app.AppCompatActivity;
import com.example.iotlab3app.R;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

// A class for either loading an existing house or creating a new house

public class CreateHouseActivity extends AppCompatActivity {
    private Button createBtn, loadBtn;
    private EditText nickname, category;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_house);

        nickname = (EditText)findViewById(R.id.nickname);
        category = (EditText)findViewById(R.id.category);

        createBtn = (Button) findViewById(R.id.createBtn);
        loadBtn = (Button) findViewById(R.id.loadBtn);


    }



}