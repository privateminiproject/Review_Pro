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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private LinearLayoutManager linearLayoutManager;
    String id = null;
    String name = null;
    String image = null;
    String Product_id = null;
    String description = null;
    String UserName;


    public FirebaseAuth firebaseAuth;
    Button add;

    //    String currentUser;
    private FirebaseAuth mAuth;
    private Toolbar mToolBar;
    int colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        mToolBar = findViewById(R.id.appbar);
        setSupportActionBar(mToolBar);

        mToolBar.setTitle("Review_Pro");

        add = findViewById(R.id.fab);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Add_Product.class);
                startActivity(intent);

            }
        });

        recyclerView = findViewById(R.id.list1);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//            }
//
//        });


    }


    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Products");

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, new SnapshotParser<Model>() {
                            @NonNull
                            @Override
                            public Model parseSnapshot(@NonNull DataSnapshot snapshot) {


                                return new Model(
                                        snapshot.child("Email_id").getValue().toString(),
                                        snapshot.child("Location").getValue().toString(),
                                        snapshot.child("Product_id").getValue().toString(),
                                        snapshot.child("Products_Description").getValue().toString(),
                                        snapshot.child("Products_Image").getValue().toString(),
                                        snapshot.child("Products_Name").getValue().toString(),
                                        snapshot.child("Time").getValue().toString(),
                                        snapshot.child("id").getValue().toString()

                                );
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final ViewHolder holder, final int position, final Model model) {
                holder.setProduct_name(model.getProducts_Name());
                holder.setDesc(model.getProducts_Description());
                holder.setUserName(model.getEmail_id());
                Log.e("name",model.getProducts_Name());
                Glide.with(MainActivity.this).load(model.getProducts_Image()).into(holder.img);
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Product_id = model.getProduct_id();

                        Intent intent = new Intent(MainActivity.this, Product_review.class);
                        intent.putExtra("name", model.getProducts_Name());
                        intent.putExtra("image", model.getProducts_Image());
                        intent.putExtra("description", model.getProducts_Description());
//                        intent.putExtra("userName",UserName);
                        intent.putExtra("Product_id",Product_id);
//                        intent.putExtra("Product_id",model.getEmail_id());

                        startActivity(intent);

                    }
                });
            }

        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();

            Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent2);
            finish();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Search:
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

