package org.bahar.helios;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.bahar.helios.entity.ForecastListItem;
import org.bahar.helios.weatherentity.WeatherResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Bahar on 12/19/2017.
 */

public interface WebApi{


//    @GET("forecast/daily?APPID=913bebceaa8367d09d1da9a134c525ba&id=112931&units=metric")
//    Call<ForecastListItem> getForecastList();
//
//    @GET("weather?APPID=913bebceaa8367d09d1da9a134c525ba&id=112931&units=metric")
//    Call<WeatherResponse> getWeatherRes();


    @GET("forecast/daily?APPID=913bebceaa8367d09d1da9a134c525ba&units=metric")
    Call<ForecastListItem> getForecastList(@Query("id") String cityid);

    @GET("weather?appid=913bebceaa8367d09d1da9a134c525ba&units=metric")
    Call<WeatherResponse> getWeatherRes(@Query("id") String cityID);

}
