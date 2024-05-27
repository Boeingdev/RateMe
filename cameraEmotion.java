package com.emotion.rateme;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.emotion.rateme.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class cameraEmotion extends AppCompatActivity {

    ImageView imageTaked;

    TextView em1,title;

    ConstraintLayout emotionsCard;
    int imageSize = 224;

    String state = "";

    int reqCode;

    Button nextBtn,repeatBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_emotion);
        changeTheme();
        //COMP
        imageTaked = findViewById(R.id.imageTaked);
        em1 = findViewById(R.id.em1);
        title = findViewById(R.id.title);
        nextBtn = findViewById(R.id.nextBtn);
        emotionsCard = findViewById(R.id.emotionsCard);
        repeatBtn = findViewById(R.id.repeatBtn);

        checkIntent(); //CHECK INTENT FOR GALLERY

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Menu.class));
            }
        });

        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (state.equals("photo")) {
                 openCamera();
              }
              else if(state.equals("gallery")) {
                  openGallery();
              }
              else {
                  openCamera();
              }
            }
        });
        hideComp();
     //   openCamera(); //CAMERA
        setAnimations();

    }

    public void changeTheme () {
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(this,R.color.red));
    }

    public void dialogAdvice () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.advice_camera));
        builder.setTitle(getString(R.string.advice_camera_title));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok), (DialogInterface.OnClickListener) (dialog, which) -> {
            finish();
        });
        builder.setNegativeButton(getString(R.string.nah), (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void checkIntent () {
        if (getIntent().getExtras()!= null) {
            if (getIntent().getExtras().getString("gallery") != null) {
                state = "gallery";
                openGallery();
            }
            else if (getIntent().getExtras() == null) {
                state = "photo";
                openCamera();
            }
        }
        else {
            openCamera();
        }
    }

    public void hideComp () {
        imageTaked.setVisibility(View.INVISIBLE);
        em1.setVisibility(View.INVISIBLE);
        nextBtn.setVisibility(View.INVISIBLE);
        repeatBtn.setVisibility(View.INVISIBLE);
    }

    public void setAnimations () {
       setTitleAnim();
        (new Handler()).postDelayed(this::setImageAnim, 1500);
        (new Handler()).postDelayed(this::setCardAnim, 2700);
        (new Handler()).postDelayed(this::setEmotionTxt, 3600);
        (new Handler()).postDelayed(this::setNextBtnAnim, 4100);
        (new Handler()).postDelayed(this::setRepeatBtnAnim, 3900);

    }
    public void setAnim (View v, Techniques techniques, int duration) {
        YoYo.with(techniques)
                .duration(duration)
                .repeat(0)
                .playOn(v);
    }

    public void setTitleAnim () {
        setAnim(title,Techniques.FadeInUp,1600);
    }

    public void setImageAnim () {
        setAnim(imageTaked,Techniques.ZoomInUp,1500);
        imageTaked.setVisibility(View.VISIBLE);
    }

    public void setCardAnim () {
        setAnim(emotionsCard,Techniques.RollIn,1100);
        emotionsCard.setVisibility(View.VISIBLE);
    }
    public void setEmotionTxt () {
        setAnim(em1,Techniques.BounceInUp,1200);
        em1.setVisibility(View.VISIBLE);
    }

    public void setNextBtnAnim () {
        setAnim(nextBtn,Techniques.StandUp,1200);
        nextBtn.setVisibility(View.VISIBLE);
    }

    public void setRepeatBtnAnim () {
        setAnim(repeatBtn,Techniques.StandUp,1200);
        repeatBtn.setVisibility(View.VISIBLE);
    }

    //CAMERA

    public void openCamera () {
        Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(open_camera,3);  //DEPRECATED
        state = "photo";
    }

    //GALLERY

    public void openGallery () {
        Intent open_gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(open_gallery,1);
        state = "gallery";
    }

    @Override
    protected void onActivityResult (int requestCode,int resultCode,Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                reqCode = 3;
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                int dimension = 224;
                photo = ThumbnailUtils.extractThumbnail(photo, dimension, dimension);
                imageTaked.setImageBitmap(photo);
                photo = Bitmap.createScaledBitmap(photo, dimension, dimension, false);
                classifyImage(photo);
                state = "photo";
                //  onCaptureImageResult(data);
            } else {
                reqCode = 1;
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                    image = ThumbnailUtils.extractThumbnail(image, imageSize, imageSize);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageTaked.setImageBitmap(image);
                if (image != null) {
                    classifyImage(image);
                    state = "gallery";

                } else {
                    startActivity(new Intent(this, LangConfigMenu.class));
                }

            }
        }
        else {
            startActivity(new Intent(getApplicationContext(),LangConfigMenu.class));
        }
        super.onActivityResult(requestCode,resultCode,data);

    }

    private void onCaptureImageResult (Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG,90,bytes);
        byte bb [] = bytes.toByteArray();
        imageTaked.setImageBitmap(thumbnail);

    }

    public void classifyImage(Bitmap image) {
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int width = image.getWidth();
            int height = image.getHeight();
            int[] intValues = new int[width * height];
            image.getPixels(intValues, 0, width, 0, 0, width, height);
            int pixel = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.put((byte) ((val >> 16) & 0xFF));
                    byteBuffer.put((byte) ((val >> 8) & 0xFF));
                    byteBuffer.put((byte) (val & 0xFF));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidences = outputFeature0.getFloatArray();
            float sum = 0;
            for (float confidence : confidences) {
                sum += confidence;
            }
            for (int i = 0; i < confidences.length; i++) {
                confidences[i] = (confidences[i] / sum) * 100;
            }
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {getString(R.string.result_angry), getString(R.string.result_disgusted), getString(R.string.result_happy),
                    getString(R.string.result_fearful), getString(R.string.result_neutral), getString(R.string.result_sad), getString(R.string.result_surprised)};
            title.setText(getString(R.string.result_you_are) + " " + classes[maxPos]);
            String s = "";
            for (int i = 0; i < classes.length; i++) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i]);
            }
           em1.setText(s);



            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
          Toast.makeText(this,getString(R.string.something_wrong),Toast.LENGTH_SHORT).show();
         }
    }


}