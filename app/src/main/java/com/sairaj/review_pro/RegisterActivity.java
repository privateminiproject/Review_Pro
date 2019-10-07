package com.sairaj.review_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    public EditText emailId, passwd, userName;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    // Write a message to the database

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    ProgressBar progressBar;
    private Toolbar mToolBar;
    private FirebaseAuth mAuth;
    CircleImageView candidate_image;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    CropImage.ActivityResult result;
    private StorageReference mStorageRef;
    String user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.email);
        userName = findViewById(R.id.userName);
        passwd = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.btnSignUp);
        signIn = findViewById(R.id.SignIn);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        candidate_image = findViewById(R.id.Product_image);
        mStorageRef = FirebaseStorage.getInstance().getReference();



        candidate_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(RegisterActivity.this);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageUri != null) {

                    final String emailID = emailId.getText().toString();
                    final String paswd = passwd.getText().toString();
                    final String user_name = userName.getText().toString();


                    if (emailID.isEmpty()) {
                        emailId.setError("Provide your Email first!");
                        emailId.requestFocus();
                    } else if (paswd.isEmpty()) {
                        passwd.setError("Set your password");
                        passwd.requestFocus();
                    } else if (user_name.isEmpty()) {
                        userName.setError("Set your UserName");
                        userName.requestFocus();
                    } else if (emailID.isEmpty() && paswd.isEmpty() && user_name.isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                    } else if (!(emailID.isEmpty() && paswd.isEmpty() && user_name.isEmpty())) {

                        progressBar.setVisibility(View.VISIBLE);
                        emailId.setFocusable(false);
                        passwd.setFocusable(false);
                        userName.setFocusable(false);
                        candidate_image.setOnClickListener(null);

                        final StorageReference filepath = mStorageRef.child("Users_Profile")
                                .child(user_name + "" + random() + ".jpg");


                        firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if (!task.isSuccessful()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(RegisterActivity.this, "Please Enter Correct Information.", Toast.LENGTH_SHORT).show();
                                } else {

                                    mAuth = FirebaseAuth.getInstance();
                                    user_id = mAuth.getCurrentUser().getUid();
                                    Log.e("Cuurent_User", user_id);

                                    filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    Uri uris = uri;
                                                    final String download_url = uri.toString();
                                                    Log.e("Download_URI", download_url);
                                                    progressBar.setVisibility(View.INVISIBLE);

                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("id", databaseReference.getKey());
                                                    map.put("Email", emailID);
                                                    map.put("User_name", user_name);
                                                    map.put("Users_Password", paswd);
                                                    map.put("Image_Uri", download_url);
                                                    databaseReference.setValue(map);

                                                    Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(in);
                                                    finish();
                                                    progressBar.setVisibility(View.INVISIBLE);



                                                }
                                            });


                                        }
                                    });



                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Please Select Your Profile Picture.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void Login(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                candidate_image.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
