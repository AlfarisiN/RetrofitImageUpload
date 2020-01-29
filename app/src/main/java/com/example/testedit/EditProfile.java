package com.example.testedit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.testedit.MultipleImage.FileUtils;
import com.example.testedit.MultipleImage.ImageAdapter;
import com.example.testedit.MultipleImage.ItemOffsetDecoration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    private Button btSubmit, btCamera;
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
    private List<String> listOfImagesPath;
    private ArrayList<Uri> arrImages = new ArrayList();
    private RecyclerView rv_Images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btSubmit = (Button) findViewById(R.id.btSubmit);
        btCamera = (Button) findViewById(R.id.btCamera);
        edtMail = (EditText) findViewById(R.id.edtMail);
        edtNama = (EditText) findViewById(R.id.edtNama);
        imageName = (TextView) findViewById(R.id.imageName);
        rv_Images = (RecyclerView) findViewById(R.id.rvImage);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                uploadimage();
//                uploadimagemediumtutor(correctBitmap, correctBitmap1);
                uploadmultiple();
            }
        });
        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera(v);
            }
        });
    }
    private void uploadmultiple(){
        List<MultipartBody.Part> parts = new ArrayList<>();
        if(arrImages != null){
            for (int i = 0; i<arrImages.size(); i++) {
                parts.add(prepareFilePart("image[]", arrImages.get(i)));
            }
        }
        System.out.println("size parts " + parts.size());

        RequestBody name = createPartFromString("jhon");
        RequestBody mail = createPartFromString("testmail@mail.com");
        Call<Response> call = service.postMultiImage(name, mail, parts);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Toast.makeText(EditProfile.this,""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri){
        File file = FileUtils.getFile(this, fileUri);
        @SuppressLint({"NewApi", "LocalSuppress"}) RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(Objects.requireNonNull(getContentResolver().getType(fileUri))),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
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

                    String imagepath = MediaStore.Images.Media.insertImage(this.getContentResolver(), correctBitmap, "Title", null);
                    arrImages.add(Uri.parse(imagepath));
                    Log.e("camera", "onActivityResult: " + arrImages.size());
                    initRecyclerView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private void initRecyclerView(){
        int spacing = 7; // 50px
        rv_Images.addItemDecoration(new ItemOffsetDecoration(spacing));

        rv_Images.setLayoutManager(new GridLayoutManager(this, 2));
        ImageAdapter adapter = new ImageAdapter(arrImages);
        rv_Images.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        Call<Responsee> call = service.postEditProfile(body,body1, map);
        call.enqueue(new Callback<Responsee>() {
            @Override
            public void onResponse(Call<Responsee> call, retrofit2.Response<Responsee> response) {
                Log.d("upload success", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<Responsee> call, Throwable t) {

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
