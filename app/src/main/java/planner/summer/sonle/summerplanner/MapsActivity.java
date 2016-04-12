package planner.summer.sonle.summerplanner;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private SupportMapFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FragmentManager fm = getSupportFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mMap == null) {
            mMap = fragment.getMap();
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                public void onMapClick(LatLng point) {
                    Log.i("Location",point.latitude+","+point.longitude);
                    String queryURL = "http://api.worldweatheronline.com/premium/v1/search.ashx?query="+point.latitude+","+point.longitude+"&num_of_results=3&format=json&key=7a7108cfb2e94a9e95e203419161004";
                    new FetchingWeatherTask(MapsActivity.this, new FetchingWeatherTask.AsyncResponse() {
                        @Override
                        public void processFinish(String result) {
                            Gson gson = new Gson();
                            ArrayList<String> areaList = new ArrayList<String>();
                            try {
                                JSONObject data = new JSONObject(result);
                                JSONArray locations = data.getJSONObject("search_api").getJSONArray("result");
                                for (int i = 0; i < locations.length(); i++) {

                                    JSONObject location = locations.getJSONObject(i);
                                    JSONArray area = location.getJSONArray("areaName");
                                    String name = ((JSONObject)area.get(0)).getString("value");
                                    areaList.add(name);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // init dialog box for area list

                            Bundle bundle= new Bundle();
                            bundle.putString("title", "Pick an area");
                            bundle.putStringArrayList("areaList", areaList);
                            // Create the fragment and show it as a dialog
                            WeatherDialogFragment newFragment = new WeatherDialogFragment().newInstance();
                            newFragment.setArguments(bundle);
                            newFragment.show(getSupportFragmentManager(), "areaListFragment");
                        }
                    }).execute(queryURL);

                }
            });
        }
    }
}
