package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton b_add_meteo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_add_meteo = findViewById(R.id.button_add_ville);
        ArrayList<Meteo> arrayOfMeteos = new ArrayList<>();

        /**
        ListView lv = findViewById(R.id.villesList);
        TextView emptyText = findViewById(R.id.empty);
        lv.setEmptyView(emptyText);
        **/

        ActivityResultLauncher<Intent> mainActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result ->  {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null){
                            Meteo m = (Meteo) data.getSerializableExtra("new_meteo_city");
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
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",arrayOfMeteos);
                intent_back.putExtra("BUNDLE",args);
                mainActivityResultLauncher.launch(intent_back); //launch démarre l'intent
            }
        } );

    }

}