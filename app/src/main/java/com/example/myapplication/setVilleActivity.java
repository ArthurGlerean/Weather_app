package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class setVilleActivity extends AppCompatActivity {

    Button add;
    TextInputEditText ville_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ville);
        ville_input=findViewById(R.id.ville_input);
        add=findViewById(R.id.button_add_ville);

        add.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
               //on récupère la ville saisie.
               String ville =ville_input.getText().toString();
               ville = ville.substring(0,1).toUpperCase()+ville.substring(1).toLowerCase();
               Intent intentBack = new Intent();
               intentBack.putExtra("new_ville", ville);
               setResult(Activity.RESULT_OK, intentBack);
               finish();
           }
       }
       );
    }
}
