package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

    TextView ville_ref;
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
                        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+ville.toLowerCase(Locale.ROOT)+"&appid=b0e30e3d09d573bc8da44e7fdefb102b");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = rd.readLine()) != null) {
                            data = data + line;
                        }
                        if(!data.isEmpty()){
                             JSONObject jsonObject = new JSONObject(data);
                             JSONObject temp_f = jsonObject.getJSONObject("main");
                             Double temp_ville = Double.valueOf(Math.round(temp_f.getDouble("temp") / 33.8));
                             System.out.println(temp_ville);
                             ville_ref = findViewById(R.id.ville_ref);
                             ville_ref.setText(temp_ville.toString());
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



    }
}