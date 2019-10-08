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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchActivity extends AppCompatActivity {

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


    EditText search;
    ImageButton search_btn;

    public FirebaseAuth firebaseAuth;
    Button add;

    //    String currentUser;
    private FirebaseAuth mAuth;
    private Toolbar mToolBar;
    int colors;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        search = findViewById(R.id.search_field);
        search_btn = findViewById(R.id.search_btn);

        recyclerView = findViewById(R.id.result_list);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        Feach();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = search.getText().toString();
                Search(searchText);
            }
        });
    }


    public void Search(String string) {

        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Products").orderByChild("Products_Name").startAt(string).endAt(string + "\uf8ff");
        ;

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
//                holder.setUserName(model.getEmail_id());
                Log.e("name", model.getProducts_Name());
                Glide.with(SearchActivity.this).load(model.getProducts_Image()).into(holder.img);
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Product_id = model.getProduct_id();

                        Intent intent = new Intent(SearchActivity.this, Product_review.class);
                        intent.putExtra("name", model.getProducts_Name());
                        intent.putExtra("image", model.getProducts_Image());
                        intent.putExtra("description", model.getProducts_Description());
//                        intent.putExtra("userName",UserName);
                        intent.putExtra("Product_id", Product_id);
//                        intent.putExtra("Product_id",model.getEmail_id());
                        startActivity(intent);

                    }
                });
            }

        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }


    public void Feach() {

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
//                holder.setUserName(model.getEmail_id());
                Log.e("name", model.getProducts_Name());
                Glide.with(SearchActivity.this).load(model.getProducts_Image()).into(holder.img);
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Product_id = model.getProduct_id();

                        Intent intent = new Intent(SearchActivity.this, Product_review.class);
                        intent.putExtra("name", model.getProducts_Name());
                        intent.putExtra("image", model.getProducts_Image());
                        intent.putExtra("description", model.getProducts_Description());
//                        intent.putExtra("userName",UserName);
                        intent.putExtra("Product_id", Product_id);
//                        intent.putExtra("Product_id",model.getEmail_id());
                        startActivity(intent);

                    }
                });
            }

        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }


    public class UserViewHolder extends RecyclerView.ViewHolder {


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }


}
