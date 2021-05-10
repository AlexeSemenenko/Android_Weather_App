package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String API_KEY = "33fe9ce138c6c6607762c5cecd8f9eb7";
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 10000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String locationProvider = LocationManager.GPS_PROVIDER;

    EditText editText;
    ImageButton imageButton;
    ImageView imageView;
    TextView country;
    TextView city;
    TextView temperature;
    TextView description;
    TextView feelsLike;
    TextView date;
    TextView wind;
    TextView humidity;
    TextView pressure;
    TextView cloudiness;
    TextView wind1;
    TextView humidity1;
    TextView pressure1;
    TextView cloudiness1;
    ImageView image_w;
    ImageView image_h;
    ImageView image_p;
    ImageView image_c;
    ImageButton locationButton;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextPersonName);
        imageButton = findViewById(R.id.imageButton);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        temperature = findViewById(R.id.temperature);
        imageView = findViewById(R.id.image);
        description = findViewById(R.id.description);
        feelsLike = findViewById(R.id.feels);
        date = findViewById(R.id.date);
        wind = findViewById(R.id.wind2);
        humidity = findViewById(R.id.humidity2);
        pressure = findViewById(R.id.pressure2);
        cloudiness = findViewById(R.id.cloudiness2);
        wind1 = findViewById(R.id.wind);
        humidity1 = findViewById(R.id.humidity);
        pressure1 = findViewById(R.id.pressure);
        cloudiness1 = findViewById(R.id.cloudiness);
        image_w = findViewById(R.id.image_w);
        image_h = findViewById(R.id.image_h);
        image_p = findViewById(R.id.image_p);
        image_c = findViewById(R.id.image_c);
        locationButton = findViewById(R.id.location);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherByName();
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherByLocation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherByLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        if (requestCode == REQUEST_CODE) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Got location successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Can't get weather because you didn't provide access to the location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeImgVisibility() {
        image_w.setVisibility(View.VISIBLE);
        image_h.setVisibility(View.VISIBLE);
        image_p.setVisibility(View.VISIBLE);
        image_c.setVisibility(View.VISIBLE);
    }

    private void changeTextVisibility() {
        wind1.setVisibility(View.VISIBLE);
        humidity1.setVisibility(View.VISIBLE);
        pressure1.setVisibility(View.VISIBLE);
        cloudiness1.setVisibility(View.VISIBLE);
    }

    private  void updatedUI(WeatherData weatherData) {
        changeImgVisibility();
        changeTextVisibility();

        city.setText(weatherData.getCity());
        country.setText(weatherData.getCountry());
        temperature.setText(Math.round(weatherData.getTemperature()) + "°");
        feelsLike.setText("Feels like " + Math.round(weatherData.getFeelsLike()) + "°");
        pressure.setText(weatherData.getPressure() + "hPa");
        humidity.setText(weatherData.getHumidity() + "%");
        Picasso.get().load(weatherData.getImg()).into(imageView);
        description.setText(weatherData.getDescription());
        wind.setText(weatherData.getWind() + "m/s");
        cloudiness.setText(weatherData.getClouds() + "%");
        MainActivity.this.date.setText(weatherData.getDate());
    }

    private void getWeatherData(RequestParams requestParams) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(WEATHER_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                WeatherData weatherData = WeatherData.getAllData(response);

                updatedUI(weatherData);

                Toast.makeText(MainActivity.this, "Got weather successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(MainActivity.this, "Something went wrong: check internet connection or location you entered", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWeatherByName() {
        String city_entered = editText.getText().toString();

        RequestParams requestParams = new RequestParams();
        requestParams.put("q", city_entered);
        requestParams.put("appid", API_KEY);

        getWeatherData(requestParams);
    }

    private void getWeatherByLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                RequestParams requestParams = new RequestParams();
                requestParams.put("lat", latitude);
                requestParams.put("lon", longitude);
                requestParams.put("appid", API_KEY);

                getWeatherData(requestParams);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(MainActivity.this, "Something went wrong. Can't get weather", Toast.LENGTH_SHORT).show();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(locationProvider, MIN_TIME, MIN_DISTANCE, locationListener);
    }
}