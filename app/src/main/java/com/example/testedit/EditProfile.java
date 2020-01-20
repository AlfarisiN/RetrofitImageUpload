package com.example.testedit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class EditProfile extends AppCompatActivity {

    private Button btSubmit;
    private ImageView pickpic, show;
    private EditText edtNama, edtMail;
    private TextView imageName;
    private static final int IMG_REQUEST = 777;
    private static final int IMG_CAMERA = 888;
    private static final int IMG_CAMERA1 = 8888;
    private Bitmap bitmap, bitmap1, correctBitmap, correctBitmap1;
    private String currentPhotoPath = null;
    private String currentPhotoPath1 = null;
    GetClientData service = RetrofitClient.getRetrofit().create(GetClientData.class);
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btSubmit = (Button) findViewById(R.id.btSubmit);
        pickpic = (ImageView) findViewById(R.id.pickpic);
        show = (ImageView) findViewById(R.id.show);
        edtMail = (EditText) findViewById(R.id.edtMail);
        edtNama = (EditText) findViewById(R.id.edtNama);
        imageName = (TextView) findViewById(R.id.imageName);

        pickpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera(v);
//                takeImageFromCamera();
//                editPicProfile();
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera1(v);
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                uploadimage();
                uploadimagemediumtutor(correctBitmap, correctBitmap1);
            }
        });
    }

    private void editPicProfile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"open gallery"),IMG_REQUEST);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }
    //TEST TUTOR MEDIUM
    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.WEBP,0, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TEST TUTOR MEDIUM
//        if (requestCode == IMG_CAMERA && resultCode == RESULT_OK) {
//            bitmap = (Bitmap) data.getExtras().get("data");
//            pickpic.setImageBitmap(bitmap);
//            //panggil method uploadImage
////            uploadImage(mphoto);
//        }
        if (requestCode == IMG_CAMERA && resultCode == RESULT_OK){
            if(currentPhotoPath != null){
                File file = new File(currentPhotoPath);
                try {
                    ExifInterface exifInterface = new ExifInterface(file.getPath());
                    int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int angle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90){
                        angle = 90;
                    }else if (orientation == ExifInterface.ORIENTATION_ROTATE_180){
                        angle = 180;
                    }else if (orientation == ExifInterface.ORIENTATION_ROTATE_270){
                        angle = 270;
                    }
                    final Matrix mat = new Matrix();
                    mat.postRotate(angle);
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, null);
                    correctBitmap = Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
                    pickpic.setImageBitmap(correctBitmap);
                    imageName.setText(filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == IMG_CAMERA1 && resultCode == RESULT_OK){
            if(currentPhotoPath1 != null){
                File file = new File(currentPhotoPath1);
                try {
                    ExifInterface exifInterface = new ExifInterface(file.getPath());
                    int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int angle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90){
                        angle = 90;
                    }else if (orientation == ExifInterface.ORIENTATION_ROTATE_180){
                        angle = 180;
                    }else if (orientation == ExifInterface.ORIENTATION_ROTATE_270){
                        angle = 270;
                    }
                    final Matrix mat = new Matrix();
                    mat.postRotate(angle);
                    bitmap1 = BitmapFactory.decodeStream(new FileInputStream(file), null, null);
                    correctBitmap1 = Bitmap.createBitmap(bitmap1,0,0, bitmap1.getWidth(), bitmap1.getHeight(), mat, true);
                    show.setImageBitmap(correctBitmap1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //TEST TUTOR VIA ONLY GALERY
//        if (resultCode == RESULT_OK)
//        {
//            if(requestCode == IMG_REQUEST)
//            {
//                Uri dataimage = data.getData();
//                String[] imageprojection = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(dataimage,imageprojection,null,null,null);
//
//                if (cursor != null)
//                {
//                    for (int i = 0; i<cursor.getCount(); i++){
//                        cursor.moveToFirst();
//                        int indexImage = cursor.getColumnIndex(imageprojection[i]);
//                        System.out.println(indexImage);
//                        System.out.println(cursor.getCount());
//                        part_image = cursor.getString(indexImage);
//                    }
//
////                    File image = new File(part_image);
////                    pickpic.setImageBitmap(BitmapFactory.decodeFile(image.getAbsolutePath()));
//
//                    if(part_image != null)
//                    {
//                        System.out.println("masuk sini?");
//                        File image = new File(part_image);
//                        pickpic.setImageBitmap(BitmapFactory.decodeFile(image.getAbsolutePath()));
//                    }
//                    System.out.println("langsung kesini?");
//                }
//            }
//        }

        //TEST AWAL
//        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){
//            Uri dataimage = data.getData();
//            String[] imageprojection = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(dataimage, imageprojection, null, null, null);
//            if(cursor != null){
//                cursor.moveToFirst();
//                int index = cursor.getColumnIndex(imageprojection[0]);
//                part_image = cursor.getString(index);
//                if(part_image != null){
//                    File image = new File(part_image);
//                    pickpic.setImageBitmap(BitmapFactory.decodeFile(image.getAbsolutePath()));
//                }
//            }
////            Uri path = data.getData();
////            try {
////                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
////                pickpic.setImageBitmap(bitmap);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//        }
//        if (requestCode == IMG_CAMERA && resultCode == RESULT_OK ) {
//           bitmap = (Bitmap) data.getExtras().get("data");
//            pickpic.setImageBitmap(bitmap);
////            Bundle extras = data.getExtras();
////            bitmap = (Bitmap) extras.get("data");
////            pickpic.setImageBitmap(bitmap);
//        }

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
                Uri imageuri = FileProvider.getUriForFile(EditProfile.this, "com.example.testedit.provider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                startActivityForResult(cameraIntent, IMG_CAMERA);

            }
        }
    }
    private void startCamera1(View view){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager()) != null){
            File imageFile = null;
            try {
                imageFile = getImageFile1();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (imageFile != null){
                Uri imageuri = FileProvider.getUriForFile(EditProfile.this, "com.example.testedit.provider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                startActivityForResult(cameraIntent, IMG_CAMERA1);

            }
        }
    }
    private File getImageFile1() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = timestamp+"_";
        filename = imageName;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentPhotoPath1 = imageFile.getAbsolutePath();
        return imageFile;
    }
    private File getImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = timestamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }
    public void uploadimagemediumtutor(Bitmap gambarbitmap, Bitmap gambarbitmap2){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id", createPartFromString("9"));
        map.put("name", createPartFromString("LELE SUPER"));
        map.put("mail", createPartFromString("BAYAM MERAH"));

        File file = createTempFile(gambarbitmap);
        File file1 = createTempFile(gambarbitmap2);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/jpeg"), file1);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        MultipartBody.Part body1 = MultipartBody.Part.createFormData("imagee", file.getName(), reqFile1);
        Call<Response> call = service.postEditProfile(body,body1, map);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.d("upload success", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }
    public void takeImageFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, IMG_CAMERA);
    }
    private String imagetosting(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte, Base64.DEFAULT);
    }
}
