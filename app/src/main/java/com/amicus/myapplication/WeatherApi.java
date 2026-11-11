package com.amicus.myapplication;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sizik on 11.11.2026.
 */
public interface WeatherApi {

    @GET("data/2.5/weather")
    Call<JsonObject> getWeather(@Query("q") String city,
                                @Query("appid") String apikey,
                                @Query("units") String units,
                                @Query("lang") String lang);

}
