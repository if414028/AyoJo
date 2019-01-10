package com.jo.ayo.ayojo.nework.api;

import com.jo.ayo.ayojo.data.model.post.create.ImageUpload;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiImageUpload {

    @Multipart
    @POST("api/uploads/single")
    Call<ImageUpload> uploadImage(@Part MultipartBody.Part file);

}
