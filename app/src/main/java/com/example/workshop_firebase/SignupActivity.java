package com.example.workshop_firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {

    private TextView txtlogin;
    private Button btnregister;
    private EditText editemailregister, editpasswordregister, editusername, editconfirmpassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtlogin = (TextView)findViewById(R.id.txtlogin);
        btnregister = (Button)findViewById(R.id.btnregister);
        editemailregister = (EditText)findViewById(R.id.editemailregister);
        editpasswordregister = (EditText)findViewById(R.id.editpasswordregister);
        editusername = (EditText)findViewById(R.id.editusername);
        editconfirmpassword = (EditText)findViewById(R.id.editconfirmpassword);

        //initialize firebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //to login
        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLogin = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(toLogin);
            }
        });


        //register
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email, password, username, confirmpassword;
                email = editemailregister.getText().toString().trim();
                password = editpasswordregister.getText().toString().trim();
                username = editusername.getText().toString().trim();
                confirmpassword = editconfirmpassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty() || username.isEmpty() || confirmpassword.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Fill all field", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password.equals(confirmpassword)){
                    Toast.makeText(SignupActivity.this, "Passwords not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){


                            FirebaseUser user = task.getResult().getUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User profile updated.");
                                            }
                                        }
                                    });

                            mAuth.signOut();

                            Intent toLogin = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(toLogin);

                        }else{
                            Toast.makeText(SignupActivity.this, "The user is already exist", Toast.LENGTH_SHORT).show();
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
            Intent toPosts = new Intent(SignupActivity.this, PostActivity.class);
            startActivity(toPosts);
        }
    }
}
