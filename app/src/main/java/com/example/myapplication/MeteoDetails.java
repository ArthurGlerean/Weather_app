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
    ImageView meteo;
    TextView text_description;
    TextView valeur_temperature;
    TextView valeur_humidite;
    TextView valeur_vent;
    TextView valeur_ressenti;

    FloatingActionButton bouton_retour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo_details);
        Intent i = this.getIntent();
        String ville = i.getStringExtra("ville_ref");

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    try {
                        String data = "";
                        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+ville.toLowerCase(Locale.ROOT)+"&appid=b0e30e3d09d573bc8da44e7fdefb102b&units=metric");


                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = rd.readLine()) != null) {
                            data = data + line;
                        }
                        if(!data.isEmpty()){
                             JSONObject jsonObject = new JSONObject(data);
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

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //VILLE
                                    text_ville = findViewById(R.id.text_ville);
                                    text_ville.setText(ville);

                                    //DESCRIPTION
                                    text_description = findViewById(R.id.text_description);
                                    text_description.setText(description);

                                    //GESTION IMAGE:
                                    meteo = findViewById(R.id.meteo);
                                    String img_src = description.replace(" ","_");
                                    System.out.println(img_src);
                                    meteo.setImageResource(getResources().getIdentifier(img_src, "drawable", getPackageName()));

                                    //TEMPERATURE
                                    valeur_temperature = findViewById(R.id.valeur_temperature);
                                    valeur_temperature.setText(temp_ville.toString() + "°C");

                                    //RESSENTI
                                    valeur_ressenti = findViewById(R.id.valeur_ressenti);
                                    valeur_ressenti.setText(temp_ressenti.toString() + "°C");

                                    //HUMIDITE
                                    valeur_humidite = findViewById(R.id.valeur_humidite);
                                    valeur_humidite.setText(humidite.toString()+"%");

                                    //VENT
                                    valeur_vent = findViewById(R.id.valeur_vent);
                                    valeur_vent.setText(vent.toString()+"km/h");
                                }
                            });

                        }

                        rd.close();

                        //Map<String, Object> respMap = jsonToMap(result.toString());

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        bouton_retour = findViewById(R.id.bouton_retour);
        bouton_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(MeteoDetails.this,MainActivity.class);
                startActivity(back);
            }
        });



    }
}