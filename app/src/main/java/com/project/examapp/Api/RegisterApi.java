package com.project.examapp.Api;

import com.project.examapp.models.Question;
import com.project.examapp.models.Student;
import com.project.examapp.models.Teacher;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RegisterApi {

    @GET("studentByEmail/{email}")
    Call<ArrayList<Student>> getStudentByEmail(@Path("email") String email);

    @GET("teacherByEmail/{email}")
    Call<Teacher> getTeacherByEmail(@Path("email") String email);
}
