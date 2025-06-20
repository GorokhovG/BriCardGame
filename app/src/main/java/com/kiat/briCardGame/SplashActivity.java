package com.kiat.briCardGame;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 секунды

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        // Можно запускать анимацию через LottieAnimationView, если надо программно

        new Handler().postDelayed(() -> {
            // Переход в MenuActivity
            startActivity(new Intent(SplashActivity.this, MenuActivity.class));
            finish();
        }, SPLASH_DELAY);
    }
}