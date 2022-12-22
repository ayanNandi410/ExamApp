package com.project.examapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.examapp.Adapters.ExamsAdapter;
import com.project.examapp.Api.ExamApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.models.Exam;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentExamListFragment extends Fragment {

    ArrayList<Exam> examList;
    ExamsAdapter adapter;
    RetrofitClient client;
    ExamApi examApi;
    ListView listView;

    public StudentExamListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrofit call
        client = RetrofitClient.getInstance();
        examApi = client.getRetrofit().create(ExamApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_exam_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Call<ArrayList<Exam>> callExamList = examApi.getExamList();
        callExamList.enqueue(new Callback<ArrayList<Exam>>() {
            @Override
            public void onResponse(Call<ArrayList<Exam>> call, Response<ArrayList<Exam>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), "Exam List fetched", Toast.LENGTH_SHORT).show();
                    examList = response.body();

                    // Create the adapter to convert the array to views
                    adapter = new ExamsAdapter(getContext(), examList);

                    // Attach the adapter to a ListView
                    listView = (ListView) getView().findViewById(R.id.stdExamListView);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Exam>> call, Throwable t) {
                Log.e("Fetch Exam List","FAILURE");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toTakeExam();
            }
        });
    }

    public void toTakeExam()
    {
        Intent takeExamIntent = new Intent(getActivity(),ExamPageActivity.class);
        startActivity(takeExamIntent);
        getActivity().finish();
    }
}