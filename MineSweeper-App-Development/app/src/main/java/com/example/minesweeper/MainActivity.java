package com.example.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText userID = findViewById(R.id.user);
        EditText passID = findViewById(R.id.pass);
        Button LoginButton = findViewById(R.id.loginbutton);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = userID.getText().toString();
                String pass = passID.getText().toString();
                Log.d("MainActivity", userid);
                Log.d("MainActivity", pass);


                Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(MainActivity.this, game.class);
                startActivity(homeIntent);

            }
        });
    }
}