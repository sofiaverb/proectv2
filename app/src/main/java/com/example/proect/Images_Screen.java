package com.example.proect;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Images_Screen extends MyCustom_Activity
        implements CompoundButton.OnCheckedChangeListener ,
        ImageAdapter.OnItemClickListener{
    private RadioGroup radioGroup_Cloth;
    private RadioButton radioButton_shirts;
    private RadioButton radioButton_pants;
    private RadioButton radioButton_shoes;
    private Switch swt_shoes, swt_pants, swt_shirts;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private DatabaseReference images_ref;
    private List<Upload> images;
    private FloatingActionButton fb_addImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tops);
        images = new ArrayList<>();
        fb_addImage = findViewById(R.id.fb_addImage);
        fb_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Images_Screen.this,
                        AddImage.class));
            }
        });
        radioGroup_Cloth = findViewById(R.id.radioGroup_cloth);
        radioButton_shirts = findViewById(R.id.radioButton_shirts);
        radioButton_pants = findViewById(R.id.radioButton_pants);
        radioButton_shoes = findViewById(R.id.radioButton_shoes);
        swt_pants = findViewById(R.id.savebot);
        swt_shirts = findViewById(R.id.savetop);
        swt_shoes = findViewById(R.id.saveshs);
        radioButton_shirts.setChecked(false);
        radioButton_pants.setChecked(false);
        radioButton_shoes.setChecked(false);
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        radioButton_pants.setOnCheckedChangeListener(this);
        radioButton_pants.setOnCheckedChangeListener(this);
        radioButton_shoes.setOnCheckedChangeListener(this);


    }

    public void showImages_ByType(String type) {
        //clear previous selection
        clearData();
        images_ref = FirebaseDatabase.getInstance().getReference("images/");
        images_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if (upload.getType().equals(type))
                        images.add(upload); //filter by type
                }
                adapter = new ImageAdapter(Images_Screen.this, images);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //adapter.setOnItemClickListener(Images_Screen.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Images_Screen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(this, "Delete click at position: " + position, Toast.LENGTH_SHORT).show();
    }
    private void clearData() {
        adapter = new ImageAdapter(getApplicationContext(), images);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        clearData();
        if (compoundButton.getId() == radioButton_pants.getId())
            showImages_ByType("Pants");
        if (compoundButton.getId() == radioButton_shoes.getId())
            showImages_ByType("Shoes");
        if (compoundButton.getId() == radioButton_shirts.getId())
            showImages_ByType("Shirts");
    }


}