package com.emotion.rateme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Locale;

public class Menu extends AppCompatActivity {

    SharedPreferences sharedPreferences,sharedPreferencesLang;
    String name = "";
    TextView title;

    ImageView imageView;

    ConstraintLayout pictureCard,galleryCard,settingsCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //COMP
        sharedPreferences = getSharedPreferences("SharedName", Context.MODE_PRIVATE);
        sharedPreferencesLang = getSharedPreferences("LangShared",Context.MODE_PRIVATE);
        updateLang();
        name = sharedPreferences.getString("name","user");
        title = findViewById(R.id.title);
        title.setText(title.getText() + " " + name);
        imageView = findViewById(R.id.imageView);
        pictureCard = findViewById(R.id.cardPicture);
        galleryCard = findViewById(R.id.cardGallery);
        settingsCard = findViewById(R.id.cardSettings);

        pictureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), cameraEmotion.class));
            }
        });

        settingsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });

        galleryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(getApplicationContext(), cameraEmotion.class);
                galleryIntent.putExtra("gallery","gallery");
                startActivity(galleryIntent);
            }
        });
        hideComp();
        loadAnimations();
    }
    public void hideComp () {
        title.setVisibility(View.INVISIBLE);
        pictureCard.setVisibility(View.INVISIBLE);
        galleryCard.setVisibility(View.INVISIBLE);
        settingsCard.setVisibility(View.INVISIBLE);
    }

    public void updateLang () {
        String LAN = sharedPreferences.getString("LAN","en");
        Locale locale = new Locale(LAN);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
    public void loadAnimations () {
        (new Handler()).postDelayed(this::loadTitleAnim, 100);
        (new Handler()).postDelayed(this::loadPictureCardAnim, 400);
        (new Handler()).postDelayed(this::loadGalleryCardAnim, 500);
        (new Handler()).postDelayed(this::loadSettingsCardAnim, 600);


    }



    public boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
    public void setAnimation (View v, Techniques techniques, int duration) {
        YoYo.with(techniques)
                .duration(duration)
                .repeat(0)
                .playOn(v);
    }
    public void loadTitleAnim () {
        setAnimation(title,Techniques.ZoomIn,1000);
        title.setVisibility(View.VISIBLE);
    }
    public void loadPictureCardAnim () {
        setAnimation(pictureCard,Techniques.FadeInUp,1100);
        pictureCard.setVisibility(View.VISIBLE);
    }
    public void loadGalleryCardAnim () {
        setAnimation(galleryCard,Techniques.FadeInUp,1100);
        galleryCard.setVisibility(View.VISIBLE);
    }
    public void loadSettingsCardAnim () {
        setAnimation(settingsCard,Techniques.FadeInUp,1100);
        settingsCard.setVisibility(View.VISIBLE);
    }
}