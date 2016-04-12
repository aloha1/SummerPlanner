package planner.summer.sonle.summerplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sonle on 4/11/16.
 */
public class DayForeCastAdapter extends ArrayAdapter<DayForeCast> {
    public DayForeCastAdapter(Context context,  ArrayList<DayForeCast> days) {
        super(context, R.layout.day_forecast_layout, days);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.day_forecast_layout, parent, false);
            holder = new ViewHolder();
            holder.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
            holder.tv_min_temp = (TextView) convertView.findViewById(R.id.tv_min_temp);
            holder.tv_max_temp = (TextView) convertView.findViewById(R.id.tv_max_temp);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DayForeCast item = getItem(position);
        holder.tv_day.setText(item.getDay());
        holder.tv_min_temp.setText("Min(F): "+item.getMinTemp());
        holder.tv_max_temp.setText("Max(F): "+item.getMaxTemp());

        return convertView;
    }
    static class ViewHolder {
        TextView tv_day;
        TextView tv_min_temp;
        TextView tv_max_temp;
    }

}
