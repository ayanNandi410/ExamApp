package com.project.examapp.common;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmptyFragment extends Fragment {

    String text = "Nothing to Show";

    public EmptyFragment(String text) {
        this.text = text;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.textToShow);
        textView.setText(text);
    }
}