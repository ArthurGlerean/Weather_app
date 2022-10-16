package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MeteoDetails extends AppCompatActivity {

    TextView ville_ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo_details);
        Intent i = this.getIntent();
        String ville = i.getStringExtra("ville_ref");
        ville_ref = findViewById(R.id.ville_ref);
        ville_ref.setText(ville);

    }
}