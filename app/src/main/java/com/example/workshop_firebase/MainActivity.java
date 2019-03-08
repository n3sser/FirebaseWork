package com.example.workshop_firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button btnlogin;
    private TextView txtregister;
    private EditText editemail, editpassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the views
        btnlogin = (Button)findViewById(R.id.btnlogin);
        txtregister = (TextView)findViewById(R.id.txtregister);
        editemail = (EditText)findViewById(R.id.editemail);
        editpassword = (EditText)findViewById(R.id.editpassword);

        FirebaseApp.initializeApp(MainActivity.this);

        //initialize the firebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //to register
        txtregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegister = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(toRegister);
            }
        });

        //login
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editemail.getText().toString().trim();
                String password = editpassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()) return;

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent toPosts = new Intent(MainActivity.this, PostActivity.class);
                            startActivity(toPosts);
                        }else{
                            Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null){
            Intent toPosts = new Intent(MainActivity.this, PostActivity.class);
            startActivity(toPosts);
        }
    }
}
