package planner.summer.sonle.summerplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by sonle on 4/10/16.
 */
public class WeatherFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.area_list_layout, container, false);
        Bundle args = getArguments();
        if(args != null) {
            final ArrayList<String> areaList = args.getStringArrayList("areaList");
            ListView lv = (ListView)v.findViewById(R.id.lv_area);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
                    AreaWeatherDetailsFragment area_fg = new AreaWeatherDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("area", areaList.get(position));
                    area_fg.setArguments(bundle);
                    ft.replace(R.id.embedded, area_fg);// get child fragment (WeatherFragment)
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            String[] areaArr = new String[areaList.size()];
            areaArr = areaList.toArray(areaArr);
            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,areaArr);
            lv.setAdapter(adapter);
        }
        return v;
    }

}
