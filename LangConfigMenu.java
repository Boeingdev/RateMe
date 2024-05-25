package com.emotion.rateme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class LangConfigMenu extends AppCompatActivity {

    SharedPreferences sharedPreferencesLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang_config_menu);
        sharedPreferencesLang = getSharedPreferences("LangShared", Context.MODE_PRIVATE);
        Log.d("ClassPassed","ClassPass");
        updateLang();
    }

    public void updateLang () {
        String LAN = sharedPreferencesLang.getString("LAN","en");
        Log.d("LanAlert","LanAlertAdvice");
        Locale locale = new Locale(LAN);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        startActivity(new Intent(this,Menu.class));
    }
}