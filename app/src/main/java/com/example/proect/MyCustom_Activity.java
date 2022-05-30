package com.example.proect;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MyCustom_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_custom);

        getSupportActionBar().hide(); //<< this
    }
}