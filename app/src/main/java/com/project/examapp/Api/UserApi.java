package com.project.examapp.Api;

import com.project.examapp.models.Student;
import com.project.examapp.models.Teacher;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {

    @GET("studentByEmail/{email}")
    Call<Student> getStudentByEmail(@Path("email") String email);

    @GET("teacherByEmail/{email}")
    Call<Teacher> getTeacherByEmail(@Path("email") String email);
}
