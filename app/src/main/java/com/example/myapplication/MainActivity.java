package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton b_add_meteo,b_see_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_see_map = findViewById(R.id.button_show_map);
        b_add_meteo = findViewById(R.id.button_add_ville);
        ArrayList<Meteo> arrayOfMeteos = new ArrayList<Meteo>();

        ActivityResultLauncher<Intent> mainActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result ->  {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null){
                            String ville = data.getStringExtra("new_ville");

                            Meteo m = new Meteo(ville);
                            arrayOfMeteos.add(m);
                            MeteoAdapter adapter = new MeteoAdapter(this, arrayOfMeteos);

                            ListView listView = findViewById(R.id.villesList);
                            listView.setAdapter(adapter);

                        }
                    }
                }
        );
        b_add_meteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent_back = new Intent(MainActivity.this, setVilleActivity.class);
                mainActivityResultLauncher.launch(intent_back); //launch démarre l'intent

            }
        } );

        ActivityResultLauncher<Intent> returnOfMap = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result ->  {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null){
                            //traitement des donnees...
                        }
                    }
                }
        );
        b_see_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_view_map = new Intent(MainActivity.this, MapsActivity.class);
                returnOfMap.launch(intent_view_map); //launch démarre l'intent
            }
        } );
    }
}