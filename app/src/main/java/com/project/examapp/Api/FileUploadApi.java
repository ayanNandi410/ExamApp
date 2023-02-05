package com.project.examapp.Api;

import android.media.Image;

import com.project.examapp.models.Answer;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface FileUploadApi {

    @Multipart
    // POST request to upload an image from storage
    @POST("answer/pdf")
    Call<ResponseBody> uploadAnswerImage(@Part MultipartBody.Part image,
                                         @Part("exam_id") String exam_id,
                                         @Part("student_id") String student_id);
}
