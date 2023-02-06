package com.project.examapp.Dashboard.student;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.examapp.Adapters.ExamsAdapter;
import com.project.examapp.Api.StudentDashboardApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.Exam.ExamPageActivity;
import com.project.examapp.Exam.FileUploadActivity;
import com.project.examapp.R;
import com.project.examapp.common.ProgressBarFragment;
import com.project.examapp.models.Attempt;
import com.project.examapp.models.Exam;
import com.project.examapp.models.Student;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentExamListFragment extends Fragment {

    ArrayList<Exam> examList;
    ProgressDialog dialog;
    ArrayList<Attempt> attemptList;
    ExamsAdapter adapter;
    RetrofitClient client;
    StudentDashboardApi studentDashboardApi;
    ListView listView;
    Student student;

    public StudentExamListFragment(Student student) {
        this.student = student;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrofit call
        client = RetrofitClient.getInstance();
        studentDashboardApi = client.getRetrofit().create(StudentDashboardApi.class);
        ((DashboardActivity) getActivity()).setTitle("Exam List");

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
        return inflater.inflate(R.layout.fragment_student_exam_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefreshExamsList);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getExams();
                Toast.makeText(getContext(), "Exam List refreshed", Toast.LENGTH_SHORT).show();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog = ProgressDialog.show(getContext(), "",
                "Loading.. Please wait...", true);
        dialog.show();
        getExams();
    }

    private void getExams(){
        listView = (ListView) getView().findViewById(R.id.stdExamListView);
        Call<ArrayList<Exam>> callExamList = studentDashboardApi.getExamList(student.getDept());
        callExamList.enqueue(new Callback<ArrayList<Exam>>() {
            @Override
            public void onResponse(Call<ArrayList<Exam>> call, Response<ArrayList<Exam>> response) {
                if(response.isSuccessful()) {
                    Log.i("Exam List Fetch", "Success");
                    examList = response.body();
                    getAttemptList(student.getId());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Exam>> call, Throwable t) {
                Log.e("Fetch Exam List","FAILURE");
                Log.e("Fetch Exam list", t.toString());
                ((DashboardActivity)getActivity()).toEmptyFragment("Some error occurred","dashboard");
            }
        });
    }

    private void getAttemptList(String sid)
    {
        listView = (ListView) getView().findViewById(R.id.stdExamListView);
        Call<ArrayList<Attempt>> callAttemptList = studentDashboardApi.getAttemptList(sid);
        callAttemptList.enqueue(new Callback<ArrayList<Attempt>>() {
            @Override
            public void onResponse(Call<ArrayList<Attempt>> call, Response<ArrayList<Attempt>> response) {
                if(response.isSuccessful()) {
                    Log.i("Attempt List Fetch", "Success");
                    attemptList = response.body();

                    // Create the adapter to convert the array to views
                    adapter = new ExamsAdapter(getContext(), examList, attemptList);

                    // Attach the adapter to a ListView
                    listView.setAdapter(adapter);
                    dialog.dismiss();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if(examList.get(i).isAvailable())
                            {
                                StartExamAlert(i);
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Exam not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Attempt>> call, Throwable t) {
                Log.e("Fetch Exam List","FAILURE");
                Log.e("Fetch Exam list", t.toString());
            }
        });
    }

    public void toTakeExam(int i)
    {
        Exam exam = adapter.getItem(i);
        String exam_id = exam.getExam_id();
        Integer time = exam.getTime();
        Intent takeExamIntent;

        if((exam.getType()).equals("file"))
        {
            takeExamIntent = new Intent(getActivity(),FileUploadActivity.class);

        }
        else
        {
            takeExamIntent = new Intent(getActivity(), ExamPageActivity.class);
        }
        takeExamIntent.putExtra("exam_id",exam_id);
        takeExamIntent.putExtra("student_id",student.getId());
        takeExamIntent.putExtra("time",time);
        takeExamIntent.putExtra("question",exam.getQuestion());
        getActivity().finish();
        startActivity(takeExamIntent);
    }

    private void StartExamAlert(int i) {

        Exam exam = adapter.getItem(i);
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set the message show for the Alert time
        String message = "\nExam Name : " + exam.getExamName() + "\nExam Time : " + getExamTime(exam.getTime()) +
                "\nSubject ID : " + exam.getSubject_id() + "\nDescription : " + exam.getDescription() +
                "\n\n\nDo you wish to start ?\n";
        builder.setMessage(message);
        // Set Alert Title
        builder.setTitle("Start Exam");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            toTakeExam(i);
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }

    private String getExamTime(Integer min)
    {
        Integer hours = min/60;
        min = min % 60;
        if(hours==0)
            return String.valueOf(min) + " min";
        else if(hours==1)
            return "1 hour and " + String.valueOf(min) + " min";
        else
            return String.valueOf(hours) + " hours and " + String.valueOf(min) + " min";
    }
}