package com.sairaj.review_pro;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Add_Review extends AppCompatActivity implements LocationListener {

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
    String user_id, location;
String city;
    double longitude, latitude;
    LocationManager locationManager = null;


    @RequiresApi(api = Build.VERSION_CODES.M)
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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);


        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        Log.e("Cuurent_User", user_id);
        Glide.with(Add_Review.this).load(images).into(image);


        Log.e("longitude", "" + longitude);
        Log.e("latitude", "" + latitude);

        loc_fun(location);


        FirebaseDatabase.getInstance().getReference("Users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("User_name").getValue().toString();
                image_uri = dataSnapshot.child("Image_Uri").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rating = String.valueOf(ratingBar.getRating());
                String review_text = review.getText().toString();

                if (rating.isEmpty() || review_text.isEmpty()) {

                } else {

                    Map<String, Object> map = new HashMap<>();
                    map.put("Rating", rating);
                    map.put("Review", review_text);
                    map.put("user_name", userName);
                    map.put("Image", image_uri);
                    map.put("Location",city);


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

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void loc_fun(Location location) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            String addres=addresses.get(0).getAddressLine(0);
         city=addresses.get(0).getLocality();
            Log.e("Location Name", city);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
