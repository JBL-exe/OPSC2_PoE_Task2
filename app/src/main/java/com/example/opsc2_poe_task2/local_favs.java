package com.example.opsc2_poe_task2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class local_favs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_favs);

        Button bsLink =  findViewById(R.id.btnLinkBS);
        Button bwLink =  findViewById(R.id.btnLinkBW);
        Button R67Link = findViewById(R.id.btnLinkR67);
        Button drLink =  findViewById(R.id.btnLinkDR);
        Button pers = findViewById(R.id.btnPerFavs);

        bsLink.setOnClickListener(view -> goToUrl ( "https://www.facebook.com/bridgestreetbrewerype/?ref=py_c"));
        bwLink.setOnClickListener(view -> goToUrl ( "https://www.facebook.com/Boardwalk-Hotel-Port-Elizabeth-343083829129165/"));
        R67Link.setOnClickListener(view -> goToUrl ( "https://www.nmbt.co.za/listing/route_67.html"));
        drLink.setOnClickListener(view -> goToUrl ( "https://www.nmbt.co.za/listing/donkin_reserve_pyramid_and_lighthouse.html"));

        pers.setOnClickListener(view -> {
            Intent PersonalFavs = new Intent(getApplicationContext(),MainScreen.class);
            startActivity(PersonalFavs);
        });


    }

    private void goToUrl(String URL)
    {
        Uri uriUrl = Uri.parse(URL);
        Intent linkClicked = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(linkClicked);
    }

}
