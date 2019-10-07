package com.sairaj.review_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.github.florent37.picassopalette.PicassoPalette;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class Product_review extends AppCompatActivity {

    public TextView candidate_Disc;
    TextView candidate_name;
    ImageView candidate_image;
    private FirebaseRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    String names, desc, id, images, product_id;
    private Toolbar mToolBar;

    RecyclerView comment_list;
    String pushKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_review);
        comment_list = findViewById(R.id.comment_list);
        mToolBar = findViewById(R.id.appbar);
        candidate_name = findViewById(R.id.Product_name);
        candidate_image = findViewById(R.id.product_image);
        candidate_Disc = findViewById(R.id.product_description);

        linearLayoutManager = new LinearLayoutManager(this);
        comment_list.setLayoutManager(linearLayoutManager);
        comment_list.setHasFixedSize(true);

//        pushKey=random();


        Intent intent3 = getIntent();
        names = intent3.getStringExtra("name");
        images = intent3.getStringExtra("image");
        desc = intent3.getStringExtra("description");
        product_id = intent3.getStringExtra("Product_id");
        fetch();

        Log.e("product_id",product_id);

        candidate_Disc.setText(desc);

        candidate_name.setText(names);

        Picasso.get().load(images)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(candidate_image,
                PicassoPalette.with(images, candidate_image)
//                                .use(PicassoPalette.Profile.MUTED_DARK)
//                                .intoBackground(backgroundGroup, PicassoPalette.Swatch.RGB)
                        .use(PicassoPalette.Profile.MUTED_DARK)
                        .intoBackground(candidate_image, PicassoPalette.Swatch.RGB)
                        .intoTextColor(candidate_name, PicassoPalette.Swatch.BODY_TEXT_COLOR)
                        .intoTextColor(candidate_Disc, PicassoPalette.Swatch.TITLE_TEXT_COLOR)
        );
    }

    public void add_Review(View view) {
        Intent intent = new Intent(Product_review.this, Add_Review.class);
        intent.putExtra("names", names);
        intent.putExtra("images", images);
//        intent.putExtra("description", desc);
        intent.putExtra("Product_id", product_id);
        startActivity(intent);
    }


    public void fetch() {

        Query query = FirebaseDatabase.getInstance().getReference("Review").child(product_id);

        FirebaseRecyclerOptions<Review> options =
                new FirebaseRecyclerOptions.Builder<Review>()
                        .setQuery(query, new SnapshotParser<Review>() {
                            @NonNull
                            @Override
                            public Review parseSnapshot(@NonNull DataSnapshot snapshot) {

                                return new Review(

                                        snapshot.child("Image").getValue().toString(),
                                        snapshot.child("Rating").getValue().toString(),
                                        snapshot.child("Review").getValue().toString(),
                                        snapshot.child("user_name").getValue().toString()


                                );
                            }
                        }).build()
                ;

        adapter =new FirebaseRecyclerAdapter<Review,ReviewHolder>(options) {
            @NonNull
            @Override
            public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.review_layout, parent, false);
                return new ReviewHolder(view);


            }

            @Override
            protected void onBindViewHolder(@NonNull ReviewHolder holder, int i, @NonNull Review review) {

                holder.user.setText(review.getUser_name());
                holder.review.setText(review.getReview());
                Glide.with(Product_review.this).load(review.getImage()).into(holder.userImage);

            }
        };
        adapter.notifyDataSetChanged();
        comment_list.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

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
}
