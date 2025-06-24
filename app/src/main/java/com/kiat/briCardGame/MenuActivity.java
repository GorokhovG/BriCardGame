package com.kiat.briCardGame;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import androidx.annotation.Nullable;
import com.google.android.material.button.MaterialButton;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setStatusBarColor(ContextCompat.getColor(this, R.color.mainTop));
        setContentView(R.layout.activity_menu);

        MaterialButton btnSingle = findViewById(R.id.btnSingle);
        MaterialButton btnMulti = findViewById(R.id.btnMulti);
        MaterialButton btnSettings = findViewById(R.id.btnSettings);
        MaterialButton btnLeaderboard = findViewById(R.id.btnLeaderboard);

        // Анимация появления кнопок
        animateButtonIn(btnSingle, 0);
        animateButtonIn(btnMulti, 100);
        animateButtonIn(btnSettings, 200);
        animateButtonIn(btnLeaderboard, 300);

        btnSingle.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });

        btnMulti.setOnClickListener(v -> {
            // TODO: переход к выбору сетевой игры
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        btnLeaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(this, LeaderActivity.class);
            startActivity(intent);
        });
    }

    private void animateButtonIn(View view, int delay) {
        view.setTranslationY(300);
        view.setAlpha(0f);
        view.animate()
                .translationY(0)
                .alpha(1f)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(600)
                .setStartDelay(delay)
                .start();
    }

    public void setStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
    }
}