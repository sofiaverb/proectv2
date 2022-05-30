package com.example.proect;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Images_Screen extends MyCustom_Activity {
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private DatabaseReference images_ref;
    private List<Upload> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tops);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        images = new ArrayList<>();
        images_ref = FirebaseDatabase.getInstance().getReference("images");
        images_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    images.add(upload); //filter by type
                }
                adapter = new ImageAdapter(Images_Screen.this, images);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Images_Screen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}