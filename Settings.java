package com.emotion.rateme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    ConstraintLayout constraintLayout1,cardSettings;
    TextView  changeLanguageTxt;

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor sharedPreferencesEditor;

    Button backBtn;

    ImageView wave;

    String [] languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences("LangShared", Context.MODE_PRIVATE);
        Intent i = new Intent(this, MainActivity.class);
        wave = findViewById(R.id.imageView);
        constraintLayout1 = findViewById(R.id.card1);
        cardSettings = findViewById(R.id.cardLanguage);
        changeLanguageTxt = findViewById(R.id.changeLanguageTxt);
        backBtn = findViewById(R.id.backBtn);
        languages =getResources().getStringArray(R.array.languages);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });

        cardSettings.setOnClickListener(new View.OnClickListener() {        //CHANGE LANG
            @Override
            public void onClick(View v) {
                changeLan();
            }
        });
        hideComp();
        loadAnimations();

    }

    public void hideComp () {
        constraintLayout1.setVisibility(View.INVISIBLE);
        cardSettings.setVisibility(View.INVISIBLE);
        changeLanguageTxt.setVisibility(View.INVISIBLE);
        backBtn.setVisibility(View.INVISIBLE);
    }

    public void loadAnimations () {
        (new Handler()).postDelayed(this::loadCard1Anim, 100);
        (new Handler()).postDelayed(this::loadCardSettings, 500);
        (new Handler()).postDelayed(this::loadChangeLanguageTxt, 900);
        (new Handler()).postDelayed(this::loadBackBtnAnim, 1000);
    }

    public void setAnimation (View v, Techniques techniques, int duration) {
        YoYo.with(techniques)
                .duration(duration)
                .repeat(0)
                .playOn(v);
    }

    public void loadCard1Anim () {
        setAnimation(constraintLayout1,Techniques.RollIn,1500);
        constraintLayout1.setVisibility(View.VISIBLE);
    }

    public void loadCardSettings () {
        setAnimation(cardSettings,Techniques.SlideInRight,1400);
        cardSettings.setVisibility(View.VISIBLE);
    }
    public void loadChangeLanguageTxt () {
        setAnimation(changeLanguageTxt,Techniques.FadeIn,1500);
        changeLanguageTxt.setVisibility(View.VISIBLE);
    }

    public void loadBackBtnAnim () {
        setAnimation(backBtn,Techniques.RollIn,500);
        backBtn.setVisibility(View.VISIBLE);
    }

    public void changeLan () {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getString(R.string.settings_change_language))
                .setItems(R.array.languages, (dialog, which) -> {
                    if (which == 0) {
                        setLocal("es");
                        startActivity(new Intent(this,LangConfigMenu.class));
                    }
                    if (which == 1) {
                        setLocal("en");
                        startActivity(new Intent(this,LangConfigMenu.class));

                    }
                    Toast.makeText(getApplicationContext(),getString(R.string.changes_applied) + " " +  languages[which],Toast.LENGTH_SHORT).show();
                });
        dialogBuilder.show();

    }

    public void setLocal (String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resourcesEX;
        try {
            Resources resources = getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration,resources.getDisplayMetrics());
            sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString("LAN",langCode);
            sharedPreferencesEditor.apply();
            startActivity(new Intent(this,LangConfigMenu.class));
        } catch (NullPointerException e) {
            Toast.makeText(this,getString(R.string.something_wrong) + " " + e.getMessage(),Toast.LENGTH_SHORT).show();
            resourcesEX = getResources();
            Configuration configuration = resourcesEX.getConfiguration();
            configuration.setLocale(locale);
            resourcesEX.updateConfiguration(configuration,resourcesEX.getDisplayMetrics());
            sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString("LAN",langCode);
            sharedPreferencesEditor.apply();
            startActivity(new Intent(this,LangConfigMenu.class));

        }

    }

}