package com.project.examapp.Api;

import com.project.examapp.models.Attempt;
import com.project.examapp.models.FileAnswer;
import com.project.examapp.models.MCQAnswer;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AnswerApi {
    @Headers("Content-Type: application/json")
    @POST("/answer")
    Call<ResponseBody> postAnswers(@Body MCQAnswer body);

    @POST("/attempt/")
    Call<ResponseBody> postAttempt(@Body Attempt attempt);

    @GET("answer/{eid}/{sid}")
    Call<FileAnswer> getAnswer(@Path("eid") String eid,@Path("sid") String sid);

    @Multipart
    // POST request to upload an image from storage
    @POST("answer/pdf")
    Call<ResponseBody> uploadAnswerImage(@Part MultipartBody.Part image,
                                         @Part("exam_id") RequestBody exam_id,
                                         @Part("student_id") RequestBody student_id,
                                         @Part("timestamp") RequestBody timestamp);
}
