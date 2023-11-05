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
import com.google.firebase.auth.FirebaseUser;
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
                try {
                    if(gitEmail == null){
                        Toast.makeText(GithubActivity.this, "Enter your github accounted!!", Toast.LENGTH_SHORT).show();
                    }else {
                        SignInWithGithubProvider(
                                OAuthProvider.newBuilder("github.com")
                                        .addCustomParameter("login",gitEmail.getText().toString())
                                        .setScopes(
                                                new ArrayList<String>() {
                                                    {
                                                        add("user:email");
                                                    }
                                                })
                                        .build()
                        );
                    }
                }catch (Exception e){
                    Toast.makeText(GithubActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SignInWithGithubProvider(OAuthProvider login) {
        Task<AuthResult> pendingAuthTask = fAuth.getPendingAuthResult();
        if (pendingAuthTask != null) {
            pendingAuthTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(GithubActivity.this, "User exist", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GithubActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            fAuth.startActivityForSignInWithProvider(this,login).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GithubActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = fAuth.getCurrentUser();
                    Intent intent = new Intent(GithubActivity.this, HomeActivity.class);
                    intent.putExtra("githubname",user.getEmail());
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    public void loginForm (){
        Intent intent = new Intent(GithubActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}