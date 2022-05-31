package com.example.proect;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

public class AddImage extends MyCustom_Activity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btn_chooseImage, btn_upload;
    private TextView tv_show_upload;
    private Spinner spinner_type;
    private ImageView iv_pic;
    private ProgressBar progressBar;
    private Uri uri_image;

    private DatabaseReference imageRef;
    private StorageReference storageRef;
    private StorageTask upload_task;
    ActivityResultLauncher<String> result_content;
    String[] types = {"Select Type!", "Shoes", "Pants", "Shirts"};
    String type_selected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);


        init();
        result_content = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        iv_pic.setImageURI(result);
                        uri_image = result;
                        //upload image to firebase using picasso
                        Picasso.get().load(uri_image).into(iv_pic);
                    }
                });
        storageRef = FirebaseStorage.getInstance().getReference("images");
        imageRef = FirebaseDatabase.getInstance().getReference("images");
    }

    private void init() {

        btn_chooseImage = findViewById(R.id.button_choose_image);
        btn_upload = findViewById(R.id.button_upload);
        tv_show_upload = findViewById(R.id.text_view_show_uploads);
        spinner_type = findViewById(R.id.spinner_type);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type_selected = types[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, types);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_type.setAdapter(aa);

        iv_pic = findViewById(R.id.image_view);
        progressBar = findViewById(R.id.progress_bar);

        btn_upload.setOnClickListener(this);
        btn_chooseImage.setOnClickListener(this);
        tv_show_upload.setOnClickListener(this);
    }

    //loads image from our device
    private void openFileChooser() {
        //v1 - decprated, newer solution below
       /* Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);*/
        result_content.launch("image/*");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            uri_image = data.getData();
            //upload image to firebase using picasso
            Picasso.get().load(uri_image).into(iv_pic); //replace with to get
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_choose_image:
                openFileChooser();
                break;

            case R.id.button_upload:
                if (upload_task != null && upload_task.isInProgress()) {
                    Toast.makeText(AddImage.this,
                            "Upload in progress",
                            Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
                break;

            case R.id.text_view_show_uploads:
                openImagesActivity();
                break;

        }
    }

    private void openImagesActivity() {
        Intent intent = new Intent(AddImage.this, Images_Screen.class);
        startActivity(intent);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();  //singleton - https://techvidvan.com/tutorials/java-singleton-class/

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
        if (!type_selected.equals("Select Type!")) {
            if (uri_image != null) {
                StorageReference fileRef = storageRef.child(
                        System.currentTimeMillis() + "." + getFileExtension(uri_image));
                upload_task = fileRef.putFile(uri_image).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setProgress(0);
                                    }
                                }, 500);
                                Toast.makeText(AddImage.this,
                                        "Upload Successfully!", Toast.LENGTH_SHORT).show();
                                //below code will create a bug (wont show images)- fix is below
                                /*Upload upload = new Upload(type_selected,
                                        taskSnapshot.getMetadata().
                                                getReference().getDownloadUrl().toString());
                                String upLoadId = imageRef.push().getKey();
                                imageRef.child(upLoadId).setValue(upload);*/
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                Uri downloadUrl = urlTask.getResult();

                                //Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                                Upload upload = new Upload(type_selected, downloadUrl.toString());

                                String uploadId = imageRef.push().getKey();
                                imageRef.child(uploadId).setValue(upload);
                                startActivity(new Intent(getApplicationContext(),
                                        Closet_Screen.class));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddImage.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0 *
                                        snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                progressBar.setProgress((int) progress);
                            }
                        });
            } else {
                Toast.makeText(this,
                        "No File Selected!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AddImage.this,
                    "No Type Selected!", Toast.LENGTH_SHORT).show();
        }

    }
}