package com.amicus.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String API_KEY = "a7b76e3c606d48effb4e257c3ff96b01";
    private final String BASE_URL = "https://api.openweathermap.org";

    EditText edCity;
    Button buttonWeather;
    TextView textWeather;

    WeatherApi weatherApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edCity = findViewById(R.id.editCity);
        buttonWeather = findViewById(R.id.buttonWeather);
        textWeather = findViewById(R.id.textWeather);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherApi = retrofit.create(WeatherApi.class);

        buttonWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edCity.getText().toString();

                if (city.isEmpty()) {
                    edCity.setError("Введите город");
                    return;
                }
                textWeather.setText("Загрузка погоды...");

                getWeather(city);
            }
        });
    }

    private void getWeather(String ciy) {
        Call<JsonObject> call = weatherApi.getWeather(ciy, API_KEY, "metric", "ru");

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject json = response.body();
                    String cityName = json.get("name").getAsString();
                    JsonObject main = json.getAsJsonObject("main");
                    double temp = main.get("temp").getAsDouble();
                    int humidity = main.get("humidity").getAsInt();
                    JsonArray weatherArray = json.getAsJsonArray("weather");
                    JsonObject weather = weatherArray.get(0).getAsJsonObject();
                    String description = weather.get("description").getAsString();
                    JsonObject wind = json.getAsJsonObject("wind");
                    double windSpeed = wind.get("speed").getAsDouble();

                    String result = "City " + cityName + "\nTemp " + temp + "\nВлажность " + humidity + "\nПогода " + description + "\nскорость ветра " + windSpeed;
                    textWeather.setText(result);
                } else {
                    textWeather.setText("Ошибка сервера " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                textWeather.setText("Ошибка подключения "+throwable.getMessage());
            }
        });
    }
}
