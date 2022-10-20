package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myapplication.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private LocationManager lm;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    try {
                        String data = "";
                        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=genay&appid=b0e30e3d09d573bc8da44e7fdefb102b&units=metric");


                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = rd.readLine()) != null) {
                            data = data + line;
                        }
                        if(!data.isEmpty()){
                            JSONObject jsonObject = new JSONObject(data);

                            JSONObject coordonnees = jsonObject.getJSONObject("coord");
                            Double lat_ville = coordonnees.getDouble("lat");
                            Double long_ville = coordonnees.getDouble("lon");


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LatLng ville = new LatLng(lat_ville, long_ville);
                                    mMap.addMarker(new MarkerOptions().position(ville).title("Marker in city"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(ville));
                                }
                            });

                        }
                        rd.close();
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
        // Add a marker in Sydney and move the camera





    }

    @Override
    protected void onResume() {
        super.onResume();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000L, (float) 0, (LocationListener) this);
        }
        if( lm.isProviderEnabled( LocationManager.PASSIVE_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000L, (float) 0, (LocationListener) this);
        }
        if( lm.isProviderEnabled( LocationManager.NETWORK_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000L,0, (LocationListener) this);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                System.out.println("tu as clique sur le marker.");
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(lm != null){
            lm.removeUpdates((LocationListener) this);
        }
    }

    public void onProviderEnabled(String provider){

    }
    public void onProviderDisabled(String provider){

    }
    public void onStatusChanged(String provider,int status,Bundle extras){

    }
    public void onLocationChanged(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
    }
}