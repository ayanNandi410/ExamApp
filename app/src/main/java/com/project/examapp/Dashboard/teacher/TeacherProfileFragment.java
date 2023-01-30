package com.project.examapp.Dashboard.teacher;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.R;
import com.project.examapp.models.Student;
import com.project.examapp.models.Teacher;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherProfileFragment extends Fragment {

    Teacher user;

    public TeacherProfileFragment(Teacher teacher) {
        user = teacher;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((DashboardActivity) getActivity()).setTitle("Profile");
        TextView emailView = view.findViewById(R.id.user_email);
        TextView nameView = view.findViewById(R.id.userName);
        TextView deptV = view.findViewById(R.id.user_dept);
        TextView addrV = view.findViewById(R.id.user_address);
        TextView phno = view.findViewById(R.id.user_phone);
        CircleImageView img = view.findViewById(R.id.profileImgCategory);

        emailView.setText(user.getEmail());
        nameView.setText(user.getName());
        addrV.setText(user.getAddress());
        phno.setText(user.getMobNo());
        deptV.setText(user.getDept());

        String gender = user.getGender();
        switch (gender)
        {
            case "M": img.setImageResource(R.drawable.male_tr);
                break;
            case "F": img.setImageResource(R.drawable.female_tr);
                break;
            case "other": img.setImageResource(R.drawable.profile);
                break;
        }
    }
}