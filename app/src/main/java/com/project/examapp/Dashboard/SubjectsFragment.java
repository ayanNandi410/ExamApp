package com.project.examapp.Dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.project.examapp.Adapters.SubjectsAdapter;
import com.project.examapp.Adapters.TeachersAdapter;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Api.StudentDashboardApi;
import com.project.examapp.R;
import com.project.examapp.models.Student;
import com.project.examapp.models.Subject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectsFragment extends Fragment {

    RetrofitClient client;
    StudentDashboardApi studentDashboardApi;
    Student student;
    ArrayList<Subject> subjectList;
    SubjectsAdapter adapter;

    public SubjectsFragment(Student student) {
       this.student = student;
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
        return inflater.inflate(R.layout.fragment_subjects, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).setTitle("Exam List");
        Call<ArrayList<Subject>> callExamList = studentDashboardApi.getSubjectsList(student.getDept());
        callExamList.enqueue(new Callback<ArrayList<Subject>>() {
            @Override
            public void onResponse(Call<ArrayList<Subject>> call, Response<ArrayList<Subject>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), "Teachers List fetched", Toast.LENGTH_SHORT).show();
                    subjectList = response.body();

                    // Create the adapter to convert the array to views
                    adapter = new SubjectsAdapter(getContext(), subjectList);

                    // Attach the adapter to a ListView
                    ListView listView = (ListView) getView().findViewById(R.id.subsListView);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Subject>> call, Throwable t) {
                Log.e("Fetch Teacher List","FAILURE");
            }
        });
    }
}