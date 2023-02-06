package com.project.examapp.Exam;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.examapp.Api.AnswerApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.Dashboard.DashboardActivity;
import com.project.examapp.R;
import com.project.examapp.models.Attempt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadActivity extends AppCompatActivity {

    TextView imgPath, qs, exmName,exmTime;
    private static final int PICK_IMAGE_REQUEST = 9544;
    ImageView image;
    Button btn_upload;
    Uri selectedFile;
    String part_image, examTime, examId, studentId;
    ProgressDialog dialog;
    AnswerApi api;

    RetrofitClient client;
    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };

    Integer time, hr, min, sec, size;
    long endTime, updatedTime;
    Handler handler;
    Runnable UpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            final long end = endTime;
            updatedTime = endTime;
            long millis = end - SystemClock.uptimeMillis();

            if(millis<0)
            {
                toDashboard();
            }
            else{
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds     = seconds % 60;

                if (seconds < 10) {
                    exmTime.setText("" + minutes + ":0" + seconds);
                } else {
                    exmTime.setText("" + minutes + ":" + seconds);
                }
                handler.postAtTime(this,SystemClock.uptimeMillis()+1000 );
            }
        }
    };

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        selectedFile = data.getData(); // Get the image file URI
                        String mimeType = getContentResolver().getType(selectedFile);
                        Log.d(TAG," URL :" +mimeType);

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedFile, filePathColumn, null, null, null);
                        if(cursor != null) {
                            cursor.moveToFirst();
                            int indexImage = cursor.getColumnIndex(filePathColumn[0]);
                            part_image = cursor.getString(indexImage);
                            imgPath.setText(part_image);    // Get the image file absolute path
                            Log.d(TAG," URL :" +part_image);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        client = RetrofitClient.getInstance();
        api = client.getRetrofit().create(AnswerApi.class);

        qs = findViewById(R.id.questionView);
        imgPath = findViewById(R.id.item_img);
        btn_upload = findViewById(R.id.create_item);
        exmName = findViewById(R.id.examFName);
        exmTime = findViewById(R.id.FExamTime);
        handler = new Handler();

        Bundle b = getIntent().getExtras();
        qs.setText(b.getString("question"));
        examId = b.getString("exam_id");
        studentId = b.getString("student_id");
        time = b.getInt("time");
        exmName.setText(examId);

        btn_upload.setOnClickListener(v -> {
           uploadImage();
        });
        startTimer(time);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toDashboard();
    }

    // Method for starting the activity for selecting image from phone storage
    public void pick(View view) {
        verifyStoragePermissions(FileUploadActivity.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");

        someActivityResultLauncher.launch(intent);
    }

    private void registerAttemptAndSubmit()
    {
        Attempt attempt = new Attempt();
        attempt.setExam_id(examId);
        attempt.setStudent_id(studentId);
        Call<ResponseBody> callAttemptPost = api.postAttempt(attempt);
        callAttemptPost.enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.e("Attempt Registration","SUCCESS");
                    toDashboard();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Submit Answers","FAILURE");
                dialog.dismiss();
                Toast.makeText(FileUploadActivity.this, "Failed to register attempt", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Upload the image to the remote database
    public void uploadImage() {

        if(part_image==null)
        {
            Toast.makeText(FileUploadActivity.this, "Please use different app for uploading", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = ProgressDialog.show(FileUploadActivity.this, "",
                "Submitting.. Please wait...", true);
        dialog.show();

        File imageFile = new File(part_image);// Create a file using the absolute path of the image

        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("file", imageFile.getName(), reqBody);
        RequestBody examID = RequestBody.create(MediaType.parse("text/plain"),examId);
        RequestBody studentID = RequestBody.create(MediaType.parse("text/plain"),studentId);

        String currDateTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        RequestBody timestamp = RequestBody.create(MediaType.parse("text/plain"),currDateTime);

        Call<ResponseBody> upload = api.uploadAnswerImage(partImage, examID,studentID,timestamp);
        upload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.e("File Upload","SUCCESS");
                    Toast.makeText(FileUploadActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                    registerAttemptAndSubmit();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Log.e("File Upload","FAILURE");
                Toast.makeText(FileUploadActivity.this, "File Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTimer(Integer time){
        long interval = (Integer)(time * 60 * 1000);
        endTime = SystemClock.uptimeMillis()+interval;
        handler.removeCallbacks(UpdateTimeTask);
        handler.postDelayed(UpdateTimeTask, 100);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void toDashboard()
    {
        dialog.dismiss();
        Toast.makeText(FileUploadActivity.this, "Exam Ended", Toast.LENGTH_SHORT).show();
        Intent endExamIntent = new Intent(this, DashboardActivity.class);
        this.finish();
        handler.removeCallbacks(UpdateTimeTask);
        startActivity(endExamIntent);
    }
}