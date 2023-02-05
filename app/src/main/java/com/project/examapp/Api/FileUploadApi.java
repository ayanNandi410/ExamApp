package com.project.examapp.Api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadApi {

    @Multipart
    // POST request to upload an image from storage
    @POST("answer/pdf")
    Call<ResponseBody> uploadAnswerImage(@Part MultipartBody.Part image,
                                         @Part("exam_id") RequestBody exam_id,
                                         @Part("student_id") RequestBody student_id,
                                         @Part("timestamp") RequestBody timestamp);
}
