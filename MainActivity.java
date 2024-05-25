package com.emotion.rateme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.analytics.FirebaseAnalytics;

import intro.inntro_1;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences,sharedPreferencesName;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        sharedPreferences = getSharedPreferences("SharedLog", Context.MODE_PRIVATE);
        sharedPreferencesName = getSharedPreferences("SharedName", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("log","logNo").equals("logYes") && !sharedPreferencesName.getString("name", "noName").equals("")) {
            startActivity(new Intent(this,LangConfigMenu.class));
        }
        else {
           replaceFragment(new inntro_1());
        }
    }

    public void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();

    }
}