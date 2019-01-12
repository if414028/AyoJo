package com.jo.ayo.ayojo.nework.api;

import com.jo.ayo.ayojo.data.model.post.create.ImageUpload;

import java.io.File;
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

public interface ApiImageUpload {

    @Multipart
    @POST("api/uploads/single")
    Call<ImageUpload> uploadImage(@Part MultipartBody.Part file);


}
