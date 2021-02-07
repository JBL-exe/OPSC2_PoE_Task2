package com.example.opsc2_poe_task2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment
{
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fs = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();



        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        final Button btnImp = (Button) view.findViewById(R.id.btnImp);
        final Button btnMet = (Button) view.findViewById(R.id.btnMet);
        RadioButton rbHist = (RadioButton) view.findViewById(R.id.rbHist);
        RadioButton rbMod = (RadioButton) view.findViewById(R.id.rbMod);
        RadioButton rbPop = (RadioButton) view.findViewById(R.id.rbPop);
        RadioGroup rgLM = (RadioGroup) view.findViewById(R.id.radioGroup);

        btnImp.setOnClickListener(view1 -> {
            String userID;
        btnImp.setBackgroundColor(Color.CYAN);
        btnMet.setBackgroundColor(Color.GRAY);
        userID = fAuth.getCurrentUser().getUid();


            Map<String,Object> user = new HashMap<>();
            user.put("UOM","Imperial");

            DocumentReference documentReferance;
            fs.collection("users").document(userID).set(user, SetOptions.merge());
        });
        btnMet.setOnClickListener(view15 -> {
            String userID;
            btnMet.setBackgroundColor(Color.CYAN);
            btnImp.setBackgroundColor(Color.GRAY);

            userID = fAuth.getCurrentUser().getUid();

            Map<String,Object> user = new HashMap<>();
            user.put("UOM","Metric");

            DocumentReference documentReferance;
            fs.collection("users").document(userID).set(user, SetOptions.merge());
        });
        rbHist.setOnClickListener(view12 -> {
            String userID;

            userID = fAuth.getCurrentUser().getUid();

            Map<String,Object> user = new HashMap<>();
            user.put("PrefLandmark","Historical");

            DocumentReference documentReferance;
            fs.collection("users").document(userID).set(user, SetOptions.merge());
        });
        rbMod.setOnClickListener(view13 -> {
            String userID;

            userID = fAuth.getCurrentUser().getUid();

            Map<String,Object> user = new HashMap<>();
            user.put("PrefLandmark","Modern");

            DocumentReference documentReferance;
            fs.collection("users").document(userID).set(user, SetOptions.merge());
        });
        rbPop.setOnClickListener(view14 -> {
            String userID;

            userID = fAuth.getCurrentUser().getUid();

            Map<String,Object> user = new HashMap<>();
            user.put("PrefLandmark","Popular");

            DocumentReference documentReferance;
            fs.collection("users").document(userID).set(user, SetOptions.merge());
        });


        return view;

    }


}
