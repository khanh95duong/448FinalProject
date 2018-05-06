package com.csci448.kduong.finalproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by darks on 3/16/2018.
 */

public class MapsFragment extends SupportMapFragment {
    private GoogleApiClient mClient;
    private GoogleMap mMap;
    private static final String[] LOCATION_PERMISSIONS = new String[] {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;

    private double mLong;
    private double mLat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClient = new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {

            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        }).build();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (hasLocationPermission()) {
                            // Zoom map to latest marker
                            LatLng myPoint = new LatLng(mLong, mLat);
                            LatLngBounds bounds = new LatLngBounds.Builder()
                                    .include(myPoint)
                                    .build();
                            int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
                            CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
                            mMap.animateCamera(update);
                        } else {
                            requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if(hasLocationPermission()) {
                    getLocation();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean hasLocationPermission() {
        int result = ContextCompat
                .checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void getLocation() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        try {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mClient, request, new LocationListener() {
                        @Override
                        public void onLocationChanged(android.location.Location location) {
                            mLong = location.getLongitude();
                            mLat = location.getLatitude();
                        }
                    });
        } catch (SecurityException e) {
            Log.e("ERROR", "Security Exception here");
        }
    }

}