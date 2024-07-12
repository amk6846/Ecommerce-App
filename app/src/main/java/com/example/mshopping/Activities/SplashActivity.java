package com.example.mshopping.Activities;

// SplashActivity.java
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mshopping.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    // Splash screen duration in milliseconds
    private static final long SPLASH_DURATION = 3000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Use Handler with the main looper to post delayed runnable
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the MainActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish splash activity to prevent back navigation
            }
        }, SPLASH_DURATION);
    }
}
