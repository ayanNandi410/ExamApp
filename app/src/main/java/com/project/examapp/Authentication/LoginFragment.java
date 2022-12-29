package com.project.examapp.Authentication;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.project.examapp.MainActivity;
import com.project.examapp.R;

public class LoginFragment extends Fragment {
    String type;
    public LoginFragment() {
        type="None";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText text_email = view.findViewById(R.id.et_email);
        EditText text_psd = view.findViewById(R.id.et_password);
        Button btnClick = view.findViewById(R.id.btn_login);
        Button btnRegister = view.findViewById(R.id.goToRegister);
        RadioGroup radioGroup = view.findViewById(R.id.radioUserType);

        // Uncheck or reset the radio buttons initially
        radioGroup.clearCheck();

        // Add the Listener to the RadioGroup
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId)
                    {
                        // Get the selected Radio Button
                        RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                        // Check which radio button was clicked
                        switch(radioButton.getId()) {
                            case R.id.radio_student:
                                type = "student";
                                break;
                            case R.id.radio_teacher:
                                type = "teacher";
                                break;
                        }

                    }
                });

        btnClick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity) getActivity()).signIn(text_email.getText().toString(),text_psd.getText().toString(),type);
                Log.d(TAG, "Account added");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)getActivity()).sign_up();
            }
        });

    }
}