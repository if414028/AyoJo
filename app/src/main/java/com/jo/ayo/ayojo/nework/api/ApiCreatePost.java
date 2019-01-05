package com.jo.ayo.ayojo.nework.api;

import com.jo.ayo.ayojo.data.model.post.create.PostDataResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiCreatePost {

    @FormUrlEncoded
    @POST("api/reports/")
    Call<PostDataResult> createPost(
            @Header("authorization") String token,
            @Field("name") String name,
            @Field("address1") String address,
            @Field("pekerjaan") String work,
            @Field("usia") String age,
            @Field("jenisKelamin") String sex,
            @Field("lat") String lat,
            @Field("lng") String lng,
            @Field("images") String imageUrl,
            @Field("answer1") String answer1,
            @Field("answer2") String answer2
    );
}
