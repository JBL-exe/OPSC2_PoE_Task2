package com.example.opsc2_poe_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class register1 extends AppCompatActivity {
    private FirebaseFirestore fs;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        fs = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        Button RegNext = (Button) findViewById(R.id.btnRegNext);
        TextView FirstName = findViewById(R.id.txtFirstName);
        TextView LastName = findViewById(R.id.txtLastName);
        TextView Email = findViewById(R.id.txtEmail);
        TextView Password = findViewById(R.id.txtPassword);
        TextView ConfPass = findViewById(R.id.txtPasswordConf);

        if (Password.getText() != null)
        {
            ConfPass.setVisibility(View.VISIBLE);
        }
        else
        {
            ConfPass.setVisibility(View.INVISIBLE);
        }
        RegNext.setOnClickListener(view -> RegLoop());



    }
    private void RegLoop() {

        TextView FirstName = findViewById(R.id.txtFirstName);
        TextView LastName = findViewById(R.id.txtLastName);
        TextView Email = findViewById(R.id.txtEmail);
        TextView Password = findViewById(R.id.txtPassword);
        TextView ConfPass = findViewById(R.id.txtPasswordConf);

        final Context context = getApplicationContext();
        final int duration = Toast.LENGTH_LONG;

        int count = 0;

        final String fName = FirstName.getText().toString();
        final String lName = LastName.getText().toString();
        final String email = Email.getText().toString();
        final String pass = Password.getText().toString();
        String confpass = ConfPass.getText().toString();

        if (TextUtils.isEmpty(fName))
        {
            FirstName.setError("Name is required.");
            return;
        }
        else
        {
            ++count;
        }
        if (TextUtils.isEmpty(lName))
        {
            LastName.setError("Surname is required.");
            return;
        }
        else
        {
            ++count;
        }
        if (TextUtils.isEmpty(email))
        {
            Email.setError("Email is required.");
            return;

        }
        else
        {
            ++count;
        }
        if (pass.length() < 6)
        {
            Password.setError("Password must be >= 6 characters.");
            return;
        }
        else
        {
            ++count;
        }
        if (TextUtils.isEmpty(confpass))
        {
            ConfPass.setError("Please re-enter your password.");
            return;
        }
        else
        {
            ++count;
        }
        if (pass.equals(confpass))
        {
            ++count;
        }
        else
        {
            CharSequence CS = "Passwords do not match!";
            Toast.makeText(context, CS, duration).show();
        }
        if (count == 6)
        {

            fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
                String userID;
                if (task.isSuccessful())
                {
                    userID = fAuth.getCurrentUser().getUid();

                    Map<String,Object> user = new HashMap<>();
                    user.put("First Name",fName);
                    user.put("Surname",lName);
                    user.put("Email",email);
                    user.put("Password",pass);

                    DocumentReference documentReferance;
                    fs.collection("users").document(userID).set(user, SetOptions.merge());

                    NextReg();
                }
                else{
                    Toast.makeText(register1.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            })


            ;
        }
        else
        {
            CharSequence CS = "Please enter all information. ";
            Toast.makeText(context, CS, duration).show();
        }
    }

    private void NextReg() {
        Intent Reg1Success = new Intent(getApplicationContext(),register2.class);
        startActivity(Reg1Success);
    }
}