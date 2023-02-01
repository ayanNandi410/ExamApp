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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.examapp.Api.FileUploadApi;
import com.project.examapp.Api.RetrofitClient;
import com.project.examapp.R;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadActivity extends AppCompatActivity {

    TextView imgPath;
    private static final int PICK_IMAGE_REQUEST = 9544;
    ImageView image;
    Button btn_upload;
    Uri selectedFile;
    String part_image;

    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                           String contentData;
                            if ("image".equals(mimeType.substring(0,5))) {
                                contentData = MediaStore.Images.Media.DATA;
                            } else if ("video".equals(mimeType.substring(0,5))) {
                                contentData = MediaStore.Video.Media.DATA;
                            } else if ("audio".equals(mimeType.substring(0,5))) {
                                contentData = MediaStore.Audio.Media.DATA;
                            } else {
                                contentData = MediaStore.Files.FileColumns.MEDIA_TYPE;
                            }

                            String[] imageProjection = {contentData};
                            Cursor cursor = getContentResolver().query(selectedFile, imageProjection, null, null, null);
                            if(cursor != null) {
                                cursor.moveToFirst();
                                int indexImage = cursor.getColumnIndex(contentData);
                                part_image = cursor.getString(indexImage);
                                imgPath.setText(part_image);    // Get the image file absolute path
                                Log.d(TAG," URL :" +part_image);
                                Bitmap bitmap = null;
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                image.setImageBitmap(bitmap);                                                       // Set the ImageView with the bitmap of the image
                            }
                        }
                    }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);
        imgPath = findViewById(R.id.item_img);
        //image = findViewById(R.id.img);
        btn_upload = findViewById(R.id.create_item);

        btn_upload.setOnClickListener(v -> {
            uploadImage();
        });
    }

    // Method for starting the activity for selecting image from phone storage
    public void pick(View view) {
        verifyStoragePermissions(FileUploadActivity.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        someActivityResultLauncher.launch(intent);
    }



    // Upload the image to the remote database
    public void uploadImage() {
        File imageFile = new File(part_image);                                                          // Create a file using the absolute path of the image
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("file", imageFile.getName(), reqBody);
        FileUploadApi api = RetrofitClient.getInstance().getAPI();
        Call<ResponseBody> upload = api.uploadImage(partImage);
        upload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(FileUploadActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    /*
                    Intent main = new Intent(Upload.this, MainActivity.class);
                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(main);

                     */
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FileUploadActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
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
}