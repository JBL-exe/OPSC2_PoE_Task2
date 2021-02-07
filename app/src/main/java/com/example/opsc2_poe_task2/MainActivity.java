package com.example.opsc2_poe_task2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegButton = findViewById(R.id.btnRegister);
        final EditText loginUsername = findViewById(R.id.txtLoginUsername);
        final EditText loginPassword = findViewById(R.id.txtLoginPassword);
        final ProgressBar pbLogin = findViewById(R.id.loginProgressBar);

        btnLogin.setOnClickListener(view -> {
            int count = 0;
            String strLoginUsername = loginUsername.getText().toString();
            String strLoginPass = loginPassword.getText().toString();

            Context context = getApplicationContext();
            CharSequence logintext = "Invalid Username or Password";
            int duration = Toast.LENGTH_SHORT;


            if (TextUtils.isEmpty(strLoginUsername))
            {
                loginUsername.setError("Email is required.");
                return;
            }
            else
            {
                ++count;
            }
            if (TextUtils.isEmpty(strLoginPass))
            {
                loginPassword.setError("Password is required.");
                return;
            }
            else
            {
                ++count;
            }
            if (count == 2)
            {
                fAuth.signInWithEmailAndPassword(strLoginUsername, strLoginPass).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this,"Login successful.",Toast.LENGTH_LONG).show();
                        Intent LOGIN = new Intent(getApplicationContext(),MainScreen.class);
                        startActivity(LOGIN);

                        pbLogin.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                       Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG);
                    }
                });

            }
            else
            {
                Toast.makeText(context, logintext, duration);
            }

        });
            btnRegButton.setOnClickListener(view -> {

                Intent RegisterIntent = new Intent(getApplicationContext(),register1.class);
                startActivity(RegisterIntent);
            });
        }

        }
