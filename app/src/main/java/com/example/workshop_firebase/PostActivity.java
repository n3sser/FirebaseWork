package com.example.workshop_firebase;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private Button btnlogout, btnpost;
    private EditText editpost;
    private TextView txtusername;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ListView lstposts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btnlogout = (Button) findViewById(R.id.btnlogout);
        btnpost = (Button) findViewById(R.id.btnpost);
        editpost = (EditText) findViewById(R.id.editpost);
        txtusername = (TextView) findViewById(R.id.txtusername);
        lstposts = (ListView) findViewById(R.id.lstposts);

        //initilaize firebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //intilaize real time firebase
        myRef = FirebaseDatabase.getInstance().getReference("Posts");

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent toLogin = new Intent(PostActivity.this, MainActivity.class);
                startActivity(toLogin);
            }
        });


        //write a post to firebase
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strpost = editpost.getText().toString().trim();
                String username = mAuth.getCurrentUser().getDisplayName();

                Posts post = new Posts(strpost, username);

                myRef.push().setValue(post).addOnCompleteListener(PostActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(PostActivity.this, "Post added successfuly", Toast.LENGTH_SHORT).show();
                            editpost.setText("");

                        } else {
                            Toast.makeText(PostActivity.this, "Check the internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //show all post on list view

        myRef.addChildEventListener(new ChildEventListener() {
            List<String> list = new ArrayList<>();
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //Posts post = dataSnapshot.getValue(Posts.class);


                String name = dataSnapshot.child("username").getValue(String.class);
                String poststr = dataSnapshot.child("post").getValue(String.class);
                list.add(name + "\n" + poststr + "\n");



                /*YOUR CHOICE OF COLOR*/




                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PostActivity.this, android.R.layout.simple_list_item_1, list);
                lstposts.setAdapter(arrayAdapter);




                //list.clear();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if(user==null){
            Intent toLogin = new Intent(PostActivity.this, MainActivity.class);
            startActivity(toLogin);
        }else{
            txtusername.setText("Welcome " + user.getDisplayName());
        }
    }
}
