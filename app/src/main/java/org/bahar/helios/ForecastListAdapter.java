package org.bahar.helios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.bahar.helios.entity.ForecastListItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Bahar on 12/19/2017.
 */

public class ForecastListAdapter extends BaseAdapter {

    private ForecastListItem mDataset;

    public ForecastListAdapter(ForecastListItem dataset) {
        mDataset = dataset;
    }

    @Override
    public int getCount() {
        return mDataset.getList().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        String mWeather =mDataset.getList().get(i).getWeather().get(0).getDescription();
        String iconUrl = "http://openweathermap.org/img/w/" + mDataset.getList().get(i).getWeather().get(0).getIcon() + ".png";
        Long mTime= mDataset.getList().get(i).getDt().longValue();
        Integer maxTemp=mDataset.getList().get(i).getTemp().getMax().intValue();
        Integer minTemp=mDataset.getList().get(i).getTemp().getMin().intValue();

        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_forecast_list, null, false);

        TextView tvweather = row.findViewById(R.id.rowForecastList_textView_label);
        TextView tvmin=row.findViewById(R.id.rowForecastList_textView_temp_min);
        TextView tvmax=row.findViewById(R.id.rowForecastList_textView_temp_max);
        TextView tvdate=row.findViewById(R.id.rowForecastList_textView_dayofweek);
        SimpleDraweeView icon = row.findViewById(R.id.rowForecastList_simpleDraweeView_icon);

        tvweather.setText(mWeather);
        icon.setImageURI(iconUrl);
        tvmin.setText(minTemp.toString());
        tvmax.setText(maxTemp.toString());

        long unixSeconds = mTime;
                Date date = new Date(unixSeconds*1000L);
        SimpleDateFormat daytime = new SimpleDateFormat("\"EEE, MMM d\"");
        daytime.setTimeZone(TimeZone.getTimeZone("GMT+3.5"));
        String formattedDate = daytime.format(date);
        daytime.setTimeZone(TimeZone.getTimeZone("GMT+3.5"));


        tvdate.setText(formattedDate);

        return row;
    }
}
