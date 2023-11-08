package com.example.signuplogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SignupActivity extends AppCompatActivity {
    //create object
    public EditText editMail,editPass;
    Button sign;
    TextView regiText;
    FirebaseAuth firebaseAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //get value
        firebaseAuth = FirebaseAuth.getInstance();
        editMail = findViewById(R.id.edEmail);
        editPass = findViewById(R.id.edPassword);
        regiText = findViewById(R.id.registerText);
        sign = findViewById(R.id.btn_signIn);

        //event TextView Already have an account
        regiText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //event btn btn signIn
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password;
                email = String.valueOf(editMail.getText());
                password = String.valueOf(editPass.getText());


                try {
                    if (email.isEmpty()){
                        Toast.makeText(SignupActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(password.isEmpty()){
                        Toast.makeText(SignupActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(password.length()<=6){
                        Toast.makeText(SignupActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "Account created!!.",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(SignupActivity.this, "Account created failed!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }catch (Exception e){
                    Toast.makeText(SignupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}