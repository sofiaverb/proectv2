package com.example.proect;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;

public class MainActivity extends MyCustom_Activity implements View.OnClickListener {

    Button clst, btn_create;
    Switch lock_shoes, lock_top, lock_bot;
    ImageView img_bot, img_top, img_shoes;
    List<Upload> clothes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clothes = new ArrayList<>();
        img_bot = findViewById(R.id.img_bot);
        img_top = findViewById(R.id.img_top);
        img_shoes = findViewById(R.id.img_shoes);

        btn_create = findViewById(R.id.btn_create);
        btn_create.setOnClickListener(this);
        clst = findViewById(R.id.btn_clst);
        clst.setOnClickListener(this);



        lock_shoes = findViewById(R.id.saveshs);
        lock_top = findViewById(R.id.savetop);
        lock_bot = findViewById(R.id.savebot);

    }
//todo background music
    //todo copy camera code
    //todo generating and favourites
    public void onClick(View v) {
        if(clst == v) {
            Intent intent = new Intent(MainActivity.this, Images_Screen.class);
            startActivity(intent);
        }
        if(v == btn_create) {
            //TODO generator from database
            if(!lock_top.isChecked()){
                getSingleImage(img_top, "Shirts");
            }
            if(!lock_bot.isChecked()){
                getSingleImage(img_bot, "Pants");
            }
            if(!lock_shoes.isChecked()){
                getSingleImage(img_shoes, "Shoes");
            }
        }
    }

    private void getSingleImage(ImageView pic, String type) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("images");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //todo - correct randomly loop
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {

                    Random random = new Random();
                    int index = random.nextInt((int) dataSnapshot.getChildrenCount());

                    int count = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Upload cloth = snapshot.getValue(Upload.class);
                        if (count == index && cloth.getType().equals(type)) {
                            Picasso.get().
                                    load(cloth.getImageUrl()).
                                    fit().
                                    centerCrop().
                                    into(pic);
                            return;
                        }
                        count++;

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}