package com.project.examapp.Api;

import com.project.examapp.models.Attempt;
import com.project.examapp.models.Exam;
import com.project.examapp.models.Subject;
import com.project.examapp.models.Teacher;
import com.project.examapp.models.User;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StudentDashboardApi {

    @GET("/exam/{dept}")
    Call<ArrayList<Exam>> getExamList(@Path("dept") String dept);

    @GET("/attempt/{sid}")
    Call<ArrayList<Attempt>> getAttemptList(@Path("sid") String sid);

    @POST("/exam/")
    Call<ResponseBody> getExamList(@Body Attempt attempt);

    @GET("/teacher/{dept}")
    Call<ArrayList<Teacher>> getTeachersList(@Path("dept") String dept);

    @GET("/subjects/{dept}")
    Call<ArrayList<Subject>> getSubjectsList(@Path("dept") String dept);

}
