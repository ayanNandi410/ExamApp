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

import com.project.examapp.MainActivity;
import com.project.examapp.R;

public class RegisterFragment extends Fragment {

    EditText text_email, text_pswd, text_name, text_rep_pswd;
    Button btnClick, btnLogin;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        text_email = view.findViewById(R.id.et_email);
        text_pswd = view.findViewById(R.id.et_password);
        text_name = view.findViewById(R.id.et_name);
        text_rep_pswd = view.findViewById(R.id.et_repassword);
        btnClick = view.findViewById(R.id.btn_register);
        btnLogin = view.findViewById(R.id.goToLogin);

        btnClick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(CheckAllFields()) {
                    ((MainActivity) getActivity()).createAccount(text_email.getText().toString(),text_pswd.getText().toString(),text_name.getText().toString());
                    Log.d(TAG, "Account added");
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)getActivity()).sign_in();
            }
        });
    }

    private boolean CheckAllFields() {
        if (text_name.length() == 0) {
            text_name.setError("This field is required");
            return false;
        }

        if (text_email.length() == 0) {
            text_email.setError("This field is required");
            return false;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(text_email.getText().toString()).matches()){
            text_email.setError("Not an email address");
            return false;
        }

        if (text_pswd.length() == 0) {
            text_pswd.setError("Password is required");
            return false;
        } else if (text_pswd.length() < 8) {
            text_pswd.setError("Password must be minimum 8 characters");
            return false;
        }

        if((text_pswd.length()!=0)&&(text_rep_pswd.length()!=0)&&(text_rep_pswd.getText().toString().compareTo(text_pswd.getText().toString())!=0)){
            text_rep_pswd.setError("Two passwords do not match");
            return false;
        }

        // after all validation return true.
        return true;
    }


}