package com.example.myapplication;

import java.io.Serializable;

public class Meteo implements Serializable {
    public String ville;
    public String description;
    public Double temperature;
    public Double humidite;
    public Double ressenti;
    public Double vent;
    public String dir_vent;
    public Meteo(String ville,String description,Double temperature,Double humidite, Double ressenti, Double vent, String dir_vent) {
        this.ville = ville;
        this.description = description;
        this.temperature = temperature;
        this.humidite = humidite;
        this.ressenti = ressenti;
        this.vent = vent;
        this.dir_vent = dir_vent;
    }
}
