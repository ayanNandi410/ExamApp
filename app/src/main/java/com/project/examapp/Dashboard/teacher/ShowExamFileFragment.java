package com.project.examapp.Dashboard.teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.examapp.Api.AnswerApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Constants;
import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.R;
import com.project.examapp.models.FileAnswer;
import com.project.examapp.models.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowExamFileFragment extends Fragment {

    private Result StdSubmission;
    private AnswerApi api;
    private RetrofitClient client;
    private String fName;
    private TextView answerText, exmID, stdName, timestamp, btnDownload;
    ProgressDialog dialog;
    public ShowExamFileFragment(Result result) {
        StdSubmission = result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = RetrofitClient.getInstance();
        api = client.getRetrofit().create(AnswerApi.class);

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                ((DashboardActivity)getActivity()).examScoresFrag();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_exam_file, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        exmID = view.findViewById(R.id.answerExamID);
        stdName = view.findViewById(R.id.answerStName);
        btnDownload = view.findViewById(R.id.btn_download);
        answerText = view.findViewById(R.id.answerDetail);
        timestamp = view.findViewById(R.id.answerTimestamp);

        exmID.setText("Exam ID : "+StdSubmission.getExamId());
        stdName.setText("Name : "+StdSubmission.getStudent_name());
        timestamp.setText("Time : "+StdSubmission.getTimestamp());
        setAnswerText();
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPDF(fName);
            }
        });
        dialog = ProgressDialog.show(getContext(), "",
                "Loading.. Please wait...", true);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog.dismiss();
    }

    private void setAnswerText()
    {
        Call<FileAnswer> callExamList = api.getAnswer(StdSubmission.getExamId(),StdSubmission.getStudentId());
        callExamList.enqueue(new Callback<FileAnswer>() {
            @Override
            public void onResponse(Call<FileAnswer> call, Response<FileAnswer> response) {
                if(response.isSuccessful()) {
                    Log.i("Answer List Fetch", "Success");
                    FileAnswer answer = response.body();
                    fName = answer.getFile_name();
                    answerText.setText(answer.getAnswer());
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<FileAnswer> call, Throwable t) {
                Log.e("Fetch Exam List","FAILURE");
                dialog.dismiss();
                ((DashboardActivity)getActivity()).toEmptyFragment("Some error occurred","ExamScores");
            }
        });
    }

    private void downloadPDF(String filename)
    {
        final String url = Constants.URL + "/" + filename;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}