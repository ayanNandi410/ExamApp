package com.project.examapp.Dashboard.teacher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.project.examapp.Adapters.TeachersAdapter;
import com.project.examapp.Api.StudentDashboardApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.R;
import com.project.examapp.models.Teacher;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTeachersListFragment extends Fragment {

    ArrayList<Teacher> teachersList;
    TeachersAdapter adapter;
    RetrofitClient client;
    StudentDashboardApi studentDashboardApi;

    public StudentTeachersListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrofit call
        client = RetrofitClient.getInstance();
        studentDashboardApi = client.getRetrofit().create(StudentDashboardApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_teachers_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Call<ArrayList<Teacher>> callExamList = studentDashboardApi.getTeachersList();
        callExamList.enqueue(new Callback<ArrayList<Teacher>>() {
            @Override
            public void onResponse(Call<ArrayList<Teacher>> call, Response<ArrayList<Teacher>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), "Teachers List fetched", Toast.LENGTH_SHORT).show();
                    teachersList = response.body();

                    // Create the adapter to convert the array to views
                    adapter = new TeachersAdapter(getContext(), teachersList);

                    // Attach the adapter to a ListView
                    ListView listView = (ListView) getView().findViewById(R.id.stdTsListView);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Teacher>> call, Throwable t) {
                Log.e("Fetch Teacher List","FAILURE");
            }
        });
    }
}