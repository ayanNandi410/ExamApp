package com.project.examapp.Api;

import com.project.examapp.models.Attempt;
import com.project.examapp.models.FileAnswer;
import com.project.examapp.models.MCQAnswer;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AnswerApi {
    @Headers("Content-Type: application/json")
    @POST("/answer")
    Call<ResponseBody> postAnswers(@Body ArrayList<MCQAnswer> body);

    @POST("/attempt/")
    Call<ResponseBody> postAttempt(@Body Attempt attempt);

    @GET("answer/{eid}/{sid}")
    Call<FileAnswer> getAnswer(@Path("eid") String eid,@Path("sid") String sid);
}
