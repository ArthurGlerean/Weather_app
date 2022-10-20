package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Map;

public class MeteoDetails extends AppCompatActivity {

    TextView text_ville;
    ImageView img_meteo;
    TextView text_description;
    TextView valeur_temperature;
    TextView valeur_humidite;
    TextView valeur_vent;
    TextView valeur_ressenti;

    FloatingActionButton bouton_retour;
    FloatingActionButton bouton_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo_details);
        Intent i = this.getIntent();
        Meteo meteo_city_ref = (Meteo) i.getSerializableExtra("meteo_city_ref");

        text_ville = findViewById(R.id.text_ville);
        text_ville.setText(meteo_city_ref.ville);

        //DESCRIPTION
        text_description = findViewById(R.id.text_description);
        text_description.setText(meteo_city_ref.description);

        //GESTION IMAGE:
        img_meteo = findViewById(R.id.meteo);
        String img_src = meteo_city_ref.description.replace(" ","_");
        img_meteo.setImageResource(getResources().getIdentifier(img_src, "drawable", getPackageName()));
        if(img_meteo.getDrawable() == null){
            if(meteo_city_ref.description.contains("rain") && meteo_city_ref.description != "shower_rain"){
                img_meteo.setImageResource(getResources().getIdentifier("rain", "drawable", getPackageName()));
            }
            if(meteo_city_ref.description.contains("clouds") && meteo_city_ref.description != "overcast_clouds"){
                img_meteo.setImageResource(getResources().getIdentifier("few_clouds", "drawable", getPackageName()));
            }
            if(meteo_city_ref.description.contains("snow")){
                img_meteo.setImageResource(getResources().getIdentifier("snow", "drawable", getPackageName()));
            }
            if(meteo_city_ref.description.contains("thunderstorm")){
                img_meteo.setImageResource(getResources().getIdentifier("snow", "drawable", getPackageName()));
            }

        }

        //TEMPERATURE
        valeur_temperature = findViewById(R.id.valeur_temperature);
        valeur_temperature.setText(meteo_city_ref.temperature + "°C");

        //RESSENTI
        valeur_ressenti = findViewById(R.id.valeur_ressenti);
        valeur_ressenti.setText(meteo_city_ref.ressenti + "°C");

        //HUMIDITE
        valeur_humidite = findViewById(R.id.valeur_humidite);
        valeur_humidite.setText(meteo_city_ref.humidite + "%");

        //VENT
        valeur_vent = findViewById(R.id.valeur_vent);
        valeur_vent.setText(meteo_city_ref.vent + "km/h (" + meteo_city_ref.dir_vent +")");

        bouton_retour = findViewById(R.id.bouton_retour);
        bouton_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bouton_map = findViewById(R.id.button_show_map);
        bouton_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_map = new Intent(MeteoDetails.this,MapsActivity.class);
                i_map.putExtra("nom_ville",meteo_city_ref.ville);
                i_map.putExtra("lat_ville",meteo_city_ref.lat_ville);
                i_map.putExtra("long_ville",meteo_city_ref.long_ville);
                startActivity(i_map);
            }
        });


    }
}