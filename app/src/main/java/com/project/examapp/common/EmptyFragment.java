package com.project.examapp.common;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class EmptyFragment extends Fragment {

    String text = "Nothing to Show";
    String back;
    public EmptyFragment(String text,String back) {
        this.text = text;
        this.back = back;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                if(back.equals("dashboard"))
                {
                    ((DashboardActivity)getActivity()).toDashboard();
                }
                else if(back.equals("ExamScores"))
                {
                    ((DashboardActivity)getActivity()).examScoresFrag();
                }
                else
                {
                    ((DashboardActivity)getActivity()).HomeBackPress();
                }

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
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