package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

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

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeather();
            }
        });
    }

    public void getWeather() {
        String city_entered = editText.getText().toString();

            String url = "http://api.openweathermap.org/data/2.5/weather?q="+city_entered+"&appid=33fe9ce138c6c6607762c5cecd8f9eb7";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    wind1.setVisibility(View.VISIBLE);
                    humidity1.setVisibility(View.VISIBLE);
                    pressure1.setVisibility(View.VISIBLE);
                    cloudiness1.setVisibility(View.VISIBLE);

                    image_w.setVisibility(View.VISIBLE);
                    image_h.setVisibility(View.VISIBLE);
                    image_p.setVisibility(View.VISIBLE);
                    image_c.setVisibility(View.VISIBLE);

                    JSONObject jsonObject = new JSONObject(response);

                    String city_find = jsonObject.getString("name");
                    city.setText(city_find);


                    JSONObject jsonObject1 = jsonObject.getJSONObject("sys");

                    String country_find = jsonObject1.getString("country");
                    country.setText(country_find);


                    JSONObject jsonObject2 = jsonObject.getJSONObject("main");

                    String temperature_find = jsonObject2.getString("temp");
                    double temperature_c = Double.parseDouble(temperature_find) - 273.15;
                    temperature.setText( Math.round(temperature_c) + "°");

                    String feelsLike_find = jsonObject2.getString("feels_like");
                    double feelsLike_c = Double.parseDouble(feelsLike_find) - 273.15;
                    feelsLike.setText("Feels like " + Math.round(feelsLike_c) + "°");

                    String pressure_find = jsonObject2.getString("pressure");
                    pressure.setText(pressure_find + "hPa");

                    String humidity_find = jsonObject2.getString("humidity");
                    humidity.setText(humidity_find + "%");


                    JSONArray jsonArray = jsonObject.getJSONArray("weather");

                    JSONObject jsonObject3 = jsonArray.getJSONObject(0);
                    String img = jsonObject3.getString("icon");
                    Picasso.get().load("http://openweathermap.org/img/wn/" + img + "@2x.png").into(imageView);

                    String description_find = jsonObject3.getString("description");
                    description.setText(description_find);

                    String timezone_find = jsonObject.getString("timezone");
                    Calendar calendar = Calendar.getInstance();
                    Date date = new Date(calendar.getTimeInMillis());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy \nHH:mm");
                    TimeZone my_time = TimeZone.getTimeZone("UTC");
                    my_time.setRawOffset(Integer.parseInt(timezone_find) * 1000);
                    simpleDateFormat.setTimeZone(my_time);
                    String formattedDate =  simpleDateFormat.format(date);
                    MainActivity.this.date.setText(formattedDate);


                    JSONObject jsonObject4 = jsonObject.getJSONObject("wind");

                    String wind_find = jsonObject4.getString("speed");
                    wind.setText(wind_find + "m/s");


                    JSONObject jsonObject5 = jsonObject.getJSONObject("clouds");

                    String cloudiness_find = jsonObject5.getString("all");
                    cloudiness.setText(cloudiness_find + "%");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "There is no such location", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
}