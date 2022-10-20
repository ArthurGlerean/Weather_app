package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MeteoAdapter extends ArrayAdapter<Meteo> {
    private Context mcon;
    public MeteoAdapter(Context context, ArrayList<Meteo> meteos) {
        super(context, 0, meteos);
    }


    @Override
    public View getView(int x, View convertView, ViewGroup parent) {
        // Récupérer le contact à la position x.
        Meteo m = getItem(x);

        //Vérifiez si une vue existante est réutilisée, sinon faire monter la vue
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.meteolist, parent, false);
        }

        TextView text_ville = convertView.findViewById(R.id.ville);
        text_ville.setText(m.ville);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_details = new Intent(getContext(),MeteoDetails.class);
                intent_details.putExtra("meteo_city_ref",m);
                getContext().startActivity(intent_details);
                Toast.makeText(getContext(),"Clique sur element de la liste", Toast.LENGTH_SHORT).show();
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                remove(m);
                return false;
            }
        });

        // Retour de la vue du contact:
        return convertView;
    }
}
