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
import android.widget.Toast;

import com.project.examapp.Api.UserApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.R;
import com.project.examapp.models.Student;
import com.project.examapp.models.Teacher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    EditText text_email, text_pswd, text_name, text_rep_pswd;
    Button btnClick, btnLogin;
    RadioGroup radioGroup;
    String type;
    RetrofitClient client;
    UserApi userApi;
    Student student;
    Teacher teacher;
    boolean verified;

    public RegisterFragment() {
        type = null;
        verified = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = RetrofitClient.getInstance();
        userApi = client.getRetrofit().create(UserApi.class);
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
        radioGroup = view.findViewById(R.id.radioUserType);

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
                if(CheckAllFields()) {
                    verifyUserAndRegister();
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

    private void verifyUserAndRegister(){
        if(type.equals("student")) {
            Call<Student> callStudent = userApi.getStudentByEmail(text_email.getText().toString());
            callStudent.enqueue(new Callback<Student>() {
                @Override
                public void onResponse(Call<Student> call, Response<Student> response) {
                    if(response.isSuccessful()) {
                        student = response.body();
                        Log.e("student",student.getEmail().toString());
                        if(student.getEmail().equals("notFound"))
                        {
                            Toast.makeText(getContext(), "Student Email Id not found", Toast.LENGTH_SHORT).show();
                        }
                        else if(!(student.getName().toLowerCase()).equals(text_name.getText().toString().toLowerCase()))
                        {
                            Toast.makeText(getContext(), "Student Name not matching", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                            ((MainActivity) getActivity()).createAccount(text_email.getText().toString(),text_pswd.getText().toString(),text_name.getText().toString());
                            Log.d(TAG, "Account added");
                        }
                    }
                }

                @Override
                public void onFailure(Call<Student> call, Throwable t) {
                    Log.e("User Verification","FAILURE");
                }
            });
        }
        else if(type.equals("teacher"))
        {
            Call<Teacher> callTeacher = userApi.getTeacherByEmail(text_email.getText().toString());
            callTeacher.enqueue(new Callback<Teacher>() {
                @Override
                public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                    if(response.isSuccessful()) {
                        teacher = response.body();
                        if(teacher.getEmail().equals("notFound"))
                        {
                            Toast.makeText(getContext(), "Teacher Email Id not found", Toast.LENGTH_SHORT).show();
                        }
                        else if(!(teacher.getName().toLowerCase()).equals(text_name.getText().toString().toLowerCase()))
                        {
                            Toast.makeText(getContext(), "Teacher Name not matching", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                            ((MainActivity) getActivity()).createAccount(text_email.getText().toString(),text_pswd.getText().toString(),text_name.getText().toString());
                            Log.d(TAG, "Account added");
                        }
                    }
                }

                @Override
                public void onFailure(Call<Teacher> call, Throwable t) {
                    Log.e("User Verification","FAILURE");
                }
            });
        }
        else{
            Log.e("User Type","NOT SET");
        }
    }



    private boolean CheckAllFields() {
        if (text_name.length() == 0) {
            text_name.setError("This field is required");
            Toast.makeText(getContext(), "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (text_email.length() == 0) {
            text_email.setError("This field is required");
            Toast.makeText(getContext(), "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(text_email.getText().toString()).matches()){
            text_email.setError("Not an email address");
            Toast.makeText(getContext(), "Not an email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (text_pswd.length() == 0) {
            text_pswd.setError("Password is required");
            Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (text_pswd.length() < 8) {
            text_pswd.setError("Password must be minimum 8 characters");
            Toast.makeText(getContext(), "Password must be minimum 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if((text_pswd.length()!=0)&&(text_rep_pswd.length()!=0)&&(text_rep_pswd.getText().toString().compareTo(text_pswd.getText().toString())!=0)){
            text_rep_pswd.setError("Two passwords do not match");
            Toast.makeText(getContext(), "Two passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(type==null)
        {
            Toast.makeText(getContext(), "User type not selected", Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "User type not selected", Toast.LENGTH_SHORT).show();
            return false;
        }

        // after all validation return true.
        return true;
    }


}