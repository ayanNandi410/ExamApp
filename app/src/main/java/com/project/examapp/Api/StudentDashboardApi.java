package com.project.examapp.Api;

import com.project.examapp.models.Exam;
import com.project.examapp.models.Teacher;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StudentDashboardApi {

    @GET("/exam")
    Call<ArrayList<Exam>> getExamList();

    @GET("/teacher")
    Call<ArrayList<Teacher>> getTeachersList();
}
