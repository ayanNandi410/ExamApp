package com.project.examapp.Dashboard.student;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.R;
import com.project.examapp.models.Student;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfileFragment extends Fragment {

    Student userStudent;

    public StudentProfileFragment(Student student) {
        userStudent = student;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        return inflater.inflate(R.layout.fragment_student_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((DashboardActivity) getActivity()).setTitle("Profile");
        TextView emailView = view.findViewById(R.id.user_email);
        TextView nameView = view.findViewById(R.id.userName);
        TextView yearV = view.findViewById(R.id.user_year);
        TextView addrV = view.findViewById(R.id.user_address);
        TextView phno = view.findViewById(R.id.user_phone);
        CircleImageView img = view.findViewById(R.id.profileImgCategory);

        emailView.setText(userStudent.getEmail());
        nameView.setText(userStudent.getName());
        yearV.setText(userStudent.getYear());
        addrV.setText(userStudent.getAddress());
        phno.setText(userStudent.getMobNo());

        String gender = userStudent.getGender();
        switch (gender)
        {
            case "M": img.setImageResource(R.drawable.male_st);
                    break;
            case "F": img.setImageResource(R.drawable.female_st);
                break;
            case "other": img.setImageResource(R.drawable.profile);
                break;
        }
    }
}