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


public class StudentDashboardFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public StudentDashboardFragment() {
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
        return inflater.inflate(R.layout.fragment_student_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView stName = view.findViewById(R.id.studentName);
        TextView stEmail = view.findViewById(R.id.studentEmail);
        TextView stId = view.findViewById(R.id.studentId);
        CardView examsCrd = view.findViewById(R.id.stExamsCard);
        CardView profileCrd = view.findViewById(R.id.stProfileCard);

        stName.setText(currentUser.getDisplayName());
        stEmail.setText(currentUser.getEmail());
        stId.setText(currentUser.getPhoneNumber());
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

    }
}