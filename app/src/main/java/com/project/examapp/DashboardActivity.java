package com.project.examapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class DashboardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        getSupportActionBar().hide();

    }
}
