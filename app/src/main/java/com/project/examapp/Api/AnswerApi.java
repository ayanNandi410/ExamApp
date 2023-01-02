package com.project.examapp.Api;

import com.project.examapp.models.Answer;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AnswerApi {
    @Headers("Content-Type: application/json")
    @POST("/answer")
    Call<Integer> postAnswers(@Body ArrayList<Answer> body);
}
