package org.bahar.helios;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.bahar.helios.entity.ForecastListItem;
import org.bahar.helios.weatherentity.WeatherResponse;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherActivity extends AppCompatActivity {
        private WeatherResponse mWeatherResponse;
        private ForecastListItem mforecastResponse;
        private SimpleDraweeView mWeatherIcon;
        private TextView mcityname, mDate, mTemp, mWeather, mMaxTemp, mMinTemp;
        private TextView mMormingTemp, mDayTemp, mEveningTemp, mNightTemp;
        private TextView mHumidity, mPressure, mDirection, mSpeed;
        private TextView mSunrise,mSunset,mClouds;
        private LineChart mLineChart;
        private boolean doubleTap=false;
        private Button mMenuForecast,mMenuSetting;
        private String mSharePref_cityid;



    public static void start(Context context) {
        Intent intent = new Intent(context, WeatherActivity.class);
        context.startActivity(intent);
    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setLocale();
            setContentView(R.layout.activity_weather);
            getSupportActionBar().setTitle("Helios, Weather App");
            findViews();
            init_sharepreference();
            loadData_currentweather();
            loadData_Forecast();
            mLineChart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WeatherActivity.this, ForecastActivity.class);
                    startActivity(intent);
                }

            });

            mMenuSetting.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WeatherActivity.this, ChangeCityActivity.class);
                    startActivity(intent);
                }

            });

            mMenuForecast.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WeatherActivity.this, ForecastActivity.class);
                    startActivity(intent);
                }

            });
                  }

    @Override
    public void onResume()
    {
        super.onResume();
        findViews();
        init_sharepreference();
        loadData_currentweather();
        loadData_Forecast();
    }
    @Override
    public void onBackPressed(){
        if(doubleTap){
            super.onBackPressed();
        }else{
            Toast.makeText(this,"Double Click to Exit!",Toast.LENGTH_SHORT).show();
            doubleTap=true;
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleTap=false;
                }
            },500);
        }
   }
    private void setLocale(){
    Locale myLocale = new Locale("en");
    Locale.setDefault(myLocale);
    Configuration config = new Configuration();
    config.locale = myLocale;
    getResources().updateConfiguration(config, getResources().getDisplayMetrics());
}
    private void findViews() {
        mMenuForecast=findViewById(R.id.activityweather_menu_forecast);
        mMenuSetting=findViewById(R.id.activityweather_menu_setting);

        mLineChart = findViewById(R.id.activityweather_linechart_forecastchart);

            mcityname=findViewById(R.id.activityweather_textview_City);
            mWeatherIcon=findViewById(R.id.activityweather_SimpleDraweeView_weathericon);
            mDate=findViewById(R.id.activityweather_textview_date);
            mTemp=findViewById(R.id.activityweather_textview_temp);
            mWeather=findViewById(R.id.activityweather_textview_weather);
            mMaxTemp=findViewById(R.id.activityweather_TextView_maxtemp);
            mMinTemp=findViewById(R.id.activityweather_TextView_mintemp);

            mMormingTemp=findViewById(R.id.activityweather_TextView_morningtemp);
            mDayTemp=findViewById(R.id.activityweather_TextView_daytemp);
            mEveningTemp=findViewById(R.id.activityweather_TextView_eveningtemp);
            mNightTemp=findViewById(R.id.activityweather_TextView_nighttemp);

            mHumidity=findViewById(R.id.activityweather_TextView_humidity);
            mPressure=findViewById(R.id.activityweather_TextView_Pressure);
            mDirection=findViewById(R.id.activityweather_TextView_winddirection);
            mSpeed=findViewById(R.id.activityweather_TextView_windspeed);

            mSunrise=findViewById(R.id.activityweather_TextView_sunrise);
            mSunset=findViewById(R.id.activityweather_TextView_sunset);
            mClouds=findViewById(R.id.activityweather_TextView_Clouds);
        }
    private void init_sharepreference(){
            SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("APIPARAM",MODE_PRIVATE);
            mSharePref_cityid= sharedpreferences.getString("cityID", "112931");
        }
    private void loadData_currentweather() {
        // Init Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

       init_sharepreference();

            WebApi weatherApi = retrofit.create(WebApi.class);
            Call<WeatherResponse> call = weatherApi.getWeatherRes(mSharePref_cityid);
            call.enqueue(new Callback<WeatherResponse>() {

                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                    mWeatherResponse = response.body();
                    getDataFromResponse_currentweather(mWeatherResponse);
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {

                    Toast.makeText(WeatherActivity.this, "Fail Connection to Current Weather API", Toast.LENGTH_LONG).show();
                }
            });


        }
    private void loadData_Forecast() {

        // Init Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        init_sharepreference();
        WebApi forecastApi = retrofit.create(WebApi.class);
        Call<ForecastListItem> call = forecastApi.getForecastList(mSharePref_cityid);
        call.enqueue(new Callback<ForecastListItem>() {

            @Override
            public void onResponse(Call<ForecastListItem> call, Response<ForecastListItem> response) {
                mforecastResponse = response.body();
                getDataFromResponse_forecast(mforecastResponse);
                loadchart(mLineChart,mforecastResponse);
            }

            @Override
            public void onFailure(Call<ForecastListItem> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, "Fail Connection to forecast API", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadchart(LineChart mlineChart, ForecastListItem mChartResponse){
        ArrayList<Entry> entries = new ArrayList<>();
        float mDayTemp[] = new float[7];

        for(int i=0;i<7;i++){
            mDayTemp[i]=mChartResponse.getList().get(i).getTemp().getDay().intValue();
            entries.add(new Entry(mDayTemp[i], i));
        }

        LineDataSet dataset = new LineDataSet(entries, "Week Forecast Chart");//Week Forecast Chart

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();


        String mDayLable[] = new String[7];
        Long mDay;

        for(int i=0;i<7;i++){

            mDay=mChartResponse.getList().get(i).getDt().longValue();
            long unixSeconds = mDay;
            Date date = new Date(unixSeconds*1000L);
            SimpleDateFormat daytime = new SimpleDateFormat("EEE");
            daytime.setTimeZone(TimeZone.getTimeZone("GMT+3.5"));
            String formattedDate = daytime.format(date);
            daytime.setTimeZone(TimeZone.getTimeZone("GMT+3.5"));

            mDayLable[i]= formattedDate;
            labels.add(new String(mDayLable[i]));
        }


        LineData data = new LineData(labels, dataset);
        mlineChart.setData(data);
        mlineChart.setDescription("");//my chart description

        //APPEARANCE OF CHART
        data.setValueTextColor(Color.WHITE);
        XAxis xl = mlineChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        YAxis leftAxis = mlineChart.getAxisLeft();
        leftAxis.setTextColor(Color.TRANSPARENT);
        leftAxis.setDrawGridLines(false);
        YAxis rightAxis = mlineChart.getAxisRight();
        rightAxis.setEnabled(false);
        dataset.setDrawFilled(true);
        mlineChart.setDescriptionColor(Color.WHITE);
        mlineChart.setBackgroundColor(Color.TRANSPARENT);
        mlineChart.setDrawGridBackground(false);
        dataset.setValueTextColor(Color.WHITE);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        mlineChart.animateY(2000);}
    private void getDataFromResponse_currentweather(WeatherResponse mWeatherResponse) {

        String mweathercity=mWeatherResponse.getName();
        mcityname.setText(mweathercity);
        String mIconURL="http://openweathermap.org/img/w/" + (mWeatherResponse.getWeather().get(0).getIcon()) +".png";
        mWeatherIcon.setImageURI(mIconURL);

        long unixSeconds = mWeatherResponse.getDt();
        Date date = new Date(unixSeconds*1000L);
        SimpleDateFormat daytime = new SimpleDateFormat("\"EEE, MMM d\"");
        daytime.setTimeZone(TimeZone.getTimeZone("GMT+3.5"));
        String formattedDate = daytime.format(date);
        daytime.setTimeZone(TimeZone.getTimeZone("GMT+3.5"));
        mDate.setText(formattedDate);

        Integer mweatherTemp=mWeatherResponse.getMain().getTemp().intValue();

        mTemp.setText(mweatherTemp.toString()+" °C");


        Integer mweathermax=mWeatherResponse.getMain().getTempMax().intValue();
        mMaxTemp.setText(mweathermax.toString());

        Integer mweathermin=mWeatherResponse.getMain().getTempMin().intValue();
        mMinTemp.setText(mweathermin.toString());

        mWeather.setText("'"+mWeatherResponse.getWeather().get(0).getDescription()+"'");

        long sunriseunix = mWeatherResponse.getSys().getSunrise();
        long sunrisedv = sunriseunix*1000;
        Date sunrisedf = new java.util.Date(sunrisedv);
        String sunrisevv = new SimpleDateFormat("hh:mma").format(sunrisedf);
        mSunrise.setText(sunrisevv);

        long sunsetunix = mWeatherResponse.getSys().getSunset();
        long sunsetdv = sunsetunix*1000;
        Date sunsetdf = new java.util.Date(sunsetdv);
        String sunsetvv = new SimpleDateFormat("hh:mma").format(sunsetdf);
        mSunset.setText(sunsetvv);

        Integer mweatherClouds=mWeatherResponse.getClouds().getAll().intValue();
        mClouds.setText(mweatherClouds.toString()+" %");

        Integer mweatherHumidity=mWeatherResponse.getMain().getHumidity().intValue();
        mHumidity.setText(mweatherHumidity.toString()+" %");

        Integer mweatherpressure=mWeatherResponse.getMain().getPressure().intValue();
        mPressure.setText(mweatherpressure.toString()+" hpa");

        Integer mweatherDirection=mWeatherResponse.getWind().getDeg().intValue();
        mDirection.setText(mweatherDirection.toString()+" m/s");

        Float mweatherspeed=mWeatherResponse.getWind().getSpeed();
        mSpeed.setText(mweatherspeed.toString()+" m/s");
        //chart3valueArray = new int[]{ mweatherTemp,mweathermin,mweathermax};

    }
    private void getDataFromResponse_forecast(ForecastListItem mforecastResponse) {

        Integer mweathermorning=mforecastResponse.getList().get(0).getTemp().getMorn().intValue();
        mMormingTemp.setText(mweathermorning.toString());

        Integer mweatherday=mforecastResponse.getList().get(0).getTemp().getDay().intValue();
        mDayTemp.setText(mweatherday.toString());

        Integer mweatherevening=mforecastResponse.getList().get(0).getTemp().getEve().intValue();
        mEveningTemp.setText(mweatherevening.toString());

        Integer mweathernight=mforecastResponse.getList().get(0).getTemp().getNight().intValue();
        mNightTemp.setText(mweathernight.toString());

    }

    }
