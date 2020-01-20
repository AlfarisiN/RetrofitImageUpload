package com.example.testedit;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView avatar;
    private TextView nama, mail;
    private Button btEdit, btTestCamera;

    GetClientData service = RetrofitClient.getRetrofit().create(GetClientData.class);
    private static final String BASE_URL = "http://10.108.45.34:8000/tests/user/avatar/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        avatar = (ImageView) findViewById(R.id.avatar);
        nama = (TextView) findViewById(R.id.nama);
        mail = (TextView) findViewById(R.id.mail);
        btEdit = (Button) findViewById(R.id.btEdit);
        btTestCamera = (Button) findViewById(R.id.btTestCamera);

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EditProfile.class);
                startActivity(i);
            }
        });
        btTestCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TestCamera.class);
                startActivity(i);
            }
        });

        getUser();
    }

    private void getUser(){
        Call<List<ResponseUser>> call = service.getdatauser("9");
        call.enqueue(new Callback<List<ResponseUser>>() {
            @Override
            public void onResponse(Call<List<ResponseUser>> call, Response<List<ResponseUser>> response) {
                if (response.code()==200){
                    System.out.println("masuksini");
                    List<ResponseUser> users = (List<ResponseUser>) response.body();
                    for (int i = 0; i<users.size(); i++){
                        nama.setText(response.body().get(i).getName());
                        mail.setText(response.body().get(i).getMail());
                        Picasso.with(MainActivity.this).load(BASE_URL+response.body().get(i).getImagename()).into(avatar);
                        System.out.println("isi url : " +BASE_URL+response.body().get(i).getImagename());
                    }
                } else{
                    Log.d("masuk sini: ", "2");
                }
            }

            @Override
            public void onFailure(Call<List<ResponseUser>> call, Throwable t) {

            }
        });
    }

}
