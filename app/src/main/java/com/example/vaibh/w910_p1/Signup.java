package com.example.vaibh.w910_p1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class Signup extends AppCompatActivity {

    private EditText edtUserName, edtPassword, edtConfirmPassword;
    private Button btnSignup;
    private FirebaseAuth mAuth;
    private ProgressBar progressDialog;
    private String email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnSignup = findViewById(R.id.btnSignup);

        progressDialog = findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();

        progressDialog.setVisibility(View.INVISIBLE);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtUserName.getText().toString();
                password = edtPassword.getText().toString();
                confirmPassword = edtConfirmPassword.getText().toString();
                registerWithEmail();
            }
        });
    }

    void registerWithEmail() {
        // Checks if Email is empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter a Username", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords don't Match", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    progressDialog.setVisibility(View.INVISIBLE);
                                    //User is successfully registered.
                                    // Redirects to Login Activity
                                    Intent login = new Intent(getApplicationContext(), Login.class);
                                    startActivity(login);
                                } else {
                                    // Checks if the user is already registered
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(getApplicationContext(), "User is already Registered.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        progressDialog.setVisibility(View.INVISIBLE);
                                    }
                                }

                            }
                        }
                );


    }
}
