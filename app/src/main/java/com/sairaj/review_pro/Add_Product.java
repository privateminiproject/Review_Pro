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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_Product extends AppCompatActivity {

    ProgressBar progressBar;
    Button add;
    public EditText Product_Name,Product_Disc;
    CircleImageView Product_image;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    CropImage.ActivityResult result;
    private StorageReference mStorageRef;
    private Toolbar mToolBar;

    String userName,userImage;

    String user_email,user_id;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__product);

        mToolBar=findViewById(R.id.appbar);
        progressBar=findViewById(R.id.add_Product_progress);
        progressBar.setVisibility(View.INVISIBLE);
        add=findViewById(R.id.add_Product);
        Product_Name=findViewById(R.id.Product_Name);
        Product_Disc=findViewById(R.id.Product_Disc);
        Product_image=findViewById(R.id.Product_image);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        user_email=mAuth.getCurrentUser().getEmail();

        FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userName = dataSnapshot.child("User_name").getValue().toString();
                userImage = dataSnapshot.child("Image_Uri").getValue().toString();
                Log.e("Name",userName);
                Log.e("image",userImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(2,1)
                        .start(Add_Product.this);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageUri!=null){

                    final String Candidate_Name = Product_Name.getText().toString();
                    final String Candidate_Desc = Product_Disc.getText().toString();
                    if (Candidate_Name.isEmpty()) {
                        Product_Name.setError("Provide your Email first!");
                        Product_Name.requestFocus();
                    } else if (Candidate_Desc.isEmpty()) {
                        Product_Disc.setError("Set your password");
                        Product_Disc.requestFocus();
                    } else if (Candidate_Name.isEmpty() && Candidate_Desc.isEmpty()) {
                        Toast.makeText(Add_Product.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                    } else if (!(Candidate_Name.isEmpty() && Candidate_Desc.isEmpty())) {
                        progressBar.setVisibility(View.VISIBLE);
                        Product_image.setOnClickListener(null);
                        Product_Name.setFocusable(false);
                        Product_Disc.setFocusable(false);


                        final StorageReference filepath=mStorageRef.child("Images").child(Candidate_Name+""+random()+".jpg");

                        filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()){

                                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri uris=uri;
                                            String download_url=uri.toString();
                                            Log.e("ImageLink",download_url);
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Products").push();
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("Product_id", databaseReference.getKey());
                                            map.put("id", user_id);
                                            map.put("Products_Name", Candidate_Name);
                                            map.put("Products_Description",Candidate_Desc);
                                            map.put("Products_Image",download_url);
                                            map.put("Time", ServerValue.TIMESTAMP);
                                            map.put("Email_id",user_email);
//

                                            //Location part
                                            map.put("Location","Mumbai");
                                            databaseReference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent in=new Intent(Add_Product.this, MainActivity.class);
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
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Add_Product.this, "Error", Toast.LENGTH_SHORT).show();

                    }


                }

            }
        });







    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
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
                Product_image.setImageURI(imageUri);
                Log.e("Name", imageUri.toString());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}
