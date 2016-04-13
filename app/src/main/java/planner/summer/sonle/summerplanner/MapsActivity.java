package planner.summer.sonle.summerplanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private SupportMapFragment fragment;
    private FloatingSearchView mSearchView;

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
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                // Handle item selection
                if (item.getItemId() == R.id.action_current_location) {
                    // get current location
                    getCurrentLocation();
                }
            }
        });
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction() {
                Toast.makeText(MapsActivity.this, mSearchView.getQuery(), Toast.LENGTH_LONG);
            }
        });
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                Toast.makeText(MapsActivity.this, mSearchView.getQuery(), Toast.LENGTH_LONG);
            }
        });
    }

    public void getCurrentLocation() {
        //TO get the location,manifest file is added with 2 permissions
        //Location Manager is used to figure out which location provider needs to be used.
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Best location provider is decided by the criteria
        Criteria criteria = new Criteria();
        //location manager will take the best location from the criteria
        locationManager.getBestProvider(criteria, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0)

            {
                String cityName = addresses.get(0).getLocality().toString();
                mSearchView.setSearchText(cityName);
            }

        } catch (IOException e) {
            e.printStackTrace();

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
                    Log.i("Location", point.latitude + "," + point.longitude);
                    String queryURL = "http://api.worldweatheronline.com/premium/v1/search.ashx?query=" + point.latitude + "," + point.longitude + "&num_of_results=3&format=json&key=7a7108cfb2e94a9e95e203419161004";
                    new FetchingWeatherTask(MapsActivity.this, new FetchingWeatherTask.AsyncResponse() {
                        @Override
                        public void processFinish(String result) {
                            Gson gson = new Gson();
                            ArrayList<String> areaList = new ArrayList<>();
                            try {
                                JSONObject data = new JSONObject(result);
                                JSONArray locations = data.getJSONObject("search_api").getJSONArray("result");
                                for (int i = 0; i < locations.length(); i++) {

                                    JSONObject location = locations.getJSONObject(i);
                                    JSONArray area = location.getJSONArray("areaName");
                                    String name = ((JSONObject) area.get(0)).getString("value");
                                    areaList.add(name);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // init dialog box for area list

                            Bundle bundle = new Bundle();
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
