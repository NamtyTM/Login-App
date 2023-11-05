package com.example.signuplogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class GithubActivity extends AppCompatActivity {
    Button back,logGit;
    EditText gitEmail;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);

        gitEmail = findViewById(R.id.edGithub);
        logGit = findViewById(R.id.btn_login_git);
        back = findViewById(R.id.btn_back);
        fAuth = FirebaseAuth.getInstance();
        //btn back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginForm();
            }
        });
        //btn lg
        logGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = gitEmail.getText().toString();
                try {
                    if(gitEmail == null){
                        Toast.makeText(GithubActivity.this, "Enter your github accounted!!", Toast.LENGTH_SHORT).show();
                    }else {
                        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
                        // Target specific email with login hint.
                        provider.addCustomParameter("login", email);

                        List<String> scopes =
                                new ArrayList<String>() {
                                    {
                                        add("user:email");
                                    }
                                };
                        provider.setScopes(scopes);

                        Task<AuthResult> pendingResultTask = fAuth.getPendingAuthResult();
                        if (pendingResultTask != null) {
                            // There's something already here! Finish the sign-in for your user.
                            pendingResultTask
                                    .addOnSuccessListener(
                                            new OnSuccessListener<AuthResult>() {
                                                @Override
                                                public void onSuccess(AuthResult authResult) {
                                                    // User is signed in.
                                                    // IdP data available in

                                                }
                                            })
                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle failure.
                                                    Toast.makeText(GithubActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                        } else {
                            fAuth
                                    .startActivityForSignInWithProvider(GithubActivity.this, provider.build())
                                    .addOnSuccessListener(
                                            new OnSuccessListener<AuthResult>() {
                                                @Override
                                                public void onSuccess(AuthResult authResult) {
                                                    openNextActivity();
                                                }
                                            })
                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle failure.
                                                    Toast.makeText(GithubActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(GithubActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openNextActivity() {
        Intent intent = new Intent(GithubActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginForm (){
        Intent intent = new Intent(GithubActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}