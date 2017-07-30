package com.dev.muslim.pantaumeter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by owner on 7/20/2017.
 */
public interface UploadImageInterface {
    @Multipart
    @POST("/manajemen_pelanggan/api/upload")
    Call<UploadObject> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);


}

