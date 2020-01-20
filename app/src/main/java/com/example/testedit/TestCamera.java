package com.example.testedit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestCamera extends AppCompatActivity {
    private Button startCamera, displayImage;
    private ImageView show;

    String currentImagePath = null;
    private static final int IMAGE_CAMERA_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camera);

        startCamera = (Button) findViewById(R.id.startCamera);
        displayImage = (Button) findViewById(R.id.displayImage);
        show = (ImageView) findViewById(R.id.show);

        startCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera(v);
            }
        });
    }

    private void startCamera(View view){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager()) != null){
            File imageFile = null;
            try {
                imageFile = getImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (imageFile != null){
                Uri imageuri = FileProvider.getUriForFile(TestCamera.this, "com.example.testedit.provider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                startActivityForResult(cameraIntent, IMAGE_CAMERA_CAPTURE);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAMERA_CAPTURE && resultCode == RESULT_OK){
            if(currentImagePath != null){
                Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath);
                show.setImageBitmap(bitmap);
            }
        }
    }

    private File getImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = timestamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }
}
