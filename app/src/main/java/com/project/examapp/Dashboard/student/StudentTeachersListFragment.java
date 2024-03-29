package com.project.examapp.Dashboard.student;

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

import com.project.examapp.Adapters.TeachersAdapter;
import com.project.examapp.Api.StudentDashboardApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.R;
import com.project.examapp.models.Student;
import com.project.examapp.models.Teacher;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTeachersListFragment extends Fragment {

    ArrayList<Teacher> teachersList;
    TeachersAdapter adapter;
    ProgressDialog dialog;
    RetrofitClient client;
    StudentDashboardApi studentDashboardApi;
    Student student;

   public StudentTeachersListFragment(Student student) {
        this.student = student;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrofit call
        client = RetrofitClient.getInstance();
        studentDashboardApi = client.getRetrofit().create(StudentDashboardApi.class);

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
        return inflater.inflate(R.layout.fragment_teachers_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog = ProgressDialog.show(getContext(), "",
                "Loading.. Please wait...", true);
        dialog.show();
        ((DashboardActivity) requireActivity()).setTitle("Teacher List");
        Call<ArrayList<Teacher>> callExamList = studentDashboardApi.getTeachersList(student.getDept());
        callExamList.enqueue(new Callback<ArrayList<Teacher>>() {
            @Override
            public void onResponse(Call<ArrayList<Teacher>> call, Response<ArrayList<Teacher>> response) {
                if(response.isSuccessful()) {
                    teachersList = response.body();

                    // Create the adapter to convert the array to views
                    adapter = new TeachersAdapter(getContext(), teachersList);

                    // Attach the adapter to a ListView
                    ListView listView = (ListView) getView().findViewById(R.id.stdTsListView);
                    dialog.dismiss();
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Teacher>> call, Throwable t) {
                Log.e("Fetch Teacher List","FAILURE");
                dialog.dismiss();
                ((DashboardActivity)getActivity()).toEmptyFragment("Some error occurred","dashboard");
            }
        });
    }
}