package org.bahar.helios;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import org.bahar.helios.entity.ForecastListItem;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastActivity extends AppCompatActivity {

    private ListView mMyListListView;
    private ForecastListItem mForecastList;
    private TextView mCityname;
    private String mSharePref_units,mSharePref_cityid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setlocale();

        setContentView(R.layout.activity_forecast);
        getSupportActionBar().setTitle("Forecast 7days!");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViews();
        loadData();
    }

    private void findViews() {
        mMyListListView = (ListView) findViewById(R.id.activityForecast_listView_myList);
        mCityname=findViewById(R.id.activityForecast_TextView_cityname);

    }
    private void init_sharepreference(){

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("APIPARAM",MODE_PRIVATE);
        mSharePref_cityid= sharedpreferences.getString("cityID", "112931");
        mSharePref_units= sharedpreferences.getString("units", "Metric");

    }
    private void setlocale(){
        Locale myLocale = new Locale("en");
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
    private void initListView() {
        mMyListListView.setAdapter(new ForecastListAdapter(mForecastList));
        String mcity=mForecastList.getCity().getName();;
        mCityname.setText(mcity);
    }
    private void loadData() {
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
                mForecastList = response.body();
                initListView();
            }

            @Override
            public void onFailure(Call<ForecastListItem> call, Throwable t) {
                Toast.makeText(ForecastActivity.this, "Fail Connection to API", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);


    }

}
