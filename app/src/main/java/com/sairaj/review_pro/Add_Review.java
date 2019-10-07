package com.sairaj.review_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Add_Review extends AppCompatActivity {

    String name, images, desc, id, Product_id;
    String userName;

    Button add_review;
    EditText review;
    RatingBar ratingBar;
    private FirebaseAuth mAuth;
    ImageView image;
    String image_uri, userNames;
    DatabaseReference myRef;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__review);


        image = findViewById(R.id.product_images);
        ratingBar = findViewById(R.id.ratingBar);
        review = findViewById(R.id.editText2);
        add_review = findViewById(R.id.Add_Review);

        Intent intent4 = getIntent();
        name = intent4.getStringExtra("names");
        Product_id = intent4.getStringExtra("Product_id");
        Log.e("Product_id_review", Product_id);
        images = intent4.getStringExtra("images");

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        Log.e("Cuurent_User", user_id);
        Glide.with(Add_Review.this).load(images).into(image);



        FirebaseDatabase.getInstance().getReference("Users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName=dataSnapshot.child("User_name").getValue().toString();
                image_uri=dataSnapshot.child("Image_Uri").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rating=String.valueOf(ratingBar.getRating());
                String review_text = review.getText().toString();

                if (rating.isEmpty() || review_text.isEmpty()) {

                } else {

                    Map<String, Object> map=new HashMap<>();
                    map.put("Rating",rating);
                    map.put("Review",review_text);
                    map.put("user_name",userName);
                    map.put("Image",image_uri);


                    FirebaseDatabase.getInstance().getReference("Review").child(Product_id).push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(Add_Review.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "Product Added", Toast.LENGTH_LONG).show();

                        }
                    });

                }


            }
        });



    }
}
