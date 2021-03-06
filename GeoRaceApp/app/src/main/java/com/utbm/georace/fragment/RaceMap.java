package com.utbm.georace.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.utbm.georace.R;

public class RaceMap extends Fragment {

    Context myContext;
    MapView mapView;
    GoogleMap map;

    public RaceMap() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_race_map, container, false);
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        MapsInitializer.initialize(this.getActivity());
        return v;
    }

    public void setCheckpointsCompteur(Integer nbCheckpoints, Integer nbChecked){
        TextView tv = (TextView) getView().findViewById(R.id.nbCheckpointsValue);
        tv.setText(nbChecked+"/"+nbCheckpoints);
    }
    public void setMarker(LatLng position, String tooltip){
        map.addMarker(new MarkerOptions()
                .position(position)
                .title(tooltip));
    }
    public void setCameraTo(LatLng position){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 18);
        map.animateCamera(cameraUpdate);

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);

        public void setUserPosition();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
