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

import com.project.examapp.R;

public class LoginFragment extends Fragment {

    EditText text_email, text_psd;
    Button btnClick, btnToRegister;

    public LoginFragment() {
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

        text_email = view.findViewById(R.id.et_email);
        text_psd = view.findViewById(R.id.et_password);
        btnClick = view.findViewById(R.id.btn_login);
        btnToRegister = view.findViewById(R.id.goToRegister);

        btnClick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(CheckAllFields()){
                    ((MainActivity) getActivity()).signIn(text_email.getText().toString(),text_psd.getText().toString());
                    Log.d(TAG, "Account added");
                }
            }
        });

        btnToRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)getActivity()).sign_up();
            }
        });

    }


    private boolean CheckAllFields() {

        if (text_email.length() == 0) {
            text_email.setError("This field is required");
            return false;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(text_email.getText().toString()).matches()){
            text_email.setError("Not an email address");
            return false;
        }

        if (text_psd.length() == 0) {
            text_psd.setError("Password is required");
            return false;
        } else if (text_psd.length() < 8) {
            text_psd.setError("Password must be minimum 8 characters");
            return false;
        }

        // after all validation return true.
        return true;
    }
}