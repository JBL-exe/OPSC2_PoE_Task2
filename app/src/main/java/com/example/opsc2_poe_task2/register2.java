package com.example.opsc2_poe_task2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class register2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore fs;
    private FirebaseAuth fAuth;
    private FirebaseStorage fbStore;
    ImageView dpDisplay;
    private static final int IMAGE_SELECT = 1;
    Uri imageUri;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        Spinner UnitSpin = (Spinner) findViewById(R.id.UnitSpinner);
        Spinner lmSpin = (Spinner) findViewById(R.id.landmarkSpinner);

        fs = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseStorage.getInstance();


        Button RegFinish = (Button) findViewById(R.id.btnRegFin);
        Button setPicture = (Button) findViewById(R.id.btnSelectDP);
        dpDisplay = findViewById(R.id.imageView);

        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UnitSpin.setAdapter(unitAdapter);
        UnitSpin.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> lmAdapter = ArrayAdapter.createFromResource(this,
                R.array.landmarks_array, android.R.layout.simple_spinner_item);
        lmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lmSpin.setAdapter(lmAdapter);
        lmSpin.setOnItemSelectedListener(this);



        RegFinish.setOnClickListener(view -> {
            Intent RegFinish1 = new Intent(getApplicationContext(),MainScreen.class);
            startActivity(RegFinish1);
        });
        setPicture.setOnClickListener(view -> {

            Intent selectImageIntent = new Intent();
            selectImageIntent.setType("image/*");
            selectImageIntent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(selectImageIntent,"Select display image"), IMAGE_SELECT);

            });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECT && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bdata = baos.toByteArray();
                    String path = "UsersDP/" + UUID.randomUUID() + ".png";
                    StorageReference userDPRef = fbStore.getReference(path);
                    UploadTask ut = userDPRef.putBytes(bdata);
                    dpDisplay.setImageBitmap(bitmap);
                    }catch (IOException ioe){
                    ioe.printStackTrace();
                }

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selection = adapterView.getItemAtPosition(i).toString();
        int count = 0;
        if (selection.equals("Unit Preference") || selection.equals("Landmark Preference"))
        {
            Toast.makeText(adapterView.getContext(),"Please select your choice.", Toast.LENGTH_SHORT).show();
        }
        else if (selection.equals("Metric")||selection.equals("Imperial"))
        {
            String userID;
            userID = fAuth.getCurrentUser().getUid();
            Map<String,Object> user = new HashMap<>();
            user.put("UOM", selection);
            DocumentReference docRef;
            fs.collection("users").document(userID).set(user, SetOptions.merge());
            ++count;
        }
        else
        {
            String userID;
            userID = fAuth.getCurrentUser().getUid();
            Map<String,Object> user = new HashMap<>();
            user.put("PrefLandmark", selection);
            DocumentReference docRef;
            fs.collection("users").document(userID).set(user, SetOptions.merge());
            ++count;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(adapterView.getContext(),"Please select your choices.", Toast.LENGTH_SHORT).show();
    }
}