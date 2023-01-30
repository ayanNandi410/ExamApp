package com.project.examapp.Dashboard.teacher;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.project.examapp.Adapters.ExamScoresAdapter;
import com.project.examapp.Adapters.ScoresAdapter;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Api.TeacherDashboardApi;
import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.R;
import com.project.examapp.models.Exam;
import com.project.examapp.models.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoresFragment extends Fragment {
    ArrayList<Result> scoresList;
    ProgressDialog dialog;
    ScoresAdapter adapter;
    RetrofitClient client;
    TeacherDashboardApi teacherDashboardApi;
    ListView listView;
    String exam_id;
    public ScoresFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exam_id = getArguments().getString("exam-id");
        //Retrofit call
        client = RetrofitClient.getInstance();
        teacherDashboardApi = client.getRetrofit().create(TeacherDashboardApi.class);
        ((DashboardActivity) getActivity()).setTitle("Exam Scores ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exam_scores, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog = ProgressDialog.show(getContext(), "",
                "Loading.. Please wait...", true);
        dialog.show();
        getScores();
    }
    private void getScores(){
        listView = (ListView) getView().findViewById(R.id.tchExamListView);
        Call<ArrayList<Result>> callScoresList = teacherDashboardApi.getResultsList(exam_id);
        callScoresList.enqueue(new Callback<ArrayList<Result>>() {
            @Override
            public void onResponse(Call<ArrayList<Result>> call, Response<ArrayList<Result>> response) {
                if(response.isSuccessful()) {
                    Log.i("Exam List Fetch", "Success");
                    scoresList = response.body();

                    if(scoresList.size()==0)
                    {
                        dialog.dismiss();
                        ((DashboardActivity)getActivity()).toEmptyFragment("No scores available");
                    }

                    // Create the adapter to convert the array to views
                    adapter = new ScoresAdapter(getContext(), scoresList);

                    // Attach the adapter to a ListView
                    listView.setAdapter(adapter);
                    dialog.dismiss();

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ExpandScoreAlert(i);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Result>> call, Throwable t) {
                Log.e("Fetch Exam List","FAILURE");
                dialog.dismiss();
                ((DashboardActivity)getActivity()).toEmptyFragment("`Some error occurred`");
            }
        });
    }

    private void ExpandScoreAlert(int i)
    {
        Result result = adapter.getItem(i);
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set the message show for the Alert time
        String message = "\nExam ID : " + result.getExamId() + "\nStudent ID : " + result.getStudentId() + "\nScore : " + result.getMarks();
        builder.setMessage(message);
        // Set Alert Title
        builder.setTitle("Score");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(true);

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("Close", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}
