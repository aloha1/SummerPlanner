package planner.summer.sonle.summerplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sonle on 4/10/16.
 */
public class AreaWeatherDetailsFragment extends Fragment {
    static final String[] numbers = new String[] {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.area_weather_details_layout, container, false);
        // get weather forecast for specific area
        Bundle args = getArguments();
        String area = args.getString("area");
        // perform retrieving forecast for this area
        String queryURL = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=7a7108cfb2e94a9e95e203419161004&q="+area+"&num_of_days=2&tp=3&format=json";
        new FetchingWeatherTask(getActivity(), new FetchingWeatherTask.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.i("Area forecast",output);
                Gson gson = new Gson();
                ArrayList<String> areaList = new ArrayList<String>();
                try {
                    JSONObject data = new JSONObject(output);
                    JSONArray current = data.getJSONObject("data").getJSONArray("current_condition");

                    JSONObject current_condition = current.getJSONObject(0);
                    String current_time  = current_condition.getString("observation_time");
                    String current_temp  = current_condition.getString("temp_F");
                    String iconURL = current_condition.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value");
                    TextView tv_current_time = (TextView)v.findViewById(R.id.tv_current_time);
                    tv_current_time.setText(current_time);
                    TextView tv_current_temp = (TextView)v.findViewById(R.id.tv_current_temp);
                    tv_current_temp.setText("Temp(F):"+current_temp);
                    ImageView imageView = (ImageView)v.findViewById(R.id.icon);
                    Picasso.with(getActivity()).load(iconURL).into(imageView);
                    // load day forecast to gridview
                    JSONArray weather = data.getJSONObject("data").getJSONArray("weather");
                    ArrayList<DayForeCast> dayArr = new ArrayList<DayForeCast>();
                    for (int i = 0; i < weather.length(); i++) {

                        JSONObject dayForecast = weather.getJSONObject(i);
                        String day = dayForecast.getString("date");
                        String mintempF = dayForecast.getString("mintempF");
                        String maxtempF = dayForecast.getString("maxtempF");
                        DayForeCast d = new DayForeCast(day,mintempF,maxtempF);
                        dayArr.add(d);
                    }
                    Log.i("asdfasdfasdf",dayArr.get(0).getDay());
                    GridView gridView = (GridView)v.findViewById(R.id.gv_area_weather);

                    DayForeCastAdapter adapter = new DayForeCastAdapter(getActivity(),dayArr);

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, numbers);

                    gridView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(queryURL);



        Button btn_back = (Button) v.findViewById(R.id.btn_Back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        });
        return v;
    }

}
