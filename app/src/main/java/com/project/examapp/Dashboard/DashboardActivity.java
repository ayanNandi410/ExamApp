package com.project.examapp.Dashboard;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Api.UserApi;
import com.project.examapp.Authentication.MainActivity;
import com.project.examapp.Dashboard.student.StudentProfileFragment;
import com.project.examapp.Dashboard.student.StudentTeachersListFragment;
import com.project.examapp.Dashboard.teacher.ExamScoresFragment;
import com.project.examapp.Dashboard.teacher.ScoresFragment;
import com.project.examapp.Dashboard.teacher.StudentsListFragment;
import com.project.examapp.Dashboard.teacher.TeacherProfileFragment;
import com.project.examapp.common.EmptyFragment;
import com.project.examapp.common.ProgressBarFragment;
import com.project.examapp.R;
import com.project.examapp.Dashboard.student.StudentDashboardFragment;
import com.project.examapp.Dashboard.student.StudentExamListFragment;
import com.project.examapp.Dashboard.teacher.TeacherDashboardFragment;
import com.project.examapp.models.Exam;
import com.project.examapp.models.Student;
import com.project.examapp.models.Teacher;
import com.project.examapp.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String type = "none";
    private boolean typeSetOrNot = false;
    private boolean homeActive = true;
    private Student student;
    private Teacher teacher;
    private User dashboardUser;
    private UserApi userApi;
    private RetrofitClient client;
    private ProgressDialog dialog;
    private TextView title;
    Handler handler;
    Runnable FetchDetailsTask = new Runnable() {
        @Override
        public void run() {
            if(!typeSetOrNot)
            {
                getUserDetails();
            }
            handler.postAtTime(this,SystemClock.uptimeMillis()+8000 );
            }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        handler = new Handler();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        client = RetrofitClient.getInstance();
        userApi = client.getRetrofit().create(UserApi.class);
        student = null;
        teacher = null;
        typeSetOrNot = false;
        type = "none";
    }

    @Override
    protected void onStart() {

        super.onStart();
        dialog = ProgressDialog.show(DashboardActivity.this, "",
                "Loading.. Please wait...", true);
        dialog.show();
        startGettingDetails();


        ImageButton signOut = findViewById(R.id.logOutB);
        ImageButton backButton = findViewById(R.id.backB);
        title = findViewById(R.id.textView);
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

    @Override
    public void onResume()
    {
        super.onResume();
        dialog.dismiss();
    }

    private void startGettingDetails()
    {
        handler.removeCallbacks(FetchDetailsTask);
        handler.postDelayed(FetchDetailsTask, 100);
    }

    private void getUserDetails()
    {
        Call<User> callUser = userApi.getUser(user.getEmail());
        callUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    Log.i("User fetch","Success");
                    dashboardUser = response.body();
                    type = dashboardUser.getType();
                    Log.i("User Type",type);
                    if(type.equals("student"))
                    {
                        student = dashboardUser.getStudent();
                        typeSetOrNot = true;
                        toDashboard();
                    }
                    else if(type.equals("teacher"))
                    {
                        teacher = dashboardUser.getTeacher();
                        typeSetOrNot = true;
                        toDashboard();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("User fetch","Failure");
            }
        });
    }

    public void setTitle(String titleText){
        title.setText(titleText);
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

    public void studentsListFrag(){
        homeActive = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_dashboard, new StudentsListFragment(teacher));
        transaction.commit();
    }

    public void subjectsFrag(){
        homeActive = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_dashboard, new SubjectsFragment(student,teacher));
        transaction.commit();
    }

    public void stProfileFrag(){
        homeActive = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        StudentProfileFragment fragUser = new StudentProfileFragment(student);
        transaction.replace(R.id.fragment_dashboard, fragUser);
        transaction.commit();
    }

    public void trProfileFrag(){
        homeActive = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        TeacherProfileFragment fragUser = new TeacherProfileFragment(teacher);
        transaction.replace(R.id.fragment_dashboard, fragUser);
        transaction.commit();
    }

    private void toDashboard(){
        handler.removeCallbacks(FetchDetailsTask);
        title.setText("Dashboard");
        homeActive = true;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(type.equals("student")){
            dialog.dismiss();
            transaction.replace(R.id.fragment_dashboard, new StudentDashboardFragment(student));
        }
        else if(type.equals("teacher")){
            dialog.dismiss();
            transaction.replace(R.id.fragment_dashboard, new TeacherDashboardFragment(teacher));
        }
        transaction.commit();
    }

    public void examScoresFrag()
    {
        homeActive = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ExamScoresFragment examScoresFragment = new ExamScoresFragment(teacher);
        transaction.replace(R.id.fragment_dashboard, examScoresFragment);
        transaction.commit();
    }

    public void toScoresFragment(Exam exam)
    {
        homeActive = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ScoresFragment scoresFragment = new ScoresFragment();
        Bundle bundle = new Bundle();
        bundle.putString("exam-id",exam.getExam_id());
        scoresFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_dashboard, scoresFragment);
        transaction.commit();
    }

    public void toEmptyFragment(String text)
    {
        homeActive = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        EmptyFragment fragment = new EmptyFragment(text);
        transaction.replace(R.id.fragment_dashboard, fragment);
        transaction.commit();
    }
}
