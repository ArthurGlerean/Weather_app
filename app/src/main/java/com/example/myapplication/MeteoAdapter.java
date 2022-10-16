package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MeteoAdapter extends ArrayAdapter<Meteo> {
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
