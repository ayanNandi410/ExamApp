package com.project.examapp.Dashboard.teacher;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.examapp.Adapters.ExamScoresAdapter;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Api.TeacherDashboardApi;
import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.R;
import com.project.examapp.models.Exam;
import com.project.examapp.models.Teacher;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamScoresFragment extends Fragment {

    ArrayList<Exam> examList;
    ProgressDialog dialog;
    ExamScoresAdapter adapter;
    RetrofitClient client;
    TeacherDashboardApi teacherDashboardApi;
    ListView listView;
    Teacher teacher;

    public ExamScoresFragment(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrofit call
        client = RetrofitClient.getInstance();
        teacherDashboardApi = client.getRetrofit().create(TeacherDashboardApi.class);
        ((DashboardActivity) getActivity()).setTitle("Exam List");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exam_scores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefreshExamScoresList);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getExamsShow();
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
        getExamsShow();
    }
    private void getExamsShow(){
        listView = (ListView) getView().findViewById(R.id.tchExamListView);
        Call<ArrayList<Exam>> callExamList = teacherDashboardApi.getExamList(teacher.getDept());
        callExamList.enqueue(new Callback<ArrayList<Exam>>() {
            @Override
            public void onResponse(Call<ArrayList<Exam>> call, Response<ArrayList<Exam>> response) {
                if(response.isSuccessful()) {
                    Log.i("Exam List Fetch", "Success");
                    examList = response.body();

                    // Create the adapter to convert the array to views
                    adapter = new ExamScoresAdapter(getContext(), examList);

                    // Attach the adapter to a ListView
                    listView.setAdapter(adapter);
                    dialog.dismiss();

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (!examList.get(i).isAttempted()) {
                                SeeScoreAlert(i);
                            } else {
                                Toast.makeText(getContext(), "Exam not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Exam>> call, Throwable t) {
                Log.e("Fetch Exam List","FAILURE");
                dialog.dismiss();
                ((DashboardActivity)getActivity()).toEmptyFragment("Some error occurred");
            }
        });
    }

    private void SeeScoreAlert(int i)
    {
        Exam exam = adapter.getItem(i);
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set the message show for the Alert time
        String message = "\nExam Name : " + exam.getExamName() + "\n\nDo you wish to see scores ?\n";
        builder.setMessage(message);
        // Set Alert Title
        builder.setTitle("Exam Scores");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            ((DashboardActivity)getActivity()).toScoresFragment(exam);
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

}