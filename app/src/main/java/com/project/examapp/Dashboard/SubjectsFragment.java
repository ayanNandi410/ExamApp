package com.project.examapp.Dashboard;

import android.app.ProgressDialog;
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
import com.project.examapp.models.Teacher;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectsFragment extends Fragment {

    RetrofitClient client;
    StudentDashboardApi studentDashboardApi;
    String dept;
    ProgressDialog dialog;
    ArrayList<Subject> subjectList;
    SubjectsAdapter adapter;

    public SubjectsFragment(Student student, Teacher teacher) {
       if(student == null)
           dept = teacher.getDept();
       else
           dept = student.getDept();
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
        dialog = ProgressDialog.show(getContext(), "",
                "Loading.. Please wait...", true);
        dialog.show();
        ((DashboardActivity) getActivity()).setTitle("Subject List");
        Call<ArrayList<Subject>> callExamList = studentDashboardApi.getSubjectsList(dept);
        callExamList.enqueue(new Callback<ArrayList<Subject>>() {
            @Override
            public void onResponse(Call<ArrayList<Subject>> call, Response<ArrayList<Subject>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), "Subject List fetched", Toast.LENGTH_SHORT).show();
                    subjectList = response.body();

                    // Create the adapter to convert the array to views
                    adapter = new SubjectsAdapter(getContext(), subjectList);

                    // Attach the adapter to a ListView
                    ListView listView = (ListView) getView().findViewById(R.id.subsListView);
                    dialog.dismiss();
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Subject>> call, Throwable t) {
                Log.e("Fetch Subject List","FAILURE");
                dialog.dismiss();
                ((DashboardActivity)getActivity()).toEmptyFragment("Some error occurred");
            }
        });
    }
}