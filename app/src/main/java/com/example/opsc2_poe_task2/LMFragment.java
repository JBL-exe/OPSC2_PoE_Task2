package com.example.opsc2_poe_task2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.net.Uri;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import timber.log.Timber;

public class LMFragment extends Fragment {
    FirebaseAuth fAuth;
    FirebaseFirestore fs;
    TextView L1, L2, L3, L4;
    TextView A1, A2, A3, A4;
    Button fav,local;
    private String docid;

    public LMFragment() {
    }
    boolean clicked = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_lm, container, false);
        L1 = view.findViewById(R.id.location);
        L2 = view.findViewById(R.id.location1);
        L3 = view.findViewById(R.id.location2);
        L4 = view.findViewById(R.id.location3);
        A1 = view.findViewById(R.id.address);
        A2 = view.findViewById(R.id.address1);
        A3 = view.findViewById(R.id.address2);
        A4 = view.findViewById(R.id.address3);
        fav = view.findViewById(R.id.fav1);
        local = view.findViewById(R.id.btnLocal);
        String lmID;
        lmID = fAuth.getCurrentUser().getUid();
        //DocumentReference newReference = fs.collection("users").document(lmID).collection("Favorites").document();
        fs.collection("Landmark").document(lmID).collection("Favorites")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Timber.d(document.getId() + " => " + document.getData());
                            String l = document.getId();
                            String a = document.getData().toString();
                            L1.setText(l);
                            A1.setText(a);
                            idtrans(l);
                        }
                    } else {
                        Timber.d(task.getException(), "Error getting documents: ");
                    }
                });
        fav.setOnClickListener(view1 -> {
            fs.collection("Landmark").document(lmID).collection("Favorites").document(docid)
                    .delete();
            if (view1.getId() == R.id.fav1)
            {
                clicked = !clicked;
                view1.setBackgroundResource(clicked ? R.drawable.ic_fav : R.drawable.ic_unfav);
            }

        });

        local.setOnClickListener(view12 -> {
            Intent localFavs = new Intent(this.getContext(),local_favs.class);
            startActivity(localFavs);
        });


        return view;
    }

    private void idtrans(String l)
    {
        docid = l;
    }


}
