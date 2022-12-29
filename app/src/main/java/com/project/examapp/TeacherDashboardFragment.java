package com.project.examapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.examapp.Authentication.LoginFragment;


public class TeacherDashboardFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public TeacherDashboardFragment() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tchName = view.findViewById(R.id.teacherName);
        TextView tchEmail = view.findViewById(R.id.teacherEmail);
        TextView tchId = view.findViewById(R.id.teacherId);
        CardView examsCrd = view.findViewById(R.id.trExamsCard);
        CardView profileCrd = view.findViewById(R.id.trProfileCard);
        CardView studentsCrd = view.findViewById(R.id.studentsCard);
        CardView subjectsCrd = view.findViewById(R.id.trSubjectsCard);

        tchName.setText(currentUser.getDisplayName());
        tchEmail.setText(currentUser.getEmail());
        tchId.setText("Teacher");

        examsCrd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardActivity)getActivity()).examListFrag();
            }
        });

        profileCrd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardActivity)getActivity()).profileFrag();
            }
        });

        studentsCrd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardActivity)getActivity()).teachersListFrag();
            }
        });

        subjectsCrd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardActivity)getActivity()).subjectsFrag();
            }
        });
    }
}