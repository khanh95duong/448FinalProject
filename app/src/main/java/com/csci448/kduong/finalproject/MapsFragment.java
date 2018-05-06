package com.csci448.kduong.finalproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darks on 3/16/2018.
 */

public class MapsFragment extends SupportMapFragment {
    private GoogleApiClient mClient;
    private GoogleMap mMap;
    private static final String[] LOCATION_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;

    private Location mCurrentLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClient = new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        if (hasLocationPermission()) {
                            getLocation();
                        }
                        else {
                            requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);
                        }
                    }
                });
            }


            @Override
            public void onConnectionSuspended(int i) {

            }
        }).build();
    }

    public void update() {
        if (mMap == null) {
            return;
        }

        LatLng myPoint = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        LatLng topright = new LatLng(mCurrentLocation.getLatitude() + 1.0, mCurrentLocation.getLongitude() + 1.0);
        LatLng topleft = new LatLng(mCurrentLocation.getLatitude() + 1.0, mCurrentLocation.getLongitude() - 1.0);
        LatLng botright = new LatLng(mCurrentLocation.getLatitude() - 1.0, mCurrentLocation.getLongitude() + 1.0);
        LatLng botleft = new LatLng(mCurrentLocation.getLatitude() - 1.0, mCurrentLocation.getLongitude() - 1.0);

        MarkerOptions myMarker = new MarkerOptions()
                .position(myPoint)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("You are here");

        mMap.clear();
        mMap.addMarker(myMarker);

        Geocoder geocoder = new Geocoder(getActivity());
        for (Event ev : EventLab.getInstance().getEvents()) {
            try {
                List<Address> addresses = geocoder.getFromLocationName(ev.getAddress(), 1);
                if (addresses.size() > 0) {
                    LatLng newll = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(newll).title(ev.getTitle()));
                }
            }
            catch (IOException e) {

            }
        }

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(myPoint)
                .include(topright)
                .include(topleft)
                .include(botright)
                .include(botleft)
                .build();

        int margin = 10;
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mMap.animateCamera(update);
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
                            mCurrentLocation = location;
                            update();
                        }
                    });
        } catch (SecurityException e) {
            Log.e("ERROR", "Security Exception here");
        }
    }

}