package com.roninaks.hellomywork.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.roninaks.hellomywork.R;

public class SplashScreen extends AppCompatActivity {

    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        iv = (ImageView) findViewById(R.id.ivImage);
        Glide.with(SplashScreen.this)
                .load(R.raw.logo_splash)
                .into(iv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                while(System.currentTimeMillis() - time < 5850){

                }
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }).start();
    }
}
