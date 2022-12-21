package com.project.examapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {

        super.onStart();
        user = mAuth.getCurrentUser();

        toStudentDashboard();

        ImageButton signout = findViewById(R.id.logOutB);
        ImageButton backButton = findViewById(R.id.backB);

        signout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAuth.signOut();
                Log.d(TAG, "Signed out");
                Intent sOut = new Intent(DashboardActivity.this,MainActivity.class);
                startActivity(sOut);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toStudentDashboard();
            }
        });
    }

    public void examListFrag(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_dashboard, new StudentExamListFragment());
        transaction.commit();
    }

    public void profileFrag(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("email", user.getEmail());
        bundle.putString("name",user.getDisplayName());
        ProfileFragment fragUser = new ProfileFragment();
        fragUser.setArguments(bundle);
        transaction.replace(R.id.fragment_dashboard, fragUser);
        transaction.commit();
    }

    public void toStudentDashboard()
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_dashboard, new StudentDashboardFragment());
        transaction.commit();
    }
}
