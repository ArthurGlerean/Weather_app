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
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
               //on récupère la ville saisie et les villes déjà saisies auparavant
               String ville =ville_input.getText().toString();
               Intent array = getIntent();
               Bundle args = array.getBundleExtra("BUNDLE");
               ArrayList<Meteo> arrayofMeteos = (ArrayList<Meteo>) args.getSerializable("ARRAYLIST");

               //si la ville est saisie->
               if(!ville.isEmpty()) {
                   String ville_maj = ville.substring(0, 1).toUpperCase() + ville.toString().substring(1).toLowerCase();
                   //on vérifie que la ville saisie n'est pas déjà dans la liste.
                   boolean b =false;
                   for(int i=0;i<arrayofMeteos.toArray().length;i++){
                       if(arrayofMeteos.get(i).ville.equals(ville_maj)){
                           b=true;
                       }
                   }

                   //si elle n'y est pas déjà, on l'ajoute !
                   if(b == false) {
                       String finalVille = ville_maj;
                       Thread thread = new Thread(new Runnable() {
                           @Override
                           public void run() {
                               try {
                                   try {
                                       String data = "";
                                       URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + finalVille.toLowerCase(Locale.ROOT) + "&appid=b0e30e3d09d573bc8da44e7fdefb102b&units=metric");
                                       HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                       InputStream inputStream = conn.getInputStream();
                                       BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                                       String line;
                                       while ((line = rd.readLine()) != null) {
                                           data = data + line;
                                       }
                                       if (!data.isEmpty()) {
                                           JSONObject jsonObject = new JSONObject(data);
                                           //COORDONNEES
                                           JSONObject coordonnees = jsonObject.getJSONObject("coord");
                                           Double lat_ville = coordonnees.getDouble("lat");
                                           Double long_ville = coordonnees.getDouble("lon");
                                           //DESCRIPTION
                                           String weather = jsonObject.getString("weather");
                                           JSONArray weather_array = new JSONArray(weather);
                                           JSONObject desc = weather_array.getJSONObject(0);
                                           String description = desc.getString("description");

                                           //TEMPERATURE, RESSENTI, HUMIDITE
                                           JSONObject temps = jsonObject.getJSONObject("main");
                                           Double temp_ville = temps.getDouble("temp");
                                           Double temp_ressenti = temps.getDouble("feels_like");
                                           Double humidite = temps.getDouble("humidity");

                                           //VENT
                                           JSONObject wind = jsonObject.getJSONObject("wind");
                                           Double vent = wind.getDouble("speed");
                                           Double deg_vent = wind.getDouble("deg");
                                           String tab_vents[][] = {
                                                   {"22.5", "N"},
                                                   {"67.5", "NE"},
                                                   {"112.5", "E"},
                                                   {"157.5", "SE"},
                                                   {"202.5", "S"},
                                                   {"247.5", "SO"},
                                                   {"292.5", "O"},
                                                   {"337.5", "NO"},
                                           };
                                           String dir_vent = "";
                                           int i = 0;
                                           boolean v = false;
                                           while (i < tab_vents.length && v == false) {
                                               if (Double.parseDouble(tab_vents[i][0]) < deg_vent) {
                                                   i++;
                                               } else {
                                                   dir_vent = tab_vents[i][1];     // on récupère le sens du vent associé au degrés.
                                                   v = true;
                                               }
                                           }
                                           String finalDir_vent = dir_vent;
                                           runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   Meteo m = new Meteo(finalVille, lat_ville, long_ville, description, temp_ville, humidite, temp_ressenti, vent, finalDir_vent);
                                                   Intent intentBack = new Intent();
                                                   intentBack.putExtra("new_meteo_city", m);
                                                   setResult(Activity.RESULT_OK, intentBack);
                                                   finish();
                                               }
                                           });
                                       } else {
                                           runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   Toast.makeText(getApplicationContext(), "Désolé, nous n'avons aucune information sur cette ville :(", Toast.LENGTH_SHORT).show();
                                               }
                                           });
                                       }
                                       rd.close();
                                   } catch (MalformedURLException e) {
                                       e.printStackTrace();
                                   } catch (IOException e) {
                                       runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                               Toast.makeText(getApplicationContext(), "Nous ne connaissons pas cette ville :/", Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   }
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }
                           }
                       });

                       thread.start();
                   }
                   //sinon, on affiche une erreur.
                   else{
                       Toast.makeText(getApplicationContext(), "Tu as déjà saisie cette ville !", Toast.LENGTH_SHORT).show();
                   }

               }
               else{
                   Toast.makeText(getApplicationContext(), "Veuillez saisir la ville !", Toast.LENGTH_SHORT).show();
               }
           }
       }
       );
    }
}
