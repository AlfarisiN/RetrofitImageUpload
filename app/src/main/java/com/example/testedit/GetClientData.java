package com.example.testedit;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface GetClientData {

    @FormUrlEncoded
    @POST("datausers")
    Call<List<ResponseUser>> getdatauser(@Field("id") String id);

    @Multipart
    @POST("update")
    Call<Responsee> postEditProfile(@Part MultipartBody.Part image,
                                    @Part MultipartBody.Part imagee,
                                    @PartMap Map<String, RequestBody> body);

    @Multipart
    @POST("update")
    Call<Response> postMultiImage(@Part ("name") RequestBody name,
                                   @Part ("mail") RequestBody mail,
                                   @Part ("id_image") RequestBody id_image,
                                   @Part List<MultipartBody.Part> image);
}
