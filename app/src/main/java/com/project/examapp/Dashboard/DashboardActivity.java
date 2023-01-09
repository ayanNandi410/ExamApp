package com.project.examapp.Dashboard;

import static android.content.ContentValues.TAG;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Api.UserApi;
import com.project.examapp.Authentication.MainActivity;
import com.project.examapp.ProgressBarFragment;
import com.project.examapp.R;
import com.project.examapp.Dashboard.student.StudentDashboardFragment;
import com.project.examapp.Dashboard.student.StudentExamListFragment;
import com.project.examapp.Dashboard.teacher.StudentTeachersListFragment;
import com.project.examapp.Dashboard.teacher.TeacherDashboardFragment;
import com.project.examapp.models.Student;
import com.project.examapp.models.Teacher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String type;
    private boolean homeActive = true;
    private Student student;
    private Teacher teacher;
    private UserApi userApi;
    private RetrofitClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        client = RetrofitClient.getInstance();
        userApi = client.getRetrofit().create(UserApi.class);
        student = null;
        teacher = null;
        type = null;
        getUserDetails();
    }

    @Override
    protected void onStart() {

        super.onStart();
        showProgressBar();

        ImageButton signOut = findViewById(R.id.logOutB);
        ImageButton backButton = findViewById(R.id.backB);

        signOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAuth.signOut();
                Log.d(TAG, "Signed out");
                Intent sOut = new Intent(DashboardActivity.this, MainActivity.class);
                finish();
                startActivity(sOut);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(homeActive){
                    BackPress();
                }
                else
                {
                   toDashboard();
                }
            }
        });
    }

    private void getUserDetails()
    {
            Call<Student> callStudent = userApi.getStudentByEmail(user.getEmail());
            callStudent.enqueue(new Callback<Student>() {
                @Override
                public void onResponse(Call<Student> call, Response<Student> response) {
                    if(response.isSuccessful()) {
                        student = response.body();
                        type = "student";
                        Log.i("User Type","student");
                        toDashboard();
                    }
                }

                @Override
                public void onFailure(Call<Student> call, Throwable t) {
                    Log.e("Student","False");
                }
            });

            Call<Teacher> callTeacher = userApi.getTeacherByEmail(user.getEmail());
            callTeacher.enqueue(new Callback<Teacher>() {
                @Override
                public void onResponse(Call<Teacher> call, Response<Teacher> response) {
                    if(response.isSuccessful()) {
                        teacher = response.body();
                        type = "teacher";
                        Log.i("User Type","teacher");
                        toDashboard();
                    }
                }

                @Override
                public void onFailure(Call<Teacher> call, Throwable t) {
                    Log.e("Teacher","False");
                }
            });

    }

    private void BackPress() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Are you sure?");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finishAndRemoveTask();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }

    private void showProgressBar(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_dashboard, new ProgressBarFragment());
        transaction.commit();
    }

    public void examListFrag(){
        homeActive = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_dashboard, new StudentExamListFragment(student));
        transaction.commit();
    }

    public void teachersListFrag(){
        homeActive = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_dashboard, new StudentTeachersListFragment(student));
        transaction.commit();
    }

    public void subjectsFrag(){
        homeActive = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_dashboard, new SubjectsFragment(student));
        transaction.commit();
    }

    public void profileFrag(){
        homeActive = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("email", user.getEmail());
        bundle.putString("name",user.getDisplayName());
        ProfileFragment fragUser = new ProfileFragment();
        fragUser.setArguments(bundle);
        transaction.replace(R.id.fragment_dashboard, fragUser);
        transaction.commit();
    }

    private void toDashboard(){
        homeActive = true;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(type.equals("student") && (student!=null)){
            transaction.replace(R.id.fragment_dashboard, new StudentDashboardFragment(student));
        }
        else if(teacher!=null){
            transaction.replace(R.id.fragment_dashboard, new TeacherDashboardFragment(teacher));
        }
        transaction.commit();
    }
}
