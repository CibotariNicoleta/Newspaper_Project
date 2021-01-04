package com.example.newsmanagerproject.model;

import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsmanagerproject.R;
import com.google.android.gms.location.FusedLocationProviderClient;

public class MapLocation extends AppCompatActivity {
    Location location;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
    }
}