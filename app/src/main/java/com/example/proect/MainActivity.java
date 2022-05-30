package com.example.proect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends MyCustom_Activity implements View.OnClickListener {

    Button clst, create;
    Switch lock_shoes, lock_top, lock_bot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //<< this

        clst = findViewById(R.id.btn_clst);
        clst.setOnClickListener(this);

        create = findViewById(R.id.btn_create);
        create.setOnClickListener(this);

        lock_shoes = findViewById(R.id.saveshs);
        lock_top = findViewById(R.id.savetop);
        lock_bot = findViewById(R.id.savebot);

    }
//todo background music
    //todo copy camera code
    //todo generating and favourites
    public void onClick(View v) {
        if(clst == v) {
            Intent c = new Intent(this, Closet_Screen.class);
            startActivity(c);
        }
        if(v == create) {
            //TODO generator from database
        }
    }
}