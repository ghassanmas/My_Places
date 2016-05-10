package com.example.jbt.placess_3;

import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.jbt.placess_3.Tasks.SearchByMyPlace;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements   GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    MapView mapView;
    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    public  static Location mLastLocation;
    Dialog showInfo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_map, container, false);

        mapView= (MapView) v.findViewById(R.id.mapview);

        mapView.onCreate(savedInstanceState);
            googleMap=mapView.getMap();
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setMyLocationEnabled(true);
        setGoogleApi();

        MapsInitializer.initialize(getActivity());
        setGoogleApi();








        return v;
    }


    private void setGoogleApi() {
        if (googleApiClient == null) {
            googleApiClient =  new GoogleApiClient.Builder(this.getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        googleApiClient.connect();


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnected(Bundle bundle) {

        showInfo=new Dialog(getActivity());
        showInfo.setContentView(R.layout.dialog_latlang);
        showInfo.setTitle("Lat, Lng");

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        TextView lat= (TextView) showInfo.findViewById(R.id.lat);
        lat.setText("Latitude = " + mLastLocation.getLatitude());
        TextView lng= (TextView) showInfo.findViewById(R.id.lng);
        lng.setText("longiti = :  " + mLastLocation.getLongitude());

        Button ok = (Button) showInfo.findViewById(R.id.ok);
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo.dismiss();

            }
        });
        showInfo.show();

        mapView.getMapAsync(new OnMapReadyCallback() {


            @Override
            public void onMapReady(GoogleMap googleMap) {

                if(MainActivity.someOne){

                    LatLng coordinates = new LatLng(MainActivity.location_lat, MainActivity.location_lang);
                     googleMap.addMarker(new MarkerOptions().position(coordinates).title(MainActivity.location_name));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));

                    MainActivity.someOne=false;
                    }else{
                    LatLng coordinates = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    // googleMap.addMarker(new MarkerOptions().position(coordinates).title("ana Hoon"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));

                }


                mapView.onResume();
            }
        });

        googleApiClient.disconnect();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public  void searchLoc(){

        StringBuilder stringBuilder=new StringBuilder();
               if(mLastLocation!=null) {
                   stringBuilder.append("https://maps.googleapis.com/maps/api/place/search/json?location=");
                   stringBuilder.append(mLastLocation.getLatitude());
                   stringBuilder.append(",");
                   stringBuilder.append(mLastLocation.getLongitude());
                   stringBuilder.append("&radius=10000&sensor=true&key=AIzaSyAbKXsZwjraDcZBWwqGdrghSkF9YtVqDlE");

                   SearchByMyPlace myPlace = new SearchByMyPlace(getActivity());

                   myPlace.execute(stringBuilder.toString());
               }
        else{
                   Toast.makeText(getActivity(),"Location Seravices Not Avaiable !",Toast.LENGTH_SHORT).show();
               }




    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public  void landSet(){
        googleMap.clear();
        LatLng coordinates = new LatLng(MainActivity.location_lat, MainActivity.location_lang);
        googleMap.addMarker(new MarkerOptions().position(coordinates).title(MainActivity.location_name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
    }
}
