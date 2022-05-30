package com.example.proect;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Start_Screen extends MyCustom_Activity {
    Handler handler;
    ImageView pic_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);


        pic_logo = findViewById(R.id.pic_logo);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Start_Screen.this,
                        MainActivity.class);

                startActivity(intent);
                finish();
            }
        },3000);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_logo_animation);
        pic_logo.startAnimation(animation);
    }
}