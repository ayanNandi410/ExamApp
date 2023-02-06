package com.project.examapp.Dashboard.teacher;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.project.examapp.Adapters.StudentsAdapter;
import com.project.examapp.Adapters.TeachersAdapter;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Api.TeacherDashboardApi;
import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.R;
import com.project.examapp.common.ProgressBarFragment;
import com.project.examapp.models.Student;
import com.project.examapp.models.Teacher;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsListFragment extends Fragment {

    ArrayList<Student> studentsList;
    StudentsAdapter adapter;
    ProgressDialog dialog;
    RetrofitClient client;
    TeacherDashboardApi teacherDashboardApi;
    Teacher teacher;

    public StudentsListFragment(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrofit call
        client = RetrofitClient.getInstance();
        teacherDashboardApi = client.getRetrofit().create(TeacherDashboardApi.class);

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                ((DashboardActivity)getActivity()).toDashboard();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_students_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog = ProgressDialog.show(getContext(), "",
                "Loading.. Please wait...", true);
        dialog.show();
        ((DashboardActivity) requireActivity()).setTitle("Student List");
        Call<ArrayList<Student>> callExamList = teacherDashboardApi.getStudentsList(teacher.getDept());
        callExamList.enqueue(new Callback<ArrayList<Student>>() {
            @Override
            public void onResponse(Call<ArrayList<Student>> call, Response<ArrayList<Student>> response) {
                if(response.isSuccessful()) {
                    studentsList = response.body();

                    // Create the adapter to convert the array to views
                    adapter = new StudentsAdapter(getContext(), studentsList);

                    // Attach the adapter to a ListView
                    ListView listView = (ListView) getView().findViewById(R.id.trListView);
                    dialog.dismiss();
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Student>> call, Throwable t) {
                Log.e("Fetch Student List","FAILURE");
                dialog.dismiss();
                ((DashboardActivity)getActivity()).toEmptyFragment("Some error occurred","dashboard");
            }
        });
    }
}